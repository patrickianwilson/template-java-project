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

## To Deploy
This template requires a Google Service Account and project:

```bash
SERVICE_NAME=%%{{ModuleName.lowerCase}}%%
ENV=dev
PROJECT=inquest-$SERVICE_NAME-$ENV
SERV_ACCNT_NAME="cassius-service-account"
DEV_INGRESS_IP=127.0.0.1  #change me
DNS_ZONE_NAME='inquest-devops'  #change me
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
gcloud dns --project=inlaid-citron-94802 record-sets create %%{{ModuleName.lowerCase}}%%-dev.inquestdevops.com.\
  --zone="${DNS_ZONE_NAME}"\
  --type="A" --ttl="300" --rrdatas="${DEV_INGRESS_IP}"

ENV=prod
PROJECT=inquest-$SERVICE_NAME-$ENV
PROD_INGRESS_IP=127.0.0.1  #change me
DNS_ZONE_NAME='inquest-devops'  #change me
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
gcloud dns --project=inlaid-citron-94802 record-sets create %%{{ModuleName.lowerCase}}%%-dev.inquestdevops.com.\
  --zone="${DNS_ZONE_NAME}"\
  --type="A" --ttl="300" --rrdatas="${PROD_INGRESS_IP}"
```

Note the location of the key (for me it is ~/key.json)

```bash
cassius secret create --secretName %%{{ModuleName.lowerCase}}%%-dev-gcloud-credentials --file ~/dev-key.json
cassius secret create --secretName %%{{ModuleName.lowerCase}}%%-prod-gcloud-credentials --file ~/prod-key.json

```

## Setup Authentication and Authorization for new service

###create the service account.
```bash
mercury client create --clientId %%{{ModuleName.lowerCase}}%%-dev-svc --grant client_credentials --path http://not-needed.com
mercury client create --clientId %%{{ModuleName.lowerCase}}%%-dev-svc --grant client_credentials --path http://not-needed.com
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

### grant permissions needed by your service:

**Eg**: (the following is not actually needed to run a cardinal service)

```
cardsharp policy create --name "inquest-dev-rabbit-svc-list-perms-policy" \
--description "A policy granting the rabbit service access to list permissions for an account" \
--permission "CardSharp.ListPermissionsForAccount" \
--resource "*" \
--account "inquest-dev-rabbit-svc"

```

Finally, you probably want to register a user account for testing

```
mercury account register --display-name TestUser --email test@inquestdevops.com --fname Test --lname User --password insecure

```

