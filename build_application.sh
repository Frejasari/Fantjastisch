cd cards_android_app
./gradlew assembleDebug
mkdir ../SmartStack
cp app/build/outputs/apk/debug/app-debug.apk ../SmartStack/cards.apk

cd ../cards_backend

cp -r data ../SmartStack

mvn clean package
cp target/cards_backend-1.0.0.jar ../SmartStack