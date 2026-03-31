# StudyPlanner 📚

**StudyPlanner** is a native Android application developed as a university project to help students manage their exams, organize subjects, and track their study sessions using an interactive timer.

## ⚠️ Architecture & Security Note for Reviewers
> **Disclaimer regarding the Networking Layer:**
> This repository interfaces with a third-party academic backend that required raw SQL queries to be constructed and sent from the Android client. 
> 
> I am fully aware that sending SQL queries directly from a client application is a **major anti-pattern** and introduces critical vulnerabilities (e.g., SQL Injection). In a real-world, production environment, this networking layer (`ApiClient.kt`) would be replaced by RESTful endpoints (e.g., `POST /auth/login`) with JSON payloads, and all database interactions and security validations would be strictly handled on the backend side.
> 
> This networking layer is kept intact to reflect the constraints of the original academic assignment.

## ✨ Features
* **Academic Profile Management:** Register, login, and track your university career (degree course, student ID).
* **Exams & Subjects Organization:** Add and categorize exams, study topics, and learning materials.
* **Study Session Tracker:** A real-time timer/countdown tool to help you stay focused during study sessions.
* **Calendar & Dashboard:** An interactive calendar to schedule study sessions, alongside a statistics dashboard to track your academic progress.
* **Cloud Synchronization:** Communicates with a remote server via REST API (Retrofit) to sync your sessions and materials.

## 🛠️ Tech Stack
* **Language:** Kotlin
* **UI:** Android XML Layouts with ViewBinding
* **Networking:** Retrofit 2 & Gson
* **Image Loading:** Glide
* **Other Libraries:** CountdownView (for the study timer)

## 📸 Screenshots
*(Add screenshots or GIFs of the app here to showcase the UI!)*

|<img src="placeholder1.png" width="250"/>|<img src="placeholder2.png" width="250"/>|<img src="placeholder3.png" width="250"/>|
|:---:|:---:|:---:|
| Login Screen | Dashboard | Study Timer |

## 🚀 How to Run Locally
1. Clone the repository: `git clone https://github.com/yourusername/StudyPlanner.git`
2. Open the project in **Android Studio**.
3. **Important:** The app currently points to `http://10.0.2.2:8000/webmobile/` in `ApiClient.kt`, which implies the backend must be running on your local machine using an Android emulator. If you are testing this on a physical device, you will need to update this IP address to match your local network IP.
4. Sync Gradle and run the app.

## ⚖️ License
This project is open-source and available under the [MIT License](LICENSE).
