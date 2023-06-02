#!/bin/bash

export WS_URL=http://localhost:8080
export MODULE_PATH=`pwd`
mkdir -p ${MODULE_PATH}/build/tmp

if [[ ! -f ${MODULE_PATH}/build/tmp/openapi-codegen.jar ]]; then
    wget -O ${MODULE_PATH}/build/tmp/openapi-codegen.jar https://repo1.maven.org/maven2/org/openapitools/openapi-generator-cli/6.2.1/openapi-generator-cli-6.2.1.jar
fi

java -jar ${MODULE_PATH}/build/tmp/openapi-codegen.jar generate\
 --input-spec ${WS_URL}/openapi.yaml\
 --generator-name java\
 --output ${MODULE_PATH}/\
 --config ${MODULE_PATH}/config/codegen-java-config.json \
 -p enumClassPrefix=true\
 --skip-validate-spec

rm "${MODULE_PATH}/settings.gradle"
rm "${MODULE_PATH}/pom.xml"
rm "${MODULE_PATH}/.travis.yml"
rm "${MODULE_PATH}/build.sbt"
rm -r "${MODULE_PATH}/gradle/"
rm "${MODULE_PATH}/gradle.properties"
rm "${MODULE_PATH}/git_push.sh"