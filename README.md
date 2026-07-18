<div align="center">
  <img src="docs/assets/logo.png" width="128" height="128" alt="DaemonDroid Logo">
  
  # DaemonDroid
  
  **The Universal Bootable USB & SD Card Maker for Android**
  
  [![Build Status](https://github.com/yourusername/DaemonDroid/actions/workflows/build.yml/badge.svg)](https://github.com/yourusername/DaemonDroid/actions)
  [![License](https://img.shields.io/badge/License-GPLv3-blue.svg)](LICENSE)
  [![Min SDK](https://img.shields.io/badge/Min%20SDK-29-green.svg)](https://android-arsenal.com/api?level=29)
</div>

DaemonDroid is a powerful, modern, open-source Android application that turns your Android device into a complete toolkit for creating bootable USB drives and SD cards. With support for standard Linux ISO flashing, Windows WIM splitting, and a custom port of Ventoy, it bridges the gap between PC-grade flashing tools (like Rufus or BalenaEtcher) and mobile devices.

---

## 📸 Screenshots

| Dashboard | Privilege Wizard | Operation Log (Terminal) |
| :---: | :---: | :---: |
| <img src="docs/assets/screenshot_dashboard.png" width="250" /> | <img src="docs/assets/screenshot_wizard.png" width="250" /> | <img src="docs/assets/screenshot_terminal.png" width="250" /> |

---

## ✨ Features

- **Standard ISO Flashing**: Write standard Linux, Raspberry Pi, and other bootable images to USB/SD cards directly over USB OTG.
- **Windows USB Creation**: Advanced Windows ISO extraction supporting automatic `install.wim` splitting for FAT32 compatibility.
- **Ventoy Integration**: The app features a custom wrapper around the official `Ventoy2Disk.sh` to install Ventoy on your USB drives directly from Android. Includes theme support (Vimix, Sleek, Tela).
- **Partition Management**: Built-in visual partition manager to inspect, format, and adjust partitions on connected block devices.
- **Privilege Abstraction Engine**: Works smoothly on both **Rooted (libsu)** and **Non-Rooted (Shizuku)** environments.
- **Beautiful UI**: Written in pure Kotlin and Jetpack Compose utilizing a custom "Hybrid Dark" design system with a responsive terminal-style execution log.

## 🛠 Tech Stack

DaemonDroid is built entirely with modern Android technologies:
- **Language**: Kotlin 2.x
- **UI Toolkit**: Jetpack Compose (Material 3)
- **Navigation**: Jetpack Navigation Compose (Type-safe routing)
- **Architecture**: MVI/MVVM, Single Activity
- **Concurrency**: Kotlin Coroutines
- **Dependency Injection**: Hilt
- **Root/Privilege Abstraction**: Shizuku API + libsu
- **Local Database**: Room (For persisting operation logs)

---

## 🏗️ Building from Source

This project uses modern Gradle configurations and AGP 9.0+. 

### Requirements
- Android Studio Ladybug (or newer)
- JDK 21
- Android SDK Platform 36

### Instructions
1. Clone the repository:
```bash
git clone https://github.com/yourusername/DaemonDroid.git
cd DaemonDroid
```

2. Build the project using Gradle:
```bash
./gradlew assembleDebug
```

3. The output APK will be located at `app/build/outputs/apk/debug/app-debug.apk`.

## 📚 Documentation

For a deeper dive into how DaemonDroid's Privilege Engine, WIM splitting, and Ventoy wrapping work under the hood, check out the [Architecture Documentation](docs/ARCHITECTURE.md).

## 🤝 Contributing

Contributions are always welcome! Feel free to open an issue or submit a Pull Request if you'd like to add new features or fix bugs.

## 📄 License

DaemonDroid is licensed under the [GPLv3 License](LICENSE). 
*Note: This project wraps open-source utilities like Ventoy. See their respective licenses where applicable.*
