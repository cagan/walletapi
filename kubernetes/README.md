# Kubernetes Deployment

This directory contains configuration files for deploying the Wallet API application on Kubernetes.

## Prerequisites

- Kubernetes cluster (Minikube, Docker Desktop with Kubernetes, or KIND)
- kubectl CLI tool
- Docker

## Deployment

1. Make sure your Kubernetes cluster is running:
   ```bash
   # For Minikube
   minikube start
   
   # For Docker Desktop, enable Kubernetes in settings
   ```

2. Run the deployment script:
   ```bash
   ./deploy.sh
   ```

3. The script will:
   - Build the application using Maven
   - Create a Docker image
   - Deploy PostgreSQL database
   - Deploy the Wallet API application
   - Display the URL to access the API

## Manual Deployment

If you prefer to deploy manually:

1. Build the application:
   ```bash
   ./mvnw clean package -DskipTests
   ```

2. Build the Docker image:
   ```bash
   docker build -t wallet-api:latest .
   ```

3. Apply Kubernetes manifests:
   ```bash
   kubectl apply -f kubernetes/postgres-deployment.yaml
   kubectl apply -f kubernetes/wallet-api-deployment.yaml
   ```

4. Get the NodePort to access the API:
   ```bash
   kubectl get service wallet-api
   ```

## Clean Up

To remove the deployment:
```bash
kubectl delete -f kubernetes/wallet-api-deployment.yaml
kubectl delete -f kubernetes/postgres-deployment.yaml
```