# template-java-project

Flavor: Swagger SDK

###To Build

```bash
gradle release
```

###To Configure Project

Start the service (running either in production or localhost).  Update the refresh-api.sh script variables to reflect the proper endpoints

specifically:

```bash
WS_URL=http://localhost:8080
```
Additionally, you will likely need to update the properties in ```config/codegen-java-config.json``` to reflect the desired package structure.


###To Refresh API

A properly configured project can be quickly "refreshed" via

```bash
./refresh-api.sh
```