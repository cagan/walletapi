#!/bin/bash

# Start or verify Minikube is running
if ! minikube status &>/dev/null; then
  echo "Starting Minikube..."
  minikube start --driver=docker
else
  echo "Minikube is already running"
fi

# Set up Docker environment to use Minikube's Docker daemon
echo "Setting up Docker environment for Minikube..."
eval $(minikube docker-env)

# Navigate to project directory
cd "$(dirname "$0")"

# Build the Spring Boot application
echo "Building the Spring Boot application..."
if [ -f "mvnw" ]; then
  ./mvnw clean package -DskipTests
else
  mvn clean package -DskipTests
fi

# Check if build was successful
if [ ! -f "target/walletapi-0.0.1-SNAPSHOT.jar" ]; then
  echo "ERROR: Build failed or JAR file not found."
  exit 1
fi

# Build Docker image in Minikube's Docker environment
echo "Building Docker image in Minikube's Docker environment..."
docker build -t wallet-api:latest .

# Apply Kubernetes manifests
echo "Applying Kubernetes manifests..."
kubectl apply -f kubernetes/postgres-deployment.yaml
kubectl apply -f kubernetes/wallet-api-deployment.yaml

# Wait for deployments to become ready
echo "Waiting for deployments to become ready..."
kubectl wait --for=condition=available --timeout=300s deployment/postgres || echo "WARNING: Timed out waiting for postgres deployment"
kubectl wait --for=condition=available --timeout=300s deployment/wallet-api || echo "WARNING: Timed out waiting for wallet-api deployment"

# Get the NodePort and Minikube IP to access the API
NODE_PORT=$(kubectl get service wallet-api -o jsonpath='{.spec.ports[0].nodePort}')
MINIKUBE_IP=$(minikube ip)

echo "====================================================================="
echo "🚀 Wallet API is now running in Kubernetes!"
echo "📝 API is accessible at: http://$MINIKUBE_IP:$NODE_PORT"
echo "====================================================================="
echo ""
echo "Run the following commands to check status:"
echo "  kubectl get pods                 # Check running pods"
echo "  kubectl logs <pod-name>          # View pod logs"
echo "  kubectl get services             # List services"
echo ""
echo "To clean up deployment:"
echo "  kubectl delete -f kubernetes/wallet-api-deployment.yaml"
echo "  kubectl delete -f kubernetes/postgres-deployment.yaml"
echo "====================================================================="