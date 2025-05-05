#!/bin/bash

# Start Minikube
echo "Starting Minikube..."
minikube start

# Verify Minikube is running
echo "Verifying Minikube status..."
minikube status

# Use minikube docker environment for local images
echo "Setting up Docker environment for Minikube..."
eval $(minikube docker-env)
echo "Docker environment configured to use Minikube's Docker daemon"

echo "Minikube setup complete. You can now run the deploy.sh script."