<div align="center">

<img src="desktopApp/src/desktopMain/resources/logo.png" alt="JoyMusic Logo" width="160" height="160" style="border-radius: 32px;" />

# JoyMusic

### An elegant, ad-free Material You YouTube Music client for Android, Windows, Linux, macOS, and iOS.

[![GitHub Release](https://img.shields.io/github/v/release/jehadjoy15-stack/JOY-MUSIC?style=for-the-badge&logo=github&labelColor=0d1117&color=6366f1)](https://github.com/jehadjoy15-stack/JOY-MUSIC/releases/latest)
[![License](https://img.shields.io/badge/License-GPL--3.0-blue?style=for-the-badge&logo=opensourceinitiative&labelColor=0d1117)](LICENSE)
[![Website](https://img.shields.io/badge/Website-joymusic.site-00c853?style=for-the-badge&logo=googlechrome&logoColor=white&labelColor=0d1117)](https://joymusic.site/)
[![Platform Support](https://img.shields.io/badge/Platforms-Android%20%7C%20Windows%20%7C%20Linux%20%7C%20macOS%20%7C%20iOS-orange?style=for-the-badge&labelColor=0d1117)](#downloads)

</div>

<br>

> [!NOTE]
> **JoyMusic** is a lightweight, cross-platform music streaming client designed to give you a pure, uninterrupted listening experience with modern Material 3 Expressive UI and native desktop/mobile integration.

---

## ✨ Features

- 🎵 **Ad-Free Music Streaming:** Full YouTube Music catalog playback without any ads or interruptions.
- 📱 **Multiplatform Experience:** Seamless support across Android (7+), Windows (10/11), Linux (x64 AppImage), macOS (Apple Silicon & Intel DMG), and iOS (IPA).
- 🎨 **Material 3 Expressive Design:** Dynamic system colors, fluid animations, beautiful glassmorphism, and custom player themes (Dark, Dynamic, Amoled).
- 🔗 **Device Connect (P2P Remote Playback):** Control your desktop playback directly from your phone (or vice-versa) over your local Wi-Fi network with end-to-end encryption.
- 🎤 **Synchronized Lyrics:** Live, word-by-word and line-by-line scrolling lyrics with instant translation and romanization (powered by LrcLib, BetterLyrics, and Kugou).
- 🎧 **High-Fidelity Audio:** Stream audio with volume normalization, custom equalizer (AutoEQ), audio offloading, and background playback.
- 💬 **Discord Rich Presence:** Show off what you're listening to in real-time on Discord with rich album artwork and playback controls.
- 📊 **Scrobbling Support:** Native integration with **ListenBrainz** and **Last.fm** to keep your music history in sync.
- 🔍 **Music Recognition (Shazam):** Identify surrounding music instantly with built-in Shazam recognition.
- 📦 **Playlist Migration:** Import and export your favorite playlists from Spotify, YouTube Music, Apple Music, and CSV backups.
- 📡 **Casting Support:** Cast audio to Google Cast (Chromecast), DLNA, UPnP, and F-Cast devices.
- 🔒 **Privacy Focused:** Enjoy music anonymously without signing in, or log in securely to sync your private YouTube Music playlists and library.

---

<div align="center">

<h2 id="downloads">🚀 Download JoyMusic</h2>

<p>Choose the build designed for your device from our official release page.</p>

<p>
  <a href="https://github.com/jehadjoy15-stack/JOY-MUSIC/releases/latest">
    <b>👉 View Latest Release & All Downloads on GitHub</b>
  </a>
</p>

<table>
  <thead>
    <tr>
      <th align="center">Platform</th>
      <th align="center">Format</th>
      <th align="center">Compatibility</th>
      <th align="center">Download</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td align="center"><b>Windows</b></td>
      <td>Standalone Portable / EXE</td>
      <td>Windows 10, 11 (x64)</td>
      <td><a href="https://github.com/jehadjoy15-stack/JOY-MUSIC/releases/latest">Download for Windows</a></td>
    </tr>
    <tr>
      <td align="center"><b>Android</b></td>
      <td>Native APK</td>
      <td>Android 7.0 (Nougat) and higher</td>
      <td><a href="https://github.com/jehadjoy15-stack/JOY-MUSIC/releases/latest">Download JOY MUSIC.apk</a></td>
    </tr>
    <tr>
      <td align="center"><b>Linux</b></td>
      <td>Universal AppImage</td>
      <td>Ubuntu, Debian, Fedora, Arch, etc. (x86_64)</td>
      <td><a href="https://github.com/jehadjoy15-stack/JOY-MUSIC/releases/latest">Download Linux AppImage</a></td>
    </tr>
    <tr>
      <td align="center"><b>macOS</b></td>
      <td>DMG Package</td>
      <td>macOS 12.0+ (Apple Silicon & Intel)</td>
      <td><a href="https://github.com/jehadjoy15-stack/JOY-MUSIC/releases/latest">Download macOS DMG</a></td>
    </tr>
    <tr>
      <td align="center"><b>iOS</b></td>
      <td>Unsigned IPA</td>
      <td>iOS 15.0+ (AltStore / TrollStore / Sideloadly)</td>
      <td><a href="https://github.com/jehadjoy15-stack/JOY-MUSIC/releases/latest">Download iOS IPA</a></td>
    </tr>
  </tbody>
</table>

</div>

---

## 🛠️ Building from Source

### Prerequisites
- **JDK 21 or JDK 25** (OpenJDK / Eclipse Temurin recommended)
- **Android SDK** (for Android build)
- **.NET 10 SDK** (for Windows WebView2 login helper, optional)
- **Xcode** (for macOS & iOS builds)

### 1. Clone the Repository
```bash
git clone https://github.com/jehadjoy15-stack/JOY-MUSIC.git
cd JOY-MUSIC
```

### 2. Build for Desktop (Windows / PC)
```bash
# Windows
.\gradlew.bat :desktopApp:createDistributable
```
Output will be generated in `desktopApp/build/compose/binaries/main/app/JoyMusic/`.

### 3. Build for Linux (AppImage)
```bash
chmod +x gradlew scripts/build_appimage.sh
./scripts/build_appimage.sh
```
Output will be generated in `dist/JoyMusic.AppImage`.

### 4. Build for macOS (DMG)
```bash
./gradlew :desktopApp:packageReleaseDmg
```

### 5. Build for iOS (IPA)
```bash
chmod +x scripts/build_ios.sh
./scripts/build_ios.sh --ipa
```

### 6. Build for Android (APK)
```bash
cd android
./gradlew :app:assembleRelease
```

---

## 👨‍💻 Creator & Community

- **Creator & Maintainer:** **Jehad Joy** ([@jehadjoy15-stack](https://github.com/jehadjoy15-stack))
- **Official Website:** [https://joymusic.site/](https://joymusic.site/)
- **Repository:** [https://github.com/jehadjoy15-stack/JOY-MUSIC](https://github.com/jehadjoy15-stack/JOY-MUSIC)

---

## ⚖️ Legal Disclaimer

JoyMusic is an open-source, non-commercial client application. 
- This project is **not** affiliated with, authorized, or endorsed by Google LLC, YouTube, or any of their subsidiaries.
- All music tracks, album art, metadata, and trademarks are the property of their respective owners.
- Please support your favorite artists by purchasing their music and subscribing to official services.

---

<div align="center">

**Made with ❤️ by Jehad Joy**

**This project stands with Palestine 🇵🇸**

</div>
