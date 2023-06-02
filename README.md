# %%{{ServiceModuleName}}%% Integ Tests
This module contains some integration tests for the %%{{ServiceModuleName}}%% .  Before these tests can be run there is some setup required

### Local Setup



### DEV environment setup

```bash

#create an integ test user account
INTEG_ACCOUNT_ID="%%{{ServiceModuleName.lowerCase}}%%-integ-account"
mercury client create --clientId INTEG_ACCOUNT_ID --grant "client_credentials" --path "http://nowhere"

#give this integ test account full admin permissions
cardsharp policy create --profile inquest --name "%%{{ServiceModuleName}}%%IntegTestPolicy" --description "Authorization for the integ test user to run CardSharp Integ Tests" --permission "%%{{ServiceModuleName}}%%.*" --resource "*" --account "INTEG_ACCOUNT_ID"
```

note the client secret that is created and add it to your build environment (Jenkins/Teamcity  configuration) with the
environment key `%%{{ServiceModuleName.upperCase}}%%_INTEG_SERV_ACCOUNT_SECRET` (you can do this from the Jenkins UI)