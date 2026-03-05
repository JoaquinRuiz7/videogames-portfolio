docker compose build

echo "Installing nginx ingress controller..."

kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/main/deploy/static/provider/cloud/deploy.yaml

echo "Waiting for ingress controller to be ready..."
kubectl wait --namespace ingress-nginx \
  --for=condition=ready pod \
  --selector=app.kubernetes.io/component=controller \
  --timeout=120s

echo "Fetch your RAWG api key from https://rawg.io/apidocs"

while [ -z "$RAWG_API_KEY" ]; do
  read -p "Enter your RAWG API key: " RAWG_API_KEY
done

kubectl create secret generic videogames-backend-service-secrets \
  --from-literal=RAWG_API_KEY="$RAWG_API_KEY" \
  --dry-run=client -o yaml | kubectl apply -f -

#Create backend pods
kubectl apply -f ./videogames-backend/kubernates/deploy/manifest.yaml
#Create backend services
kubectl apply -f ./videogames-backend/kubernates/services/manifest.yaml

#Create frontend config map
kubectl apply -f ./videogames-frontend/kubernates/config-map/manifest.yaml

#Create frontend pods
kubectl apply -f ./videogames-frontend/kubernates/deploy/manifest.yaml
#Create frontend service
kubectl apply -f ./videogames-frontend/kubernates/service/manifest.yaml
#Create frontend ingress
kubectl apply -f ./videogames-frontend/kubernates/ingress/manifest.yaml

echo "Waiting for ingress IP..."

INGRESS_IP=$(kubectl get ingress videogames-ingress -o jsonpath='{.status.loadBalancer.ingress[0].ip}')

echo "Ingress IP: $INGRESS_IP"

HOST="videogames.local"

if grep -q "$HOST" /etc/hosts; then
  echo "$HOST already exists in /etc/hosts"
else
  echo "Adding $HOST → $INGRESS_IP to /etc/hosts"
  echo "$INGRESS_IP $HOST" | sudo tee -a /etc/hosts > /dev/null
fi

echo ""
echo "Application available at:"
echo "http://$HOST"



