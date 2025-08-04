# 📱 BeeFashion men's clothing shopping app

Android apps developed with Kotlin and Jetpack Compose follow a modern architecture that is easy to extend and maintain.

---

## ✨ Key Features

* Interface built with Jetpack Compose
* Use "Navigation Component" to navigate between screens.
* Manage state with **ViewModel** and **StateFlow**.
* Integrate **Retrofit**/**Ktor** to call REST API.
* Store data with **MongoDB**.
* Compliant with **Material Design 3 (Material You)**.

---

## 🏗️ The application is built according to the MVVM (Model-View-ViewModel) model combined with libraries from Android Jetpack:

* UI: Jetpack Compose
* State Management: ViewModel + StateFlow
* Navigation: Jetpack Navigation Compose
* Data: Repository pattern, Retrofit/Ktor
* Dependency Injection: Kotlin

---

## 🛠️ Technologies & Libraries Used
Language: Kotlin
UI Toolkit: Jetpack Compose
Navigation: Navigation Compose
DI: Hilt or Koin
Networking: Retrofit or Ktor
Database: MongoDB
Asynchronous: Kotlin Coroutines
Image Loading: Coil

---

## 🚀 Cài đặt và chạy dự án

### 1️⃣ Environment Requirements

* Android Studio **Koala+ (2024)** or later.
* JDK 17+
* Gradle 8+

### 2️⃣ Clone dự án

```bash
git clone https://github.com/tuanna24/DATN_BeeFashion.git
```

### 3️⃣ Open in Android Studio

* Open Android Studio → **Open an existing project** → select the folder you just cloned.
* Sync Gradle and run the app on the device/emulator.

---

## 📂 Cấu trúc thư mục (ví dụ)

```
app/
├── manifests/
│ └── AndroidManifest.xml # Declare application configuration, Activity, Permission...
│
├── kotlin+java/
│ └── fpl.md19.beefashion/ # Main application package
│ ├── api/ # Handle API services, Retrofit interface
│ ├── components/ # Reusable Composable UI components
│ ├── GlobalVarible/ # Global variables, constants (global variables)
│ ├── models/ # Data models (DTO, Entity, data class)
│ ├── navigation/ # Manage navigation (NavHost, NavGraph)
│ ├── requests/ # Request body or configuration for network request
│ ├── screens/ # Main UI screens of the app (Compose Screens)
│ ├── ui.theme/ # Theme (Theme.kt, Color.kt, Typography.kt)
│ ├── viewModels/ # ViewModel for each screen (MVVM)
│ ├── FirebaseMessagingService.kt # Handle FCM push notifications (Firebase Cloud Messaging)
│ └── MainActivity.kt # Main initialization activity, containing NavHost & Compose setup
│
├── fpl.md19.beefashion (androidTest)/ # Instrumented folder Tests (UI Test)
└── fpl.md19.beefashion (test)/ # Folder containing Unit Tests
```


---

## 📌Roadmap

* [ ] Add Unit Test & UI Test with **JUnit**/**Espresso**.

* [ ] Multi-language support (Localization).

* [ ] Integrated CI/CD (GitHub Actions).

---

## 🤝 Contribute

Pull requests are welcome. If you want to contribute, please:

1. Fork the project.

2. Create a new branch (`git checkout -b feature/function-name`).

3. Commit and push the code.
4. Send a Pull Request.

---

## 📜 License

The project is released under the [MIT](LICENSE) license.
---
