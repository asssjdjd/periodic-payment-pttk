# Kubernetes Deployment Script for Windows PowerShell

$NAMESPACE = "periodic-payment"
$K8S_DIR = "k8s"

Write-Host "=====================================" -ForegroundColor Blue
Write-Host "  Kubernetes Deployment Script" -ForegroundColor Blue
Write-Host "=====================================" -ForegroundColor Blue
Write-Host ""

# Step 1: Check kubectl
Write-Host "[1/6] Checking kubectl..." -ForegroundColor Yellow
try {
    kubectl version --client | Out-Null
    Write-Host "✅ kubectl is installed" -ForegroundColor Green
} catch {
    Write-Host "❌ kubectl not found. Please install kubectl." -ForegroundColor Red
    exit 1
}
Write-Host ""

# Step 2: Check cluster
Write-Host "[2/6] Checking Kubernetes cluster..." -ForegroundColor Yellow
try {
    kubectl cluster-info | Out-Null
    kubectl cluster-info
    Write-Host "✅ Kubernetes cluster is running" -ForegroundColor Green
} catch {
    Write-Host "❌ Kubernetes cluster is not running." -ForegroundColor Red
    exit 1
}
Write-Host ""

# Step 3: Get nodes and display
Write-Host "[3/6] Checking nodes..." -ForegroundColor Yellow
Write-Host "Available nodes:" -ForegroundColor Blue
kubectl get nodes -o wide
Write-Host ""

# Step 4: Create namespace
Write-Host "[4/6] Creating namespace..." -ForegroundColor Yellow
kubectl apply -f "$K8S_DIR/namespace.yaml"
Write-Host "✅ Namespace created" -ForegroundColor Green
Write-Host ""

# Step 5: Apply configs
Write-Host "[5/6] Applying ConfigMaps, Secrets, Services, and Deployments..." -ForegroundColor Yellow

Write-Host "Applying ConfigMap..." -ForegroundColor Blue
kubectl apply -f "$K8S_DIR/configmap.yaml"
Write-Host "✅ ConfigMap applied" -ForegroundColor Green

Write-Host "Applying Secret..." -ForegroundColor Blue
kubectl apply -f "$K8S_DIR/secret.yaml"
Write-Host "✅ Secret applied" -ForegroundColor Green

Write-Host "Applying Services..." -ForegroundColor Blue
kubectl apply -f "$K8S_DIR/services.yaml"
Write-Host "✅ Services applied" -ForegroundColor Green

Write-Host "Applying Payment Service Deployment..." -ForegroundColor Blue
kubectl apply -f "$K8S_DIR/payment-service-deployment.yaml"
Write-Host "✅ Payment Service Deployment applied" -ForegroundColor Green

Write-Host "Applying User Service Deployment..." -ForegroundColor Blue
kubectl apply -f "$K8S_DIR/user-service-deployment.yaml"
Write-Host "✅ User Service Deployment applied" -ForegroundColor Green

Write-Host ""

# Step 6: Wait and check status
Write-Host "[6/6] Waiting for pods to be ready..." -ForegroundColor Yellow
Start-Sleep -Seconds 5

Write-Host ""
Write-Host "=====================================" -ForegroundColor Blue
Write-Host "  Deployment Status" -ForegroundColor Blue
Write-Host "=====================================" -ForegroundColor Blue
Write-Host ""

Write-Host "Pods:" -ForegroundColor Blue
kubectl get pods -n $NAMESPACE -o wide

Write-Host ""
Write-Host "Services:" -ForegroundColor Blue
kubectl get services -n $NAMESPACE

Write-Host ""
Write-Host "Deployments:" -ForegroundColor Blue
kubectl get deployments -n $NAMESPACE

Write-Host ""
Write-Host "✅ Deployment complete!" -ForegroundColor Green
Write-Host ""
Write-Host "Useful commands:" -ForegroundColor Blue
Write-Host "  📊 Check pod status:     kubectl get pods -n $NAMESPACE -o wide"
Write-Host "  📝 View logs:            kubectl logs -f deployment/payment-service -n $NAMESPACE"
Write-Host "  🔍 Describe pod:         kubectl describe pod <pod-name> -n $NAMESPACE"
Write-Host "  🌐 Port forward:         kubectl port-forward service/payment-service 8080:8080 -n $NAMESPACE"
Write-Host "  ❌ Delete deployment:    kubectl delete deployment --all -n $NAMESPACE"
Write-Host ""
