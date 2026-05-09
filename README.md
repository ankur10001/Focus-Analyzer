# Focus Analyzer

A modern Android productivity application that helps users track focus sessions, monitor productivity habits, and analyze work patterns using local data storage and analytics.

---

# 📱 Overview

Focus Analyzer is designed to help students and professionals improve deep work habits. The app allows users to start focus sessions, categorize tasks, store session history locally, and visualize productivity insights.

The project follows a clean layered architecture using Android development best practices.

---

# ✨ Features

## ⏱️ Focus Timer

* Start and stop focus sessions
* Track duration of deep work
* Session-based productivity tracking

## 📂 Session Categories

* Coding
* Study
* Deep Work
* Custom productivity sessions

## 📜 Session History

* Stores completed sessions locally
* Displays previous work records
* RecyclerView-based session list

## 📊 Productivity Insights

* Productivity score calculation
* Weekly focus analysis
* Graphical visualization using charts
* Focus trends tracking

## 💾 Local Data Storage

* Room Database integration
* Offline functionality
* Persistent session tracking

## 🎨 Modern UI

* Dark theme inspired interface
* Bottom navigation system
* Smooth and clean layout design

---

# 🏗️ Architecture

The application follows a layered architecture:

```text
UI Layer (Activities + XML)
        ↓
Repository Layer
        ↓
DAO Layer
        ↓
Room Database
```

---

# 🧠 Tech Stack

* Java
* Android Studio
* Room Database
* RecyclerView
* Material Design Components
* Custom Views
* XML Layouts

---

# 📂 Project Structure

```text
FocusAnalyzer/
│
├── data/local/
│   ├── SessionEntity
│   ├── SessionDao
│   └── FocusDatabase
│
├── repository/
│   └── SessionRepository
│
├── ui/
│   ├── timer/
│   ├── history/
│   └── insights/
│
├── res/
│   ├── layout/
│   ├── drawable/
│   ├── values/
│   └── menu/
│
└── MainActivity
```

---

# 🔄 Working Flow

1. User starts a focus session
2. Timer records session duration
3. Session data is stored locally using Room Database
4. History screen retrieves stored sessions
5. Insights screen analyzes session data and displays productivity analytics

---

# 📸 Screenshots

Create a folder named:

```text
screenshots/
```

Add images like:

```text
screenshots/timer.png
screenshots/history.png
screenshots/insights.png
```

Then use:

```md
## Timer Screen
![Timer](screenshots/timer.png)

## History Screen
![History](screenshots/history.png)

## Insights Screen
![Insights](screenshots/insights.png)
```

---

# 🚀 Installation

1. Clone repository

```bash
git clone https://github.com/ankur10001/Focus-Analyzer.git
```

2. Open project in Android Studio

3. Sync Gradle

4. Run application on emulator or Android device

---

# 🔮 Future Improvements

* Pomodoro mode
* Cloud synchronization
* AI-based productivity insights
* Streak system
* Notification reminders
* Focus interruption tracking

---

# 👨‍💻 Developer

Ankur

GitHub:
[https://github.com/ankur10001](https://github.com/ankur10001)

---

Developed an Android productivity tracking application using Java and Room Database with focus session analytics, local persistence, RecyclerView-based UI, and productivity insights.
