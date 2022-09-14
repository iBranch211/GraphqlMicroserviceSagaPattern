#!/bin/sh

alias vault="docker exec -i $(docker ps|awk '/vault:latest/ {print $1}') vault"

while ! nc -z localhost 8200;
do
  echo sleeping;
  sleep 1;
done;
echo "Vault Connected"

sleep 20

vault operator init |awk -F: '/:/ {
    if ($1 ~ /Unseal Key 1/) print "UNSEAL_KEY_1="$2
    if ($1 ~ /Unseal Key 2/) print "UNSEAL_KEY_2="$2
    if ($1 ~ /Unseal Key 3/) print "UNSEAL_KEY_3="$2
    if ($1 ~ /Unseal Key 4/) print "UNSEAL_KEY_4="$2
    if ($1 ~ /Unseal Key 5/) print "UNSEAL_KEY_5="$2
    if ($1 ~ /Initial Root Token/) print "INITIAL_ROOT_TOKEN="$2
}' \
| sed 's/ //g' \
| tee -a deploy/docker/.env

source deploy/docker/.env

# unseal the vault using 3 of the keys
vault operator unseal $UNSEAL_KEY_1
vault operator unseal $UNSEAL_KEY_2
vault operator unseal $UNSEAL_KEY_3

echo $INITIAL_ROOT_TOKEN|vault login -

vault auth enable approle
vault secrets enable -path=secret/ kv

# enable file audit to the mounted logs volume
vault audit enable file file_path=/vault/logs/audit.log
vault audit list

vault secrets enable database
vault write database/config/mongodb \
    plugin_name=mongodb-database-plugin \
    allowed_roles="auth-admin-role","order-admin-role","payment-admin-role" \
    connection_url="mongodb://{{username}}:{{password}}@mongodb:27017/admin?tls=false" \
    username="root" \
    password="g44bsljDAi5nbWjf"

vault write database/roles/auth-admin-role \
    db_name=mongodb\
    creation_statements='{ "db": "admin", "roles": [{ "role": "readWrite" }, {"role": "readWrite", "db": "auth"}] }' \
    default_ttl="1h" \
    max_ttl="24h"

vault write database/roles/order-admin-role \
    db_name=mongodb\
    creation_statements='{ "db": "admin", "roles": [{ "role": "readWrite" }, {"role": "readWrite", "db": "order"}] }' \
    default_ttl="1h" \
    max_ttl="24h"

vault write database/roles/payment-admin-role \
    db_name=mongodb\
    creation_statements='{ "db": "admin", "roles": [{ "role": "readWrite" }, {"role": "readWrite", "db": "payment"}] }' \
    default_ttl="1h" \
    max_ttl="24h"

# create the app-specific policy
vault policy write auth-service /policies/auth-service-policy.json
vault policy read auth-service

vault policy write order-service /policies/order-service-policy.json
vault policy read order-service

vault policy write payment-service /policies/payment-service-policy.json
vault policy read payment-service

vault policy write apollo-gateway /policies/apollo-gateway-policy.json
vault policy read apollo-gateway

vault policy write app /policies/app-policy.json
vault policy read app

# create the app token
vault token create -policy=app | awk '/token/ {
if ($1 == "token") print "APP_TOKEN="$2
else if ($1 == "token_accessor") print "APP_TOKEN_ACCESSOR="$2
}' \
| tee -a deploy/docker/.env

vault write auth/approle/role/myapp \
secret_id_num_uses=0 \
secret_id_ttl=0 \
token_num_uses=0 \
token_ttl=10m \
token_max_ttl=10m \
policies=app

vault write auth/approle/role/auth \
secret_id_num_uses=0 \
secret_id_ttl=0 \
token_num_uses=0 \
token_ttl=10m \
token_max_ttl=10m \
policies=auth-service

vault write auth/approle/role/order \
secret_id_num_uses=0 \
secret_id_ttl=0 \
token_num_uses=0 \
token_ttl=10m \
token_max_ttl=10m \
policies=order-service

vault write auth/approle/role/payment \
secret_id_num_uses=0 \
secret_id_ttl=0 \
token_num_uses=0 \
token_ttl=10m \
token_max_ttl=10m \
policies=payment-service

vault write auth/approle/role/apollo-gateway \
secret_id_num_uses=0 \
secret_id_ttl=0 \
token_num_uses=0 \
token_ttl=10m \
token_max_ttl=10m \
policies=apollo-gateway

vault write auth/approle/role/app \
secret_id_num_uses=0 \
secret_id_ttl=0 \
token_num_uses=0 \
token_ttl=10m \
token_max_ttl=10m \
policies=app

vault read auth/approle/role/myapp/role-id | awk '/role_id/ {
print "MYAPP_APP_ROLE_ROLE_ID="$2
}' \
| tee -a deploy/docker/.env

vault write -f auth/approle/role/myapp/secret-id | awk '/secret_id/ {
if ($1 == "secret_id") print "MYAPP_APP_ROLE_SECRET_ID="$2
else if ($1 == "secret_id_accessor") print "MYAPP_APP_ROLE_SECRET_ID_ACCESSOR="$2
}' \
| tee -a deploy/docker/.env

vault read auth/approle/role/auth/role-id | awk '/role_id/ {
print "AUTH_APP_ROLE_ROLE_ID="$2
}' \
| tee -a deploy/docker/.env

vault write -f auth/approle/role/auth/secret-id | awk '/secret_id/ {
if ($1 == "secret_id") print "AUTH_APP_ROLE_SECRET_ID="$2
else if ($1 == "secret_id_accessor") print "AUTH_APP_ROLE_SECRET_ID_ACCESSOR="$2
}' \
| tee -a deploy/docker/.env

vault read auth/approle/role/order/role-id | awk '/role_id/ {
print "ARTICLE_APP_ROLE_ROLE_ID="$2
}' \
| tee -a deploy/docker/.env

vault write -f auth/approle/role/order/secret-id | awk '/secret_id/ {
if ($1 == "secret_id") print "ARTICLE_APP_ROLE_SECRET_ID="$2
else if ($1 == "secret_id_accessor") print "ARTICLE_APP_ROLE_SECRET_ID_ACCESSOR="$2
}' \
| tee -a deploy/docker/.env

vault read auth/approle/role/payment/role-id | awk '/role_id/ {
print "AUTHOR_APP_ROLE_ROLE_ID="$2
}' \
| tee -a deploy/docker/.env

vault write -f auth/approle/role/payment/secret-id | awk '/secret_id/ {
if ($1 == "secret_id") print "AUTHOR_APP_ROLE_SECRET_ID="$2
else if ($1 == "secret_id_accessor") print "AUTHOR_APP_ROLE_SECRET_ID_ACCESSOR="$2
}' \
| tee -a deploy/docker/.env

vault read auth/approle/role/apollo-gateway/role-id | awk '/role_id/ {
print "APOLLO_GATEWAY_APP_ROLE_ROLE_ID="$2
}' \
| tee -a deploy/docker/.env

vault write -f auth/approle/role/apollo-gateway/secret-id | awk '/secret_id/ {
if ($1 == "secret_id") print "APOLLO_GATEWAY_APP_ROLE_SECRET_ID="$2
else if ($1 == "secret_id_accessor") print "APOLLO_GATEWAY_APP_ROLE_SECRET_ID_ACCESSOR="$2
}' \
| tee -a deploy/docker/.env

# source the env file to get the new key vars
source deploy/docker/.env

# create initial secrets
vault kv put secret/application/prod SPRING_SECURITY_OAUTH2_RESOURCE-SERVER_JWT_ISSUER-URI=http://auth-service:9000 SPRING_CLOUD_CONSUL_HOST=consul SPRING_CLOUD_CONSUL_PORT=8500 SPRING_CLOUD_CONSUL_DISCOVERY_ACL_TOKEN=${CONSUL_ACL_TOKEN} SPRING_DATA_MONGODB_HOST=mongodb SPRING_DATA_MONGODB_PORT=27017 LOGSTASH_HOST=logstash LOGSTASH_PORT=5000 SPRING_CLOUD_STREAM_KAFKA_BINDER_BROKERS=kafka SPRING_CLOUD_STREAM_KAFKA_BINDER_DEFAULT_BROKER_PORT=9092
vault kv put secret/order-service/prod PORT=8081 OAUTH2_AUTHOR-CLIENT_CLIENT_ID=payment-client OAUTH2_AUTHOR-CLIENT_CLIENT_SECRET=123456
vault kv put secret/payment-service/prod PORT=8082
vault kv put secret/apollo-gateway/production PORT=4000 CORS_ALLOWED_ORIGINS='http://localhost:3000, http://127.0.0.1:3000, https://studio.apollographql.com' CONSUL_ENABLED='true' CONSUL_HOST=consul CONSUL_PORT=8500 CONSUL_ACL_TOKEN=${CONSUL_ACL_TOKEN} APOLLO_SCHEMA_CONFIG_EMBEDDED='true' SUPERGRAPH_PATH='/etc/config/supergraph.graphql' APOLLO_KEY=service:GqlMicroServiceGraph:RyxWPbKzOUBVjr75J2ln9A APOLLO_GRAPH_REF=GqlMicroServiceGraph@current