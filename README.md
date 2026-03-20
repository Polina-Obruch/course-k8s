# snowflake-id-generator

## Запуск в Kubernetes

```bash
# Применить все манифесты
kubectl apply -f k8s/
kubectl apply -f k8s/deployment/app-deployment.yaml -f k8s/service/app-service.yaml

# Проверить, что поды запустились
kubectl get pods

#Проверка приложения
curl localhost:30080/next-id # Указываем порт из app-service.yaml
