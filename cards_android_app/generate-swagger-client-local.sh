#!/usr/bin/env bash


CACHE_DIR=../../.cache
SWAGGER_CODEGEN_URL=https://repo1.maven.org/maven2/io/swagger/swagger-codegen-cli/2.4.12/swagger-codegen-cli-2.4.12.jar
SWAGGER_URL=http://localhost:8080/swagger?

mkdir -p $CACHE_DIR

if [ ! -f $CACHE_DIR/swagger-codegen-cli.jar ]
then
    curl -C - https://repo1.maven.org/maven2/io/swagger/swagger-codegen-cli/2.4.12/swagger-codegen-cli-2.4.12.jar --output $CACHE_DIR/swagger-codegen-cli.jar 
fi

rm -rf client

java -jar $CACHE_DIR/swagger-codegen-cli.jar generate -i $SWAGGER_URL -l typescript-fetch -o typescript-client-old

cd typescript-client-old || exit

sed -i -E "s/protected configuration: Configuration;/protected configuration?: Configuration;/g" api.ts
sed -i -E "s/name: \"RequiredError\"/name= \"RequiredError\"/g" api.ts
rm -rf api.ts-E
rm git_push.sh
