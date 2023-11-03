# template-java-project
A quickstart template for writing Cardinal web services

This template is ready to go with the following out of the box features:

1. Abstract Datasource Repositories (GCP)
2. Open API spec wired in
3. Open API Rabbit integration 

## To Build

```
gradlew release

```

## To Run
Locally the server can be run by executing the ```com.sample.cardinal.Runner``` class using the `--port` flag (defaults to `8080`)

The workdir needs to be set to the module 'BUILD' dir (${projectDir}/build) so it can pick up web assets correctly.
Finally, you will need to set the `SVC_ACCOUNT_SECRET` environment for this Run configuration to be the service account secret
for the dev service account.  This can be found by running:

```bash

mercury client update --clientId sandstormresourceservice-dev-svc

```

## To Deploy
This template requires a Google Service Account and project:

```bash
SERVICE_NAME=%%{{ModuleName.lowerCase}}%%
ENV=dev
PROJECT=inquest-$SERVICE_NAME-$ENV
SERV_ACCNT_NAME="cassius-service-account"
DEV_INGRESS_IP=127.0.0.1  # change me
DNS_ZONE_NAME='inquest-devops'  # change me
gcloud projects create $PROJECT

gcloud beta iam service-accounts create $SERV_ACCNT_NAME \
    --display-name "Generated service account for Cassius" \
    --project $PROJECT
    
gcloud projects add-iam-policy-binding $PROJECT \
  --member serviceAccount:$SERV_ACCNT_NAME@$PROJECT.iam.gserviceaccount.com \
  --role roles/editor
    
gcloud iam service-accounts keys create ~/dev-key.json \
  --iam-account $SERV_ACCNT_NAME@$PROJECT.iam.gserviceaccount.com \
  --project $PROJECT

 #add DNS
gcloud dns --project=inlaid-citron-94802 record-sets create %%{{ModuleName.lowerCase}}%%-dev.%%{{DnsZoneDevDomain}}%%.\
  --zone="${DNS_ZONE_NAME}"\
  --type="A" --ttl="300" --rrdatas="${DEV_INGRESS_IP}"

ENV=prod
PROJECT=inquest-$SERVICE_NAME-$ENV
PROD_INGRESS_IP=127.0.0.1  #change me
DNS_ZONE_NAME='inquest-devops'  #change me for PROD
gcloud projects create $PROJECT

gcloud beta iam service-accounts create $SERV_ACCNT_NAME \
    --display-name "Generated service account for Cassius" \
    --project $PROJECT
    
gcloud projects add-iam-policy-binding $PROJECT \
  --member serviceAccount:$SERV_ACCNT_NAME@$PROJECT.iam.gserviceaccount.com \
  --role roles/editor
    
gcloud iam service-accounts keys create ~/prod-key.json \
  --iam-account $SERV_ACCNT_NAME@$PROJECT.iam.gserviceaccount.com \
  --project $PROJECT
 
 #add DNS
gcloud dns --project=inlaid-citron-94802 record-sets create %%{{ModuleName.lowerCase}}%%.%%{{DnsZoneProdDomain}}%%.\
  --zone="${DNS_ZONE_NAME}"\
  --type="A" --ttl="300" --rrdatas="${PROD_INGRESS_IP}"
```

Note the location of the key (for me it is ~/key.json)

```bash
cassius secret create --secretName %%{{ModuleName.lowerCase}}%%-dev-gcloud-credentials --file ~/dev-key.json
cassius secret create --secretName %%{{ModuleName.lowerCase}}%%-prod-gcloud-credentials --file ~/prod-key.json

```

## Setup Datastore (if service needs a DB)

Open the following url in a browser
```
echo https://console.cloud.google.com/datastore/setup?project=$PROJECT
```

The enable the datastore API

```bash
gcloud services enable datastore.googleapis.com --project $PROJECT
```

## Setup Authentication and Authorization for new service

###create the service account.
```bash
mercury client create --clientId %%{{ModuleName.lowerCase}}%%-dev-svc --grant client_credentials --path http://rabbit-dev.inquestdevops.com/auth/merc/complete
mercury client create --clientId %%{{ModuleName.lowerCase}}%%-dev-svc --grant client_credentials --path http://rabbit-dev.inquestdevops.com/auth/merc/complete
```
**Note:** Take note of the generated service account secret

```bash
cassius secret create --secretName %%{{ModuleName.lowerCase}}%%-dev-service-account-secret --strContent <string from above>
cassius secret create --secretName %%{{ModuleName.lowerCase}}%%-dev-service-account-secret --strContent <string from above>

cassius secret create --secretName %%{{ModuleName.lowerCase}}%%-dev-rabbit-admin-token --strContent insecure   #dev admin tokens are not secure
cassius secret create --secretName %%{{ModuleName.lowerCase}}%%-prod-rabbit-admin-token --strContent <secure password>
```

### Create a Deployment Bundle for Cassius to Track images

```
cassius deployment-bundle create --name sandstormapplauncher-dev --type image
```

### Authorize this login redirect path in rabbit 

```bash
USER='admin'
TOKEN='admin_token'  #replace this
CLIENT_ID='%%{{ModuleName.lowerCase}}%%-dev-svc'
BASIC_AUTH=$(echo -n "$USER:$TOKEN" | base64)
RABBIT_DNS='rabbit.customerdns.com' #Change this
curl -X POST "http://$RABBIT_DNS/client" -H "accept: application/json" -H "Authorization: Basic $BASIC_AUTH" -H "Content-Type: application/json" -d "{\"clientId\":\"$CLIENT_ID\",\"allowedRedirectPaths\":[\"http://localhost:8080/auth/complete\", \"http://%%{{ModuleName.lowerCase}}%%-dev.inquestdevops.com/auth/complete\"]}"


```
### grant permissions needed by your service:

**Eg**: (the following is not actually needed to run a cardinal service)

```
cardsharp policy create --name "%%{{ModuleName.lowerCase}}%%-integ-testing-perms-policy" \
--description "A policy granting the integ testing service account access to all APIs and resources for this service" \
--permission "%%{{ModuleName}}%%.*" \
--resource "*" \
--account "%%{{ModuleName.lowerCase}}%%-dev-svc"

```

Finally, you may want to register a user account for testing (not required):

```
mercury account register --display-name TestUser --email test@inquestdevops.com --fname Test --lname User --password insecure

```

