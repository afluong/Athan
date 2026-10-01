# 🕌 Athan — Prayer Times & Location App

A modern, light, and robust Android application providing accurate Islamic prayer times based on user location.

Built from the ground up using **Clean Architecture**, **MVI (Model-View-Intent)** pattern, **Jetpack Compose (M3)**, and designed to be fully **KMP-ready** (Kotlin Multiplatform).

---
## ✨ Features

- **Dynamic Location Search:** Search and select any city globally with real-time location autocompletion.
- **Accurate Prayer Schedules:** Displays daily prayer times (Fajr, Dhuhr, Asr, Maghrib, Isha) adjusted to the selected location and date.
- **Past & Upcoming Prayer Tracking:** Visual cues and status indicators for upcoming and passed prayers.
- **Date Picker Navigation:** Easily consult prayer schedules for past or future dates.
- **First-Launch Onboarding:** Seamlessly guides users to set their location on first launch before accessing the schedule.

---

## 🛠 Tech Stack & KMP Readiness

The app is built using **100% Kotlin-first libraries** to ensure seamless future migration to **Kotlin Multiplatform (iOS & Desktop)** without rewriting core business logic.

- **UI & Design:** Jetpack Compose (Material 3), Custom Design System (`core:designsystem`)
- **Architecture:** Clean Architecture + MVI (State, Intent, SideEffect)
- **Dependency Injection:** [Koin](https://insert-koin.io/) (KMP ready)
- **Networking:** [Ktor Client](https://ktor.io/) (KMP ready)
- **Local Database:** [Room](https://developer.android.com/training/data-storage/room) (KMP ready)
- **Date & Time Handling:** [`kotlinx-datetime`](https://github.com/Kotlin/kotlinx-datetime) *(Strictly avoiding `java.time.*` for full cross-platform compatibility)*
- **Asynchronous & Reactive:** Kotlin Coroutines & Flow (`StateFlow`, `Channel` for one-shot events)

---

## 🏗 Architecture Overview

The project adopts a modular-ready **Clean Architecture** structure separated into `:core`, `:feature`, and `:main` layers to enforce strict boundary separation and unidirectional data flow (UDF).

```text
├── main/                   # App Shell, MainActivity, Global Navigation & Koin Setup
├── core/                   # Shared Infrastructure & Foundations
│   ├── database/           # Local persistence (Room Database, DAOs, Entities)
│   ├── designsystem/       # M3 Theme, Color palette, Typography & Atomic Composables
│   ├── model/              # Pure Kotlin Domain Models (Location, PrayerTime)
│   ├── network/            # Ktor HTTP Engine configuration
│   └── util/               # Shared Extensions (Date formatters, helpers)
│
└── feature/                # Isolated Business Features
    ├── location/           # City Search, Geocoding API & Preference Storage
    └── prayersschedule/    # Prayer Calculation Logic, Date Management & Schedules UI