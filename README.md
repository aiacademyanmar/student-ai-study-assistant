# Student AI Study Assistant — Native Android V2.0

Independent educational Android app, inspired by general student-learning workflows and the public [OpenAI 100 chats for college students](https://chatgpt.com/use-cases/students/) page. Not affiliated with or endorsed by OpenAI. Uses ChatGPT Study Mode as an **external destination**, not an embedded AI model.

## V2.0 — New Myanmar UI

- Custom app logo (robot with graduation cap and book) and hero banner
- Native Material-inspired mobile UI with rounded cards and Myanmar labels
- 100 **original Myanmar-language** chat starters: Study 50, Career 25, Life 25
- Search/filter all prompts, open prompt details and guidance
- Edit prompt before use, copy prompt to clipboard, launch official ChatGPT Study Mode in browser
- Offline browsing and private favorites stored with Android SharedPreferences
- Legacy V1 features retained: 12 lessons, quiz, flashcards, study planner; available from Profile
- Java Android SDK native project. No WebView wrapper or hidden API keys.

## Security / privacy

- No OpenAI account/password collected by this app. Authentication happens on chatgpt.com.
- Saved favorites are local device state.
- Prompts are copied locally to the clipboard before opening ChatGPT. User must paste them in ChatGPT.
- This app does NOT directly generate AI answers or access ChatGPT conversation history.
- Use of ChatGPT is subject to service availability and applicable minimum age requirements.

## Build

Run GitHub Actions workflow: `.github/workflows/build-apk.yml`. Download the `Student-AI-Study-Assistant-V2-debug` artifact, extract the APK, and install on Android 6.0+.

Source project under `app/src/main/java/asia/aaii/studentstudy/`.
