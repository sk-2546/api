# Soundbound API Module

This module provides a minimal HTTP API for the Soundbound models using Ktor.

Endpoints:
- GET /api/health - returns {"status":"ok"}
- GET /api/songs - returns a JSON list of sample songs
- GET /api/songs/{id} - returns song by id or error

Run (PowerShell, from project root):

```powershell
./gradlew :api:run
```

The server listens on http://0.0.0.0:8080 by default.

Quick test script (PowerShell)

This repo includes a small test helper that starts the server using the Gradle wrapper,
polls the health endpoint and reports success.

From the project root run:

```powershell
.\api\test\run_api_test.ps1
```

Notes:
- The first run may take longer while Gradle and dependencies download.
- If the test fails, open `build.gradle.kts` and the Gradle console output for errors. You can also run `./gradlew :api:build` to force a build.
