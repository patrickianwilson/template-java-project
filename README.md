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

Set the following app configuration (to override defaults)

| AppConfig Name | Description | Default |
| ----- | ---- | ---- |
| CONFIG_RABBIT_AUTH_URL | Rabbit authorization URL | http://rabbit-dev.inquestdevops.com/auth/login |
| CONFIG_RABBIT_TOKEN_URL | Rabbit token URL | http://rabbit-dev.inquestdevops.com/token |
| GOOGLE_APPLICATION_CREDENTIALS | A pointer to google service account credentials | NA - (suggest /opt/secrets/gcloud/data)

Create and expose the following service account credentials for the repository layer to use

```
cassius secret create --secretName module-service-gcloud-credentials --file gcloud-credentials.json 
cassius secret expose --secretName module-service-gcloud-credentials --appId ModuleService --envName PROD --fsPath /opt/secrets/gcloud
```

