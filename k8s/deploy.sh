#!/bin/bash

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

NAMESPACE="periodic-payment"

echo -e "${BLUE}=====================================${NC}"
echo -e "${BLUE}  Kubernetes Deployment Script${NC}"
echo -e "${BLUE}=====================================${NC}"
echo ""

# Step 1: Check kubectl
echo -e "${YELLOW}[1/6] Checking kubectl...${NC}"
if ! command -v kubectl &> /dev/null; then
    echo -e "${RED}❌ kubectl not found. Please install kubectl.${NC}"
    exit 1
fi
echo -e "${GREEN}✅ kubectl is installed${NC}"
echo ""

# Step 2: Check cluster
echo -e "${YELLOW}[2/6] Checking Kubernetes cluster...${NC}"
if ! kubectl cluster-info &> /dev/null; then
    echo -e "${RED}❌ Kubernetes cluster is not running.${NC}"
    exit 1
fi
kubectl cluster-info
echo -e "${GREEN}✅ Kubernetes cluster is running${NC}"
echo ""

# Step 3: Get nodes and display
echo -e "${YELLOW}[3/6] Checking nodes...${NC}"
echo -e "${BLUE}Available nodes:${NC}"
kubectl get nodes -o wide
echo ""

# Step 4: Create namespace
echo -e "${YELLOW}[4/6] Creating namespace...${NC}"
kubectl apply -f k8s/namespace.yaml
echo -e "${GREEN}✅ Namespace created${NC}"
echo ""

# Step 5: Apply configs
echo -e "${YELLOW}[5/6] Applying ConfigMaps, Secrets, Services, and Deployments...${NC}"
echo -e "${BLUE}Applying ConfigMap...${NC}"
kubectl apply -f k8s/configmap.yaml
echo -e "${GREEN}✅ ConfigMap applied${NC}"

echo -e "${BLUE}Applying Secret...${NC}"
kubectl apply -f k8s/secret.yaml
echo -e "${GREEN}✅ Secret applied${NC}"

echo -e "${BLUE}Applying Services...${NC}"
kubectl apply -f k8s/services.yaml
echo -e "${GREEN}✅ Services applied${NC}"

echo -e "${BLUE}Applying Payment Service Deployment...${NC}"
kubectl apply -f k8s/payment-service-deployment.yaml
echo -e "${GREEN}✅ Payment Service Deployment applied${NC}"

echo -e "${BLUE}Applying User Service Deployment...${NC}"
kubectl apply -f k8s/user-service-deployment.yaml
echo -e "${GREEN}✅ User Service Deployment applied${NC}"

echo ""

# Step 6: Wait and check status
echo -e "${YELLOW}[6/6] Waiting for pods to be ready...${NC}"
sleep 5

echo ""
echo -e "${BLUE}=====================================${NC}"
echo -e "${BLUE}  Deployment Status${NC}"
echo -e "${BLUE}=====================================${NC}"
echo ""

echo -e "${BLUE}Pods:${NC}"
kubectl get pods -n $NAMESPACE -o wide

echo ""
echo -e "${BLUE}Services:${NC}"
kubectl get services -n $NAMESPACE

echo ""
echo -e "${BLUE}Deployments:${NC}"
kubectl get deployments -n $NAMESPACE

echo ""
echo -e "${GREEN}✅ Deployment complete!${NC}"
echo ""
echo -e "${BLUE}Useful commands:${NC}"
echo "  📊 Check pod status:     kubectl get pods -n $NAMESPACE -o wide"
echo "  📝 View logs:            kubectl logs -f deployment/payment-service -n $NAMESPACE"
echo "  🔍 Describe pod:         kubectl describe pod <pod-name> -n $NAMESPACE"
echo "  🌐 Port forward:         kubectl port-forward service/payment-service 8080:8080 -n $NAMESPACE"
echo "  ❌ Delete deployment:    kubectl delete deployment --all -n $NAMESPACE"
echo ""
