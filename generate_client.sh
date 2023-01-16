openapi-generator generate -i http://localhost:8080/v3/api-docs -g kotlin --library jvm-retrofit2 -o client --model-name-suffix "Entity"

cp -r client/src/main/kotlin/org cards_android_app/app/src/main/java
rm -rf client