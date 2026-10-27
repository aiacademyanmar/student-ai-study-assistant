# Student AI Study Assistant — Native Android V1.0

ကျောင်းသား/ကျောင်းသူများအတွက် Native Android Study Assistant.

## ✅ Features
- 6 subjects (မြန်မာစာ, English, Math, Science, History, ICT)
- 12 offline lesson cards with examples
- 12 quiz questions with feedback, scoring and saved best score
- 12 interactive flashcards
- Offline study planner saved privately on device
- Study Teacher: compose a prompt and copy it to clipboard, then **open official ChatGPT Study Mode** in app/browser
- Burmese/English interface; Android phone portrait-friendly scroll UI

## Important transparency
- This is an independent educational companion, **not the official ChatGPT app**.
- AI generation is **not embedded** in the APK. The external Study Mode page needs a network connection and the user's own ChatGPT sign-in; the generated prompt must be pasted there.
- No API key, user password, or login information is stored by this Android app.
- The shared Google link was not accessible from the project generation environment; features are built from the general student-study request and official OpenAI Study Mode documentation, not a claimed exact reproduction of that page.
- The app and its included lessons are an educational starter; content should be reviewed by teachers before broad distribution.

## Build in GitHub Actions (recommended)
1. Create a **new** GitHub repository, for example `student-ai-study-assistant`.
2. Upload the **contents** of this folder to repository root, including `.github/workflows/build-apk.yml`.
3. In the repository open **Actions → Build Native Android APK → Run workflow**. `push` on `main`/`master` also triggers a build.
4. When run succeeds, download **Student-AI-Study-Assistant-V1-debug** under Artifacts.
5. Unzip the artifact to get `app-debug.apk`; install it on your Android phone, allowing installation from trusted sources when prompted.

## Build with Android Studio
- Install Android Studio with JDK 17 and Android SDK Platform 35 / Build Tools.
- Open this folder as a Gradle project, let the dependencies sync.
- Select Build → Build Bundle(s)/APK(s) → Build APK(s).
- Debug APK is output to `app/build/outputs/apk/debug/app-debug.apk`.
- CLI with Gradle 8.10.2: `gradle :app:assembleDebug`.

## Package
- Application ID: `asia.aaii.studentstudy`
- `minSdk` 23 (Android 6), `targetSdk` 35 (Android 15)
- Java 17, Android Gradle Plugin 8.7.3, Gradle 8.10.2
- No third-party Android libraries or INTERNET permission needed (browser handles ChatGPT link).

## Structure
- `app/src/main/java/asia/aaii/studentstudy/MainActivity.java`: all native UI, lessons, quiz, planner and external AI link
- `app/src/main/AndroidManifest.xml`: launcher activity metadata
- `app/src/main/res/mipmap-*/ic_launcher.png`: APK icon
- `.github/workflows/build-apk.yml`: GitHub Actions build

## Safety
Offline planner data is stored with Android SharedPreferences locally; removing app may erase it. This app is a study aid, not a substitute for teacher guidance. AI responses can be inaccurate.
