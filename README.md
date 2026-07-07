# WedApp - Advanced Weather Dashboard

WedApp is a high-fidelity Android application that demonstrates modern architectural patterns, premium UI design, and robust data fetching capabilities. It provides a comprehensive weather experience with an immersive "presentation-level" aesthetic.

## 🚀 Core Technologies & Architecture

- **UI Framework**: Built entirely with **Jetpack Compose** using Material 3.
- **Architecture**: Implements **MVVM (Model-View-ViewModel)** with **Kotlin Coroutines and Flow** for reactive data handling and state management.
- **Networking**: Uses **Retrofit 3.0** with Gson for type-safe API communication with the OpenWeatherMap API.
- **Navigation**: Migrated to the latest **Navigation 3 (Nav3)** library, utilizing a developer-owned backstack and type-safe routes for maximum flexibility.
- **Local Assets**: Integrated high-quality 3D weather icons to create a premium visual experience.

## ✨ Key Features & Requirements Fulfillment

### 🔐 Authentication Flow
- **Registration & Sign In**: Robust, production-level authentication screens with real-time field validation (Email format & Password length).
- **Branding**: Stylized "S" logo integration and immersive splash screen using the Core Splashscreen API.
- **Secure Entry**: The app only grants access to the dashboard once valid credentials are provided.

### 🌤️ Live Weather Dashboard (Tab 1)
- **Current Location**: Dynamically fetches and displays the **City and Country** (e.g., "Quiapo District, PH").
- **Real-time Temperature**: Accurate current temperature displayed in **Celsius**.
- **Sun & Moon Logic**: 
    - Automatically displays a **Sun icon** for clear weather during the day.
    - Transitions to a **Moon icon** (Nightlight) after 6:00 PM or before 6:00 AM.
    - Shows a **Rain icon** specifically when precipitation is detected.
- **Astronomical Data**: Clearly displays the exact times for **Sunrise and Sunset**.
- **Meteorological Details**: Provides a grid of essential data: Wind Speed, Humidity, Pressure, and Chance of Rain.

### 📅 Location Management & Forecast (Tab 2)
- **Manage Location**: A sleek, immersive list view for browsing weather across multiple cities.
- **Search**: Integrated search bar for quick city lookups.
- **Detailed Cards**: Features 3D-styled city cards with high/low temperature ranges and condition summaries.

### 🛡️ Robustness & Reliability
- **Offline Fallback**: In the event of an API error (e.g., 401 Unauthorized or no connection), the app automatically displays a **Static Fallback UI** so the application remains functional.
- **Persistent Snackbar**: Notifies the user with a permanent snackbar when they are viewing offline static data.
- **Advanced Unit Testing**: Includes a full suite of unit tests for the Data Source, Repository, and ViewModel layers using **MockK**, **Turbine**, and **Coroutines Test**.

## 🎨 Visual Design
- **Color Palette**: Sophisticated dark purple theme inspired by modern high-end weather applications.
- **Typography**: Uses the **Inter** font family globally for superior readability and a clean professional look.
- **Edge-to-Edge**: Fully immersive layout with transparent system bars and white icon overrides.

### 📦 Production Ready
The application is fully optimized for production deployment. The `release` build type is configured with:
- `isMinifyEnabled = true`: Code obfuscation and size reduction via R8.
- `isShrinkResources = true`: Automatic removal of unused resources for a lightweight APK.

---
*Developed as a production-level demonstration of modern Android development.*
