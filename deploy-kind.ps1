# ============================================
# Deploy 2 Services lên Kind Kubernetes
# ============================================

param(
    [string]$Action = "deploy",  # deploy, cleanup, logs
    [string]$ClusterName = "periodic-payment"
)

# Colors
$GREEN = [char]27 + "[32m"
$YELLOW = [char]27 + "[33m"
$RED = [char]27 + "[31m"
$BLUE = [char]27 + "[34m"
$NC = [char]27 + "[0m"

function Write-Success {
    param([string]$Message)
    Write-Host "$GREEN✅ $Message$NC"
}

function Write-Error-Custom {
    param([string]$Message)
    Write-Host "$RED❌ $Message$NC"
}

function Write-Info {
    param([string]$Message)
    Write-Host "$BLUE[INFO]$NC $Message"
}

function Write-Warning-Custom {
    param([string]$Message)
    Write-Host "$YELLOW[WARNING]$NC $Message"
}

# ============================================
# FUNCTION: Create Kind Cluster
# ============================================
function Create-KindCluster {
    Write-Info "Tạo Kind cluster '$ClusterName'..."
    
    $clusterExists = kind get clusters | Select-String $ClusterName
    if ($clusterExists) {
        Write-Warning-Custom "Cluster '$ClusterName' đã tồn tại. Bỏ qua..."
        return
    }
    
    Write-Info "Đợi 3-5 phút để cluster tạo xong..."
    kind create cluster --name $ClusterName
    Write-Success "Cluster '$ClusterName' tạo thành công"
    
    # Set context
    kubectl config use-context "kind-$ClusterName"
    Write-Success "Context đặt thành: kind-$ClusterName"
}

# ============================================
# FUNCTION: Load Docker Images
# ============================================
function Load-DockerImages {
    Write-Info "Load Docker images vào Kind..."
    
    $images = @("payment-service:1.0", "user-service:1.0")
    
    foreach ($image in $images) {
        Write-Info "Loading $image..."
        kind load docker-image $image --name $ClusterName
        if ($LASTEXITCODE -eq 0) {
            Write-Success "Loaded $image"
        } else {
            Write-Error-Custom "Failed to load $image"
            return $false
        }
    }
    
    return $true
}

# ============================================
# FUNCTION: Deploy to Kubernetes
# ============================================
function Deploy-Services {
    Write-Info "Triển khai services..."
    
    # Create namespace
    Write-Info "Creating namespace..."
    kubectl create namespace periodic-payment 2>$null
    
    # Apply configs
    $files = @(
        "k8s/configmap.yaml",
        "k8s/secret.yaml",
        "k8s/services.yaml"
    )
    
    foreach ($file in $files) {
        Write-Info "Applying $file..."
        kubectl apply -f $file 2>$null
    }
    
    Write-Success "ConfigMap, Secret, Services applied"
    
    # Apply deployments
    Write-Info "Deploying payment-service..."
    kubectl apply -f k8s/payment-service-deployment.yaml 2>$null
    Write-Success "payment-service deployment applied"
    
    Write-Info "Deploying user-service..."
    kubectl apply -f k8s/user-service-deployment.yaml 2>$null
    Write-Success "user-service deployment applied"
    
    # Wait for pods
    Write-Info "Chờ pods khởi động..."
    Start-Sleep -Seconds 5
    
    # Check status
    Write-Info "Kiểm tra status..."
    kubectl get pods -n periodic-payment -o wide
}

# ============================================
# FUNCTION: Show Logs
# ============================================
function Show-Logs {
    Write-Info "Hiển thị logs..."
    Write-Info "Payment Service logs:"
    kubectl logs -f deployment/payment-service -n periodic-payment
}

# ============================================
# FUNCTION: Port Forward
# ============================================
function Port-Forward {
    Write-Info "Port forwarding..."
    Write-Info "Payment Service: localhost:8080"
    Write-Info "User Service: localhost:8081"
    
    kubectl port-forward service/payment-service 8080:8080 -n periodic-payment &
    kubectl port-forward service/user-service 8081:8081 -n periodic-payment &
    
    Write-Info "Test: curl http://localhost:8080/actuator/health"
    Write-Info "Test: curl http://localhost:8081/actuator/health"
}

# ============================================
# FUNCTION: Cleanup
# ============================================
function Cleanup {
    Write-Warning-Custom "Xóa cluster '$ClusterName'..."
    
    $confirm = Read-Host "Bạn chắc chắn muốn xóa cluster không? (yes/no)"
    if ($confirm -eq "yes") {
        kind delete cluster --name $ClusterName
        Write-Success "Cluster '$ClusterName' đã xóa"
    } else {
        Write-Info "Hủy xóa"
    }
}

# ============================================
# MAIN
# ============================================
Write-Host ""
Write-Host "$BLUE========================================$NC"
Write-Host "$BLUE  Deploy Kind Kubernetes Script$NC"
Write-Host "$BLUE========================================$NC"
Write-Host ""

switch ($Action.ToLower()) {
    "deploy" {
        Write-Info "=== Full Deploy Flow ==="
        
        # Step 1: Create cluster
        Create-KindCluster
        
        # Step 2: Verify cluster
        Write-Info "Verifying cluster..."
        $nodes = kubectl get nodes -o json | ConvertFrom-Json
        Write-Info "Nodes: $($nodes.items.Count)"
        
        # Step 3: Load images
        if (-not (Load-DockerImages)) {
            Write-Error-Custom "Failed to load images"
            exit 1
        }
        
        # Step 4: Deploy services
        Deploy-Services
        
        # Step 5: Status
        Write-Info ""
        Write-Info "=== Status ==="
        kubectl get pods -n periodic-payment -o wide
        kubectl get services -n periodic-payment
        
        Write-Info ""
        Write-Success "Deploy thành công!"
        Write-Info ""
        Write-Info "Tiếp theo:"
        Write-Info "1. Port forward: .\deploy-kind.ps1 -Action portforward"
        Write-Info "2. View logs: .\deploy-kind.ps1 -Action logs"
        Write-Info "3. Cleanup: .\deploy-kind.ps1 -Action cleanup"
    }
    
    "portforward" {
        Port-Forward
    }
    
    "logs" {
        Show-Logs
    }
    
    "cleanup" {
        Cleanup
    }
    
    default {
        Write-Host "Usage: .\deploy-kind.ps1 -Action [deploy|portforward|logs|cleanup]"
        Write-Host ""
        Write-Host "Examples:"
        Write-Host "  .\deploy-kind.ps1 -Action deploy          # Deploy mới"
        Write-Host "  .\deploy-kind.ps1 -Action portforward     # Port forward"
        Write-Host "  .\deploy-kind.ps1 -Action logs            # View logs"
        Write-Host "  .\deploy-kind.ps1 -Action cleanup         # Xóa cluster"
    }
}

Write-Host ""
