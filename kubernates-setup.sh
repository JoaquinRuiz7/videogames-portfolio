#!/usr/bin/env bash
set -euo pipefail

error() {
  echo "Error: $1" >&2
  exit 1
}

require_cmd() {
  command -v "$1" >/dev/null 2>&1 || error "$1 is required but not installed"
}

require_cmd docker
require_cmd kubectl

OS="$(uname -s)"

case "$OS" in
  Linux|Darwin)
    HOSTS_FILE="/etc/hosts"
    SUDO="sudo"
    ;;
  MINGW*|MSYS*|CYGWIN*)
    HOSTS_FILE="/c/Windows/System32/drivers/etc/hosts"
    SUDO=""
    ;;
  *)
    error "Unsupported OS: $OS"
    ;;
esac

HOST="videogames.local"
LOCAL_ADDR="127.0.0.1"

echo "Building docker images..."

docker build -t videogames-backend-service ./videogames-backend
docker build -t videogames-frontend-service ./videogames-frontend

echo "Installing nginx ingress controller..."

kubectl apply -f \
https://raw.githubusercontent.com/kubernetes/ingress-nginx/main/deploy/static/provider/cloud/deploy.yaml

echo "Waiting for ingress controller..."

kubectl wait --namespace ingress-nginx \
  --for=condition=ready pod \
  --selector=app.kubernetes.io/component=controller \
  --timeout=180s

echo ""
echo "Get an API key from https://rawg.io/apidocs"
echo ""

RAWG_API_KEY=${RAWG_API_KEY:-}

while [[ -z "$RAWG_API_KEY" ]]; do
  read -r -p "Enter your RAWG API key: " RAWG_API_KEY
done

echo "Creating secret..."

kubectl create secret generic videogames-backend-service-secrets \
  --from-literal=RAWG_API_KEY="$RAWG_API_KEY" \
  --dry-run=client -o yaml | kubectl apply -f -

echo "Deploying backend..."

kubectl apply -f ./videogames-backend/kubernates/deploy/manifest.yaml
kubectl apply -f ./videogames-backend/kubernates/service/manifest.yaml

echo "Deploying frontend..."

kubectl apply -f ./videogames-frontend/kubernates/config-map/manifest.yaml
kubectl apply -f ./videogames-frontend/kubernates/deploy/manifest.yaml
kubectl apply -f ./videogames-frontend/kubernates/service/manifest.yaml
kubectl apply -f ./videogames-frontend/kubernates/ingress/manifest.yaml

echo "Waiting for ingress resource..."

for i in {1..40}; do
  if kubectl get ingress videogames-ingress >/dev/null 2>&1; then
    break
  fi
  echo "Ingress not ready yet..."
  sleep 3
done

kubectl get ingress videogames-ingress >/dev/null 2>&1 || error "Ingress was not created"

echo "Ingress detected."

if grep -q "$HOST" "$HOSTS_FILE"; then
  echo "$HOST already exists in hosts file"
else
  echo "Adding $HOST → $LOCAL_ADDR to hosts file"

  echo "$LOCAL_ADDR $HOST" | $SUDO tee -a "$HOSTS_FILE" >/dev/null || {
    echo "Could not modify hosts automatically."
    echo "Add manually:"
    echo "$LOCAL_ADDR $HOST"
  }
fi

echo ""
echo "Application available at:"
echo "http://$HOST"