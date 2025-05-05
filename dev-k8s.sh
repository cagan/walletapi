#!/bin/bash

# Colors for better output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[0;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print a header
print_header() {
  echo -e "\n${BLUE}====================================================================${NC}"
  echo -e "${BLUE}$1${NC}"
  echo -e "${BLUE}====================================================================${NC}\n"
}

# Function to print success message
print_success() {
  echo -e "${GREEN}✓ $1${NC}"
}

# Function to print error message
print_error() {
  echo -e "${RED}✗ $1${NC}"
}

# Function to print info message
print_info() {
  echo -e "${YELLOW}i $1${NC}"
}

# Check if a command exists
command_exists() {
  command -v "$1" >/dev/null 2>&1
}

# Navigate to project directory
cd "$(dirname "$0")"

print_header "Wallet API - Local Kubernetes Development"

# Check for required tools
print_info "Checking for required tools..."

if ! command_exists minikube; then
  print_error "Minikube is not installed. Please install it first."
  echo "Visit: https://minikube.sigs.k8s.io/docs/start/"
  exit 1
fi

if ! command_exists kubectl; then
  print_error "kubectl is not installed. Please install it first."
  echo "Visit: https://kubernetes.io/docs/tasks/tools/install-kubectl/"
  exit 1
fi

if ! command_exists docker; then
  print_error "Docker is not installed. Please install it first."
  echo "Visit: https://docs.docker.com/get-docker/"
  exit 1
fi

# Start or verify Minikube is running
print_info "Checking Minikube status..."
if ! minikube status &>/dev/null; then
  print_info "Starting Minikube..."
  minikube start --driver=docker
  if [ $? -ne 0 ]; then
    print_error "Failed to start Minikube. Please check your Docker installation."
    exit 1
  fi
  print_success "Minikube started successfully"
else
  print_success "Minikube is already running"
fi

# Set up Docker environment to use Minikube's Docker daemon
print_info "Setting up Docker environment for Minikube..."
eval $(minikube docker-env)
print_success "Docker environment configured to use Minikube's Docker daemon"

# Build the Spring Boot application
print_header "Building Spring Boot Application"
if [ -f "mvnw" ]; then
  ./mvnw clean package -DskipTests
else
  mvn clean package -DskipTests
fi

# Check if build was successful
if [ ! -f "target/walletapi-0.0.1-SNAPSHOT.jar" ]; then
  print_error "Build failed or JAR file not found."
  exit 1
fi
print_success "Build completed successfully"

# Build Docker image in Minikube's Docker environment
print_header "Building Docker Image"
print_info "Building Docker image in Minikube's Docker environment..."
docker build -t wallet-api:latest .
print_success "Docker image built successfully"

# Apply Kubernetes manifests
print_header "Deploying to Kubernetes"
print_info "Applying Kubernetes manifests..."
kubectl apply -f kubernetes/postgres-deployment.yaml
kubectl apply -f kubernetes/wallet-api-deployment.yaml

# Wait for deployments to become ready
print_info "Waiting for deployments to become ready..."
kubectl wait --for=condition=available --timeout=300s deployment/postgres || print_error "Timed out waiting for postgres deployment"
kubectl wait --for=condition=available --timeout=300s deployment/wallet-api || print_error "Timed out waiting for wallet-api deployment"

# Get the NodePort and Minikube IP to access the API
NODE_PORT=$(kubectl get service wallet-api -o jsonpath='{.spec.ports[0].nodePort}')
MINIKUBE_IP=$(minikube ip)

print_header "🚀 Wallet API is now running in Kubernetes!"
echo -e "📝 API is accessible at: ${GREEN}http://$MINIKUBE_IP:$NODE_PORT${NC}"
echo ""
echo "Run the following commands to check status:"
echo "  kubectl get pods                 # Check running pods"
echo "  kubectl logs <pod-name>          # View pod logs"
echo "  kubectl get services             # List services"
echo ""
echo "To open the API in your browser:"
echo "  minikube service wallet-api      # This will open the API in your browser"
echo ""
echo "For local development with Kubernetes:"
echo "  1. Make code changes"
echo "  2. Re-run this script to rebuild and redeploy"
echo ""
echo "To clean up deployment:"
echo -e "${YELLOW}  kubectl delete -f kubernetes/wallet-api-deployment.yaml${NC}"
echo -e "${YELLOW}  kubectl delete -f kubernetes/postgres-deployment.yaml${NC}"
echo -e "${YELLOW}  minikube stop                                         # Stop Minikube${NC}"