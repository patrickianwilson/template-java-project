#!/bin/bash

export WS_URL=http://localhost:8080
export GROUP_ID=inquestdevops.com
export SERVICE_ID=%%{{ServiceModuleName.lowerCase}}%%
export MODULE_PATH=`pwd`
mkdir -p ${MODULE_PATH}/build/tmp

if [[ ! -f ${MODULE_PATH}/build/tmp/openapi-codegen.jar ]]; then
    wget -O ${MODULE_PATH}/build/tmp/openapi-codegen.jar https://repo1.maven.org/maven2/org/openapitools/openapi-generator-cli/6.2.1/openapi-generator-cli-6.2.1.jar
fi

java -jar ${MODULE_PATH}/build/tmp/openapi-codegen.jar generate\
 --input-spec ${WS_URL}/openapi.yaml\
 --generator-name java\
 --output ${MODULE_PATH}/src/$GROUP_ID/$SERVICE_ID \
 --config ${MODULE_PATH}/config/codegen-java-config.json \
 -p enumClassPrefix=true\
 --skip-validate-spec

rm "${MODULE_PATH}/src/$GROUP_ID/$SERVICE_ID/settings.gradle"
rm "${MODULE_PATH}/src/$GROUP_ID/$SERVICE_ID/pom.xml"
rm "${MODULE_PATH}/src/$GROUP_ID/$SERVICE_ID/.travis.yml"
rm "${MODULE_PATH}/src/$GROUP_ID/$SERVICE_ID/build.sbt"
rm "${MODULE_PATH}/src/$GROUP_ID/$SERVICE_ID/gradlew*"
rm -r "${MODULE_PATH}/src/$GROUP_ID/$SERVICE_ID/gradle/"
rm "${MODULE_PATH}/src/$GROUP_ID/$SERVICE_ID/gradle.properties"
rm "${MODULE_PATH}/src/$GROUP_ID/$SERVICE_ID/git_push.sh"