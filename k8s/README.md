# Kubernetes Configuration Files

Thư mục này chứa tất cả các file cấu hình Kubernetes để triển khai 2 microservices lên cluster.

## 📁 Cấu trúc thư mục

```
k8s/
├── namespace.yaml                      # Tạo namespace cho ứng dụng
├── configmap.yaml                      # Cấu hình chung cho tất cả services
├── secret.yaml                         # Dữ liệu nhạy cảm (password, JWT, etc)
├── payment-service-deployment.yaml     # Deployment cho payment-service (Node 1)
├── user-service-deployment.yaml        # Deployment cho user-service (Node 2)
├── services.yaml                       # Kubernetes Services expose pods
├── ingress.yaml                        # Ingress cho expose services ra ngoài
├── deploy.sh                           # Script deploy cho Linux/Mac
├── deploy.ps1                          # Script deploy cho Windows PowerShell
└── README.md                           # File hướng dẫn này
```

## 🚀 Triển khai nhanh

### Trên Linux/Mac

```bash
chmod +x k8s/deploy.sh
./k8s/deploy.sh
```

### Trên Windows PowerShell

```powershell
.\k8s\deploy.ps1
```

### Hoặc triển khai từng file

```bash
# Tạo namespace
kubectl apply -f k8s/namespace.yaml

# Tạo ConfigMap và Secret
kubectl apply -f k8s/configmap.yaml
kubectl apply -f k8s/secret.yaml

# Tạo Services
kubectl apply -f k8s/services.yaml

# Triển khai Deployments
kubectl apply -f k8s/payment-service-deployment.yaml
kubectl apply -f k8s/user-service-deployment.yaml

# (Optional) Tạo Ingress
kubectl apply -f k8s/ingress.yaml
```

## 📋 Các file chi tiết

### `namespace.yaml`
- Tạo namespace `periodic-payment` để tách biệt resources

### `configmap.yaml`
- Chứa cấu hình chung cho cả 2 services
- Spring Profiles, Database URL, JPA settings
- Dữ liệu không nhạy cảm

### `secret.yaml`
- Chứa dữ liệu nhạy cảm: password DB, JWT secret
- Base64 encoded
- ⚠️ **CẢNH BÁO**: Trong production, sử dụng secret management system (Vault, AWS Secrets Manager, etc)

### `payment-service-deployment.yaml`
- Triển khai payment-service trên **Node 1**
- 1 pod, 1 container
- Health checks, resource limits, graceful shutdown
- nodeSelectorhey lần lượt cố định pod trên node1

### `user-service-deployment.yaml`
- Triển khai user-service trên **Node 2**
- 1 pod, 1 container
- Tương tự payment-service nhưng port 8081
- nodeSelector cố định pod trên node2

### `services.yaml`
- Expose cả 2 deployment thành Services
- Type: ClusterIP (internal network)
- Có thể thay đổi sang LoadBalancer hoặc NodePort

### `ingress.yaml`
- Expose services ra ngoài cluster
- Yêu cầu Ingress Controller (nginx, traefik, etc)
- Dùng domain names để routing

## ⚙️ Cấu hình quan trọng

### Node Selector
Trong `deployment` files, có phần:

```yaml
spec:
  nodeSelector:
    kubernetes.io/hostname: node1  # hoặc node2
```

**Cần thay đổi theo tên nodes thực tế của bạn:**

```bash
# Lấy danh sách nodes
kubectl get nodes

# Kiểm tra hostname của mỗi node
kubectl get nodes -o custom-columns=NAME:.metadata.name,HOSTNAME:.status.nodeInfo.hostname
```

### Image Registry

Trong `deployment` files:

```yaml
image: payment-service:1.0  # Local image
imagePullPolicy: IfNotPresent  # Không pull từ registry
```

**Nếu dùng Docker Hub Registry:**

```yaml
image: <your-username>/payment-service:1.0
imagePullPolicy: Always
```

## 🔍 Kiểm tra trạng thái

```bash
# Xem pods
kubectl get pods -n periodic-payment
kubectl get pods -n periodic-payment -o wide  # Xem pod ở node nào

# Xem deployments
kubectl get deployments -n periodic-payment

# Xem services
kubectl get services -n periodic-payment

# Xem events
kubectl get events -n periodic-payment

# Xem logs
kubectl logs -f deployment/payment-service -n periodic-payment
kubectl logs -f deployment/user-service -n periodic-payment

# Xem chi tiết pod
kubectl describe pod <pod-name> -n periodic-payment
```

## 🌐 Test kết nối

### Port Forward

```bash
# Terminal 1: Forward payment-service
kubectl port-forward service/payment-service 8080:8080 -n periodic-payment

# Terminal 2: Forward user-service
kubectl port-forward service/user-service 8081:8081 -n periodic-payment
```

Sau đó test:
```bash
curl http://localhost:8080/actuator/health
curl http://localhost:8081/actuator/health
```

### Gọi từ trong cluster

```bash
# Exec vào pod payment-service
kubectl exec -it <payment-pod-name> -n periodic-payment -- /bin/sh

# Bên trong pod, gọi user-service
curl http://user-service:8081/api/users
```

## 🔄 Cập nhật Deployments

### Cập nhật image

```bash
kubectl set image deployment/payment-service \
  payment-service=payment-service:2.0 \
  -n periodic-payment
```

### Rollout status

```bash
kubectl rollout status deployment/payment-service -n periodic-payment
```

### Rollback

```bash
kubectl rollout undo deployment/payment-service -n periodic-payment
```

## ❌ Xóa resources

### Xóa một deployment

```bash
kubectl delete deployment payment-service -n periodic-payment
```

### Xóa tất cả resources trong namespace

```bash
kubectl delete namespace periodic-payment
```

## 🔐 Security Best Practices

1. **Secrets**: Sử dụng Secret Management System
   - HashiCorp Vault
   - AWS Secrets Manager
   - Google Secret Manager
   - Azure Key Vault

2. **RBAC**: Tạo Service Accounts và Roles
3. **Network Policies**: Giới hạn traffic giữa pods
4. **Resource Quotas**: Giới hạn resources per namespace
5. **Pod Security Policies**: Enforce security standards

## 📚 Tài liệu tham khảo

- [Kubernetes Deployments](https://kubernetes.io/docs/concepts/workloads/controllers/deployment/)
- [Kubernetes Services](https://kubernetes.io/docs/concepts/services-networking/service/)
- [Kubernetes ConfigMaps](https://kubernetes.io/docs/concepts/configuration/configmap/)
- [Kubernetes Secrets](https://kubernetes.io/docs/concepts/configuration/secret/)
- [Node Selector](https://kubernetes.io/docs/concepts/scheduling-eviction/assign-pod-node/)
- [Pod Affinity](https://kubernetes.io/docs/concepts/scheduling-eviction/assign-pod-node/#affinity-and-anti-affinity)

## 💡 Tips

1. **Dry-run**: Test manifest trước khi apply
   ```bash
   kubectl apply -f k8s/ --dry-run=client
   ```

2. **Validate YAML**: Check syntax
   ```bash
   kubectl apply -f k8s/ --validate
   ```

3. **Watch status**: Monitor thay đổi real-time
   ```bash
   kubectl get pods -n periodic-payment -w
   ```

4. **Tail logs**: Xem logs real-time
   ```bash
   kubectl logs -f deployment/payment-service -n periodic-payment --all-containers=true
   ```

## ⚠️ Troubleshooting

### Pod không start

```bash
# Kiểm tra events
kubectl describe pod <pod-name> -n periodic-payment

# Xem logs chi tiết
kubectl logs <pod-name> -n periodic-payment --previous
```

### ImagePullBackOff

- Kiểm tra image name đúng không
- Kiểm tra image registry credentials
- Verify imagePullPolicy

### CrashLoopBackOff

- Xem logs pod
- Kiểm tra resource limits
- Kiểm tra health checks

### Node assignment không work

```bash
# Kiểm tra node labels
kubectl get nodes --show-labels

# Add label nếu cần
kubectl label nodes <node-name> <label-key>=<label-value>
```

---

**Chúc bạn triển khai thành công! 🚀**
