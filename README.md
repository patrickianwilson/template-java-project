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
SERVICE_NAME=template-service
ENV=dev
PROJECT=inquest-$SERVICE_NAME-$ENV
SERV_ACCNT_NAME="cassius-service-account"

gcloud projects create $PROJECT

gcloud beta iam service-accounts create $SERV_ACCNT_NAME \
    --display-name "Generated service account for Cassius" \
    --project $PROJECT
    
gcloud projects add-iam-policy-binding $PROJECT \
  --member serviceAccount:$SERV_ACCNT_NAME@$PROJECT.iam.gserviceaccount.com \
  --role roles/editor
    
gcloud iam service-accounts keys create ~/key.json \
  --iam-account $SERV_ACCNT_NAME@$PROJECT.iam.gserviceaccount.com \
  --project $PROJECT
```

Note the location of the key (for me it is ~/key.json)

```bash
cassius secret create --secretName $SERVICE_NAME-service-gcloud-credentials --file ~/key.json
cassius secret expose --secretName $SERVICE_NAME-service-gcloud-credentials --appId $SERVICE_NAME-1-0 --envName DEV --fsPath /opt/secrets/gcloud
```

Set the following app configuration (to override defaults)

| AppConfig Name | Description | Default |
| ----- | ---- | ---- |
| CONFIG_RABBIT_AUTH_URL | Rabbit authorization URL | http://rabbit-dev.inquestdevops.com/auth/login |
| CONFIG_RABBIT_TOKEN_URL | Rabbit token URL | http://rabbit-dev.inquestdevops.com/token |
| GOOGLE_APPLICATION_CREDENTIALS | A pointer to google service account credentials | NA - (suggest /opt/secrets/gcloud/data)

Create and expose the following service account credentials for the repository layer to use

