# Modern Android MVVM App

This is a complete Android application built to demonstrate modern development practices. The app allows users to view a paginated list of users, add new users with full offline support, navigate to a list of trending movies from The Movie Database (TMDB), and view movie details.

## Key Features

*   **User List:** Displays a paginated list of users fetched from `reqres.in` API with infinite scrolling.
*   **Add User with Offline Sync:**
    *   A form to add a new user with a name and job.
    *   If the device is **online**, the new user is created via a POST request to the server.
    *   If the device is **offline**, the user data is saved locally to a Room database.
    *   **WorkManager** automatically syncs any locally saved users to the server once network connectivity is restored.
*   **Movie List:** On user selection, navigates to a screen displaying a paginated list of trending movies from the TMDB API, complete with posters, titles, and release dates.
*   **Movie Detail:** Clicking a movie opens a dedicated detail screen showing a larger poster, title, release date, and a full overview.

## Tech Stack & Architecture

*   **Language:** Kotlin
*   **Architecture:** Model-View-ViewModel (MVVM)
*   **Asynchronous:** Kotlin Coroutines & Flow
*   **Dependency Injection:** Dagger Hilt (using KSP for performance)
*   **Networking:** Retrofit & OkHttp
*   **Database:** Room for local persistence.
*   **Pagination:** Paging 3 Library.
*   **Offline Sync:** WorkManager.
*   **Navigation:** Android Jetpack Navigation Component.
*   **Image Loading:** Coil.

## Assumptions

*   A valid TMDB API key is required to fetch movie data. The project is pre-configured to accept this key.
*   The `reqres.in` API is assumed to be available and responsive.
*   For the offline sync to work, the app assumes it will eventually have network access to complete the background task.

## How to Run the Project

1.  **Clone the Repository:**
    ```bash
    git clone <your-repository-url>
    ```

2.  **Add Your TMDB API Key:**
    *   Open the project in Android Studio.
    *   Navigate to the file: `app/build.gradle.kts`.
    *   Find the following line inside the `buildTypes { debug { ... } }` block:
      ```kotlin
      buildConfigField("String", "TMDB_API_KEY", "\"20300c81c79757d53e60c5546424a923\"")
      ```
    *   Replace the key with your own valid TMDB API key if necessary.

3.  **Build and Run:**
    *   Let Android Studio sync the Gradle files.
    *   Click the "Run" button to build and install the app on an emulator or a physical device (API 24+).

---
## Final APK Generation
To generate a releasable APK for submission:
1. In Android Studio, go to **Build -> Generate Signed Bundle / APK...**
2. Select **APK** and click "Next".
3. Follow the on-screen instructions to create a new keystore or use an existing one to sign the application.
4. The signed APK will be generated in the `app/release` directory.