# Rockstar RolePLay Launcher

Single-server SA-MP mobile launcher for GTA: San Andreas 2.10.

Launcher developed by T.meYeBiYar.

I've refactored this client to fix a lot of the persistent crashes, memory leaks, and multiplayer bugs present in older mobile sources, turning it into a much more stable base for further development.

📌 What's Fixed & Improved
Enhanced Stability: Patched core crash vectors and fixed network/gameplay bugs from previous builds.

Multi-Arch Support: Full compatibility with both ARMv7 (32-bit) and ARMv8 (64-bit) devices.

Refactored Base: Optimized for smoother performance and easier ongoing maintenance.

⚠️ Note: Single-player mode is intentionally disabled. This build is strictly configured for SA-MP multiplayer connections.

🛠️ Environment & Prerequisites
Android target: API 36 (Android 16)
Android SDK Platform: 36
Android ABIs: ARMv7 (32-bit) and ARMv8 (64-bit)

Android NDK: 28.0.13004108
Android CMake: required by `app/src/main/cpp/CMakeLists.txt`

JDK: 21

Firebase reporting is optional. Add `app/google-services.json` locally (or set the `GOOGLE_SERVICES_JSON` Actions secret) to enable it; the launcher can build without it.

Game Cache: Requires GTA: SA v2.10 data files.

Server: 5.57.37.215:7777

Key configuration files:

- `app/src/main/java/com/russia/launcher/config/Config.java` — server address and public game-data path.
- `app/src/main/java/com/russia/launcher/ui/activity/MainActivity.kt` — checks game data and launches the server.
- `app/src/main/java/com/wardrumstudios/utils/WarMedia.java` — passes the game-data path to the native engine.

🚀 Setup & Build Instructions
The launcher connects directly to the configured server. Change `SERVER_IP` and `SERVER_PORT` in `app/src/main/java/com/russia/launcher/config/Config.java` to update its address.

Install the Android SDK Platform 36, NDK 28.0.13004108, and CMake in Android Studio's SDK Manager, then build the release-flavor debug APK with:

```powershell
.\gradlew.bat :app:assembleVer_releaseDebug
```

For GitHub Actions, run **Build APK** from the Actions tab. Firebase reporting is enabled when the `GOOGLE_SERVICES_JSON` repository secret is set; it is optional. The APK is uploaded as a workflow artifact.

Make sure the target device has the GTA: SA 2.10 game cache installed, then run the client.

The existing game-file download and update URLs still point to the original project services. Replace those with your own asset and manifest URLs before relying on clean installs or updates.