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

echo "Building docker images..."
docker compose build

echo "Installing nginx ingress controller..."
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/main/deploy/static/provider/cloud/deploy.yaml

echo "Waiting for ingress controller..."
kubectl wait --namespace ingress-nginx \
  --for=condition=ready pod \
  --selector=app.kubernetes.io/component=controller \
  --timeout=180s

echo "Get an API key from  RAWG here from https://rawg.io/apidocs and paste it in the next prompt"

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

echo "Waiting for ingress IP..."

for i in {1..40}; do
  INGRESS_IP=$(kubectl get ingress videogames-ingress -o jsonpath='{.status.loadBalancer.ingress[0].ip}' 2>/dev/null || true)
  if [[ -n "${INGRESS_IP:-}" ]]; then
    break
  fi
  echo "Ingress not ready yet..."
  sleep 5
done

[[ -z "${INGRESS_IP:-}" ]] && error "Ingress IP not assigned"

echo "Ingress IP: $INGRESS_IP"

HOST="videogames.local"

if grep -q "$HOST" "$HOSTS_FILE"; then
  echo "$HOST already exists in hosts file"
else
  echo "Adding $HOST → $INGRESS_IP to hosts file"
  echo "$INGRESS_IP $HOST" | $SUDO tee -a "$HOSTS_FILE" >/dev/null || \
  echo "Could not modify hosts file automatically. Add manually:"
  echo "$INGRESS_IP $HOST"
fi

echo ""
echo "Application available at:"
echo "http://$HOST"