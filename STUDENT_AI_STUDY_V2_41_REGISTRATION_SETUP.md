# Student AI Study V2.41 — Registration-only integration

This branch modifies the ORIGINAL Student AI Study V2.40 Java Android project, not the Panyar Home APK.

## Android changes
- The original OPEN APP button leads to a native MOOC-style account/activation screen.
- Student ID/password and AAII activation-code login call the separate Student AI V1.9.1 cloud API.
- Device binding, license expiry and revocation checks are done server-side.
- Accounts are administered in Student AI cloud. MOOC course content is not imported.
- The original 20-screen Study Assistant UI, lessons, quizzes, planning tools and illustrations remain available after login.
- Account management is linked from the study settings page.

## Important before building or deploying
1. Deploy and test Student AI V1.9.1 server and SQL schema in a private staging environment; do not overwrite existing production data.
2. Set the HTTPS API URL in the login screen. The Gradle default is only a placeholder and must be checked against the actual deployment.
3. Use the existing GitHub Actions **Build Native Android APK** workflow and select this branch with **Run workflow**. It is present on the default branch and supports manual dispatch.
4. The resulting APK is an unverified build until Android compilation and real account/device tests pass.
5. Take a database backup before importing existing MOOC registration data; this is an opt-in import, NOT a live shared user database.
6. Confirm the deployed API uses the matching student-registration contract before relying on paid license access.
7. Existing installed APK updates require compatible Android signing keys. Debug APK is for testing only.

This branch contains source changes, **not a successfully built APK**.
