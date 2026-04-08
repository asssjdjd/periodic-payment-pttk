# ============================================
# 🎬 DEMO SCRIPT - Show Kubernetes Deployment
# ============================================

param(
    [string]$Action = "full"  # full, quick, health-check
)

# Colors
$GREEN = [char]27 + "[32m"
$YELLOW = [char]27 + "[33m"
$RED = [char]27 + "[31m"
$BLUE = [char]27 + "[34m"
$CYAN = [char]27 + "[36m"
$NC = [char]27 + "[0m"

function Show-Title {
    param([string]$Message)
    Write-Host ""
    Write-Host "$BLUE╔════════════════════════════════════════════════════════════╗$NC"
    Write-Host "$BLUE║  $Message$NC"
    Write-Host "$BLUE╚════════════════════════════════════════════════════════════╝$NC"
    Write-Host ""
}

function Show-Section {
    param([string]$Message)
    Write-Host "$CYAN▶ $Message$NC" -ForegroundColor Cyan
    Write-Host "$CYAN" + ("─" * 60) + "$NC"
}

function Show-Success {
    param([string]$Message)
    Write-Host "$GREEN✅ $Message$NC"
}

function Show-Info {
    param([string]$Message)
    Write-Host "$BLUE➜ $Message$NC"
}

# ============================================
# DEMO: Full
# ============================================
function Demo-Full {
    Show-Title "KUBERNETES DEPLOYMENT DEMO"
    
    # 1. Docker Images
    Show-Section "1️⃣  DOCKER IMAGES"
    Write-Host "Showing built Docker images:"
    Write-Host ""
    docker images | Select-String "service" | ForEach-Object {
        Write-Host "$GREEN✓$NC $_"
    }
    
    Start-Sleep -Seconds 2
    Write-Host ""
    
    # 2. Kubernetes Cluster
    Show-Section "2️⃣  KUBERNETES CLUSTER INFO"
    Write-Host "Cluster Status:"
    kubectl cluster-info 2>$null | Select-String "control plane" | ForEach-Object {
        Write-Host "$GREEN✓$NC $_"
    }
    
    Write-Host ""
    Write-Host "Nodes:"
    kubectl get nodes --no-headers | ForEach-Object {
        Write-Host "$GREEN✓$NC $_"
    }
    
    Start-Sleep -Seconds 2
    Write-Host ""
    
    # 3. Pods
    Show-Section "3️⃣  PODS DEPLOYMENT"
    Write-Host "Pods in namespace 'periodic-payment':"
    Write-Host ""
    kubectl get pods -n periodic-payment -o wide --no-headers | ForEach-Object {
        Write-Host "$GREEN✓$NC $_"
    }
    
    Start-Sleep -Seconds 2
    Write-Host ""
    
    # 4. Services
    Show-Section "4️⃣  KUBERNETES SERVICES"
    Write-Host "Services exposing pods:"
    Write-Host ""
    kubectl get services -n periodic-payment --no-headers | ForEach-Object {
        Write-Host "$GREEN✓$NC $_"
    }
    
    Start-Sleep -Seconds 2
    Write-Host ""
    
    # 5. Deployments
    Show-Section "5️⃣  DEPLOYMENTS"
    Write-Host "Deployment status:"
    Write-Host ""
    kubectl get deployments -n periodic-payment --no-headers | ForEach-Object {
        Write-Host "$GREEN✓$NC $_"
    }
    
    Start-Sleep -Seconds 2
    Write-Host ""
    
    # 6. Pod Details
    Show-Section "6️⃣  POD DETAILS (Payment Service)"
    Write-Host ""
    $pod = kubectl get pods -n periodic-payment -o name | Select-String "payment" | Select-Object -First 1
    kubectl describe $pod -n periodic-payment | Select-Object -First 40 | ForEach-Object {
        if ($_ -match "Node:|Status:|Ready:|Image:|Port:") {
            Write-Host "$GREEN✓$NC $_"
        }
    }
    
    Start-Sleep -Seconds 2
    Write-Host ""
    
    # 7. Application Logs
    Show-Section "7️⃣  APPLICATION LOGS (Last 20 lines)"
    Write-Host ""
    kubectl logs deployment/payment-service -n periodic-payment 2>$null | Select-Object -Last 20 | ForEach-Object {
        Write-Host "$CYAN$_$NC"
    }
    
    Write-Host ""
    Show-Title "✅ DEMO COMPLETE!"
}

# ============================================
# DEMO: Quick
# ============================================
function Demo-Quick {
    Show-Title "QUICK DEMO"
    
    Show-Section "Nodes"
    kubectl get nodes
    
    Show-Section "Pods"
    kubectl get pods -n periodic-payment -o wide
    
    Show-Section "Services"
    kubectl get services -n periodic-payment
    
    Show-Title "✅ DONE!"
}

# ============================================
# DEMO: Health Check
# ============================================
function Demo-HealthCheck {
    Show-Title "HEALTH CHECK TEST"
    
    Show-Section "Testing Payment Service"
    Write-Host "Running: kubectl port-forward service/payment-service 8080:8080"
    $job1 = Start-Job -ScriptBlock {
        kubectl port-forward service/payment-service 8080:8080 -n periodic-payment 2>$null
    }
    
    Start-Sleep -Seconds 2
    
    Write-Host "Testing health endpoint..."
    try {
        $response = Invoke-WebRequest -Uri "http://localhost:8080/actuator/health" -ErrorAction SilentlyContinue
        if ($response.StatusCode -eq 200) {
            Show-Success "Payment Service is UP! (Status: $($response.StatusCode))"
            Write-Host $response.Content
        }
    } catch {
        Write-Host "$RED❌ Failed to connect to Payment Service$NC"
    }
    
    Stop-Job $job1 2>$null
    
    Write-Host ""
    Show-Section "Testing User Service"
    Write-Host "Running: kubectl port-forward service/user-service 8081:8081"
    $job2 = Start-Job -ScriptBlock {
        kubectl port-forward service/user-service 8081:8081 -n periodic-payment 2>$null
    }
    
    Start-Sleep -Seconds 2
    
    Write-Host "Testing health endpoint..."
    try {
        $response = Invoke-WebRequest -Uri "http://localhost:8081/actuator/health" -ErrorAction SilentlyContinue
        if ($response.StatusCode -eq 200) {
            Show-Success "User Service is UP! (Status: $($response.StatusCode))"
            Write-Host $response.Content
        }
    } catch {
        Write-Host "$RED❌ Failed to connect to User Service$NC"
    }
    
    Stop-Job $job2 2>$null
    
    Write-Host ""
    Show-Title "✅ HEALTH CHECK COMPLETE!"
}

# ============================================
# MAIN
# ============================================
Write-Host ""

switch ($Action.ToLower()) {
    "full" {
        Demo-Full
    }
    "quick" {
        Demo-Quick
    }
    "health-check" {
        Demo-HealthCheck
    }
    default {
        Write-Host "Usage: .\demo.ps1 -Action [full|quick|health-check]"
        Write-Host ""
        Write-Host "Examples:"
        Write-Host "  .\demo.ps1                        # Run full demo"
        Write-Host "  .\demo.ps1 -Action quick          # Quick overview"
        Write-Host "  .\demo.ps1 -Action health-check   # Test health endpoints"
    }
}

Write-Host ""
