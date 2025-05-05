#!/bin/bash

# Navigate to the project root directory
cd "$(dirname "$0")/.."

# Check if Kubernetes is running
if ! kubectl cluster-info &> /dev/null; then
  echo "ERROR: Kubernetes is not running. Please start your Kubernetes cluster first."
  exit 1
fi

# Check if running with Minikube
MINIKUBE_RUNNING=$(minikube status 2>/dev/null | grep "Running" | wc -l)
if [ "$MINIKUBE_RUNNING" -gt 0 ]; then
  echo "Minikube detected. Using Minikube's Docker environment..."
  eval $(minikube docker-env)
fi

# Build and tag the application
echo "Building the application..."
if [ -f "mvnw" ]; then
  ./mvnw clean package -DskipTests
else
  echo "Maven wrapper not found, trying with 'mvn'..."
  mvn clean package -DskipTests
fi

# Check if build was successful
if [ ! -f "target/walletapi-0.0.1-SNAPSHOT.jar" ]; then
  echo "ERROR: Build failed or JAR file not found."
  exit 1
fi

# Build docker image
echo "Building Docker image for Kubernetes..."
docker build -t wallet-api:latest .

# Apply Kubernetes manifests
echo "Applying Kubernetes manifests..."
kubectl apply -f kubernetes/postgres-pvc.yaml
kubectl apply -f kubernetes/postgres-deployment.yaml

# Wait for postgres to initialize properly
echo "Waiting for PostgreSQL to initialize..."
kubectl wait --for=condition=available --timeout=120s deployment/postgres || echo "WARNING: Timed out waiting for postgres deployment"
sleep 10  # Give a bit more time for database initialization

# Apply Wallet API deployment
kubectl apply -f kubernetes/wallet-api-deployment.yaml

# Apply API Gateway deployment
kubectl apply -f kubernetes/gateway-deployment.yaml

# Wait for deployments to become ready
echo "Waiting for deployments to become ready..."
kubectl wait --for=condition=available --timeout=300s deployment/postgres || echo "WARNING: Timed out waiting for postgres deployment"
kubectl wait --for=condition=available --timeout=300s deployment/wallet-api || echo "WARNING: Timed out waiting for wallet-api deployment"
kubectl wait --for=condition=available --timeout=300s deployment/api-gateway || echo "WARNING: Timed out waiting for api-gateway deployment"

# Get the URL to access the application via gateway
LOAD_BALANCER_IP=$(kubectl get service api-gateway -o jsonpath='{.status.loadBalancer.ingress[0].ip}' 2>/dev/null)
if [ -n "$LOAD_BALANCER_IP" ]; then
  echo "Wallet API is accessible via API Gateway at http://$LOAD_BALANCER_IP"
else
  echo "External IP not yet assigned to LoadBalancer. Check service status with: kubectl get svc api-gateway"
  
  # Fallback to NodePort access for local development
  NODE_PORT=$(kubectl get service wallet-api -o jsonpath='{.spec.ports[0].nodePort}' 2>/dev/null)
  if [ -n "$NODE_PORT" ]; then
    echo "Direct wallet-api access is available at http://localhost:$NODE_PORT (bypassing gateway)"
  fi
fi

echo "Deployment complete. Check pod status with: kubectl get pods"