# QRify - QR Code Scanner & Generator

<div align="center">

<img src="https://github.com/Surajsinghyadav/QRify/blob/master/Screenshots/Screenshot%202025-09-16%20194538.png?raw=true" alt="Scanner Interface" width="200">

**A comprehensive QR code solution for Android with advanced scanning and generation features**

[![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpack-compose&logoColor=white)](https://developer.android.com/jetpack/compose)
[![MIT License](https://img.shields.io/badge/License-MIT-green.svg?style=for-the-badge)](https://choosealicense.com/licenses/mit/)

[Download APK](https://github.com/Surajsinghyadav/QRify/raw/refs/heads/master/app/release/app-release.apk) • [Features](#features) • [Screenshots](#screenshots) • [Demo](#demo) • [Installation](#installation)

</div>

---

## 🎯 About

QRify is a modern Android application built with Jetpack Compose that combines powerful QR code scanning capabilities with a feature-rich QR generator. Designed for both personal and professional use, it offers customizable themes, persistent history, and seamless sharing options.

Whether you need to quickly scan QR codes for daily tasks or generate custom QR codes with personalized colors, QRify provides an intuitive and efficient solution.

## ✨ Features

<details>
<summary>📱 <strong>QR Code Scanner</strong></summary>

- **Real-time QR Code Scanning** using Google ML Kit's barcode scanner
- **Auto-zoom functionality** for better scanning accuracy
- **Smart module installation** - automatically installs required ML Kit modules
- **Scan result display** with scrollable content view
- **Recent scans history** with persistent storage
- **Interactive scan history** - tap any recent scan to view content
- **Multiple action support** for scanned content:
  - 📋 Copy to clipboard
  - 📤 Share via system share sheet
  - 🌐 Open URLs directly in browser (auto-detects links)

</details>

<details>
<summary>🎨 <strong>QR Code Generator</strong></summary>

- **Custom text/data input** - supports text, URLs, contact info, Wi-Fi passwords
- **Real-time QR generation** with high error correction (Level H)
- **Multiple color themes** with predefined color palette
- **Custom color picker** with hex color input validation
- **Live color preview** with circular color indicators
- **Visual feedback** for invalid/oversized content
- **Generated QR code actions**:
  - 💾 Save to device gallery
  - 📤 Share QR code as image
  - 🔍 High-resolution output (512x512 pixels)

</details>

<details>
<summary>🎭 <strong>User Interface</strong></summary>

- **Material 3 Design** with modern card-based layout
- **Segmented navigation** between Scanner and Generator modes
- **Animated UI elements** with custom video animations
- **Responsive design** with proper spacing and alignment
- **Error handling** with user-friendly messages
- **Keyboard management** with auto-hide functionality

</details>

## 📱 Screenshots

<div align="center">

| Scanner Interface | Results Interface | QR Generator | Custom Colors |
|:-----------------:|:----------------:|:------------:|:-------------:|
| <img src="https://github.com/Surajsinghyadav/QRify/blob/master/Screenshots/Screenshot%20pc2025-09-16%20192801.png?raw=true" alt="Scanner Interface" width="200"> | <img src="https://github.com/Surajsinghyadav/QRify/blob/master/Screenshots/Screenshot%202025-09-16%20193349.png?raw=true" alt="Results Interface" width="200"> | <img src="https://github.com/Surajsinghyadav/QRify/blob/master/Screenshots/Screenshot_2025-09-16-04-25-08-33_e135f6f057ae6b59c7d93a415eb432a7.jpg?raw=true" alt="QR Generator" width="200"> | <img src="https://github.com/Surajsinghyadav/QRify/blob/master/Screenshots/Screenshot_2025-09-16-04-25-32-56_e135f6f057ae6b59c7d93a415eb432a7%20-%20Copy.jpg?raw=true" alt="Custom Colors" width="200"> |
| *QR Scanner with recent history* | *Scanner with results, copy, share, open* | *Generator with color themes* | *Hex color input validation* |

</div>

## 🎥 Video Demo

<div align="center">

### 📺 Watch QRify in Action

[![QRify Demo Video](screenshots/video_thumbnail.png)](https://github.com/yourusername/QRify/releases/download/v1.0/qrify_demo.mp4)

*Click to watch a comprehensive demo showcasing all features*

**Demo Highlights:**
- 🔍 Real-time QR code scanning with ML Kit
- 🎨 Custom QR generation with color themes
- 📱 Material Design 3 interface walkthrough
- 📤 Scan history and sharing capabilities

**Alternative Links:**
- [🎬 YouTube Demo]([https://youtube.com/watch?v=YOUR_VIDEO_ID](https://youtube.com/shorts/BibqZ6AQjK8)) (if available)

</div>

## 🛠️ Technical Stack

| Component | Technology |
|-----------|------------|
| **Language** | Kotlin |
| **UI Framework** | Jetpack Compose |
| **QR Scanning** | Google ML Kit Vision API |
| **QR Generation** | ZXing (Zebra Crossing) Library |
| **Architecture** | MVVM with Compose State Management |
| **Image Processing** | Android Graphics API |
| **File Sharing** | Android FileProvider |
| **Storage** | SharedPreferences |

## 📋 Requirements

- **Android Version**: API Level 26 (Android 8.0) or higher
- **Permissions Required**:
  - 📷 Camera (for QR scanning)
  - 💾 Storage (for saving QR codes)
- **Network**: Internet connection for ML Kit module installation (first run only)
- **Device**: Any Android device with camera

## 🚀 Installation

### Option 1: Download APK

<div align="center">

[![Download APK](https://img.shields.io/badge/Download-APK-green?style=for-the-badge&logo=android)](https://github.com/Surajsinghyadav/QRify/raw/refs/heads/master/app/release/app-release.apk)

</div>

### Option 2: Build from Source

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/QRify.git
   cd QRify
   ```

2. **Open in Android Studio**
   - Launch Android Studio
   - Select "Open an Existing Project"
   - Navigate to the cloned directory

3. **Build and Run**
   ```bash
   ./gradlew assembleDebug
   ```

4. **Install on device**
   ```bash
   ./gradlew installDebug
   ```

## 🎮 Usage Guide

### Scanning QR Codes
1. Open QRify and ensure you're on the "Scan" tab
2. Tap the scan card to launch camera
3. Point camera at QR code - auto-zoom will activate
4. View results and use copy/share/open actions
5. Access recent scans from the history section

### Generating QR Codes
1. Switch to "Generate" tab
2. Enter your text, URL, or data in the input field
3. Choose from predefined colors or create custom colors
4. Tap "Generate" to create your QR code
5. Save to gallery or share the generated QR code

### Custom Colors
1. In Generator mode, scroll to "Customize Appearance"
2. Tap "Custom Color" card
3. Enter hex color codes for foreground and background
4. Preview colors in real-time
5. Confirm to apply your custom theme

## 🏗️ Architecture

```
Directory structure:
└── surajsinghyadav-qrify/
    ├── build.gradle.kts
    ├── gradle.properties
    ├── gradlew
    ├── gradlew.bat
    ├── settings.gradle.kts
    ├── app/
    │   ├── build.gradle.kts
    │   ├── proguard-rules.pro
    │   ├── release/
    │   │   ├── output-metadata.json
    │   │   └── baselineProfiles/
    │   │       ├── 0/
    │   │       │   └── app-release.dm
    │   │       └── 1/
    │   │           └── app-release.dm
    │   └── src/
    │       ├── androidTest/
    │       │   └── java/
    │       │       └── com/
    │       │           └── example/
    │       │               └── qrify/
    │       │                   └── ExampleInstrumentedTest.kt
    │       ├── main/
    │       │   ├── AndroidManifest.xml
    │       │   ├── java/
    │       │   │   └── com/
    │       │   │       └── example/
    │       │   │           └── qrify/
    │       │   │               ├── AnimVideo.kt
    │       │   │               ├── ListofColors.kt
    │       │   │               ├── MainActivity.kt
    │       │   │               ├── QR Generator.kt
    │       │   │               ├── QR Scanner.kt
    │       │   │               ├── RecentScans.kt
    │       │   │               ├── SaveQR.kt
    │       │   │               └── ui/
    │       │   │                   └── theme/
    │       │   │                       ├── Color.kt
    │       │   │                       ├── Theme.kt
    │       │   │                       └── Type.kt
    │       │   └── res/
    │       │       ├── drawable/
    │       │       │   ├── ic_launcher_background.xml
    │       │       │   └── ic_launcher_foreground.xml
    │       │       ├── mipmap-anydpi/
    │       │       │   ├── ic_launcher.xml
    │       │       │   └── ic_launcher_round.xml
    │       │       ├── mipmap-hdpi/
    │       │       │   ├── ic_launcher.webp
    │       │       │   └── ic_launcher_round.webp
    │       │       ├── mipmap-mdpi/
    │       │       │   ├── ic_launcher.webp
    │       │       │   └── ic_launcher_round.webp
    │       │       ├── mipmap-xhdpi/
    │       │       │   ├── ic_launcher.webp
    │       │       │   └── ic_launcher_round.webp
    │       │       ├── mipmap-xxhdpi/
    │       │       │   ├── ic_launcher.webp
    │       │       │   └── ic_launcher_round.webp
    │       │       ├── mipmap-xxxhdpi/
    │       │       │   ├── ic_launcher.webp
    │       │       │   └── ic_launcher_round.webp
    │       │       ├── values/
    │       │       │   ├── colors.xml
    │       │       │   ├── strings.xml
    │       │       │   └── themes.xml
    │       │       └── xml/
    │       │           ├── backup_rules.xml
    │       │           ├── data_extraction_rules.xml
    │       │           └── file_paths.xml
    │       └── test/
    │           └── java/
    │               └── com/
    │                   └── example/
    │                       └── qrify/
    │                           └── ExampleUnitTest.kt
    └── gradle/
        ├── libs.versions.toml
        └── wrapper/
            └── gradle-wrapper.properties

```

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request. For major changes, please open an issue first to discuss what you would like to change.

1. **Fork the Project**
2. **Create your Feature Branch** (`git checkout -b feature/AmazingFeature`)
3. **Commit your Changes** (`git commit -m 'Add some AmazingFeature'`)
4. **Push to the Branch** (`git push origin feature/AmazingFeature`)
5. **Open a Pull Request**

### Development Guidelines
- Follow Kotlin coding conventions
- Use Jetpack Compose for UI components
- Write meaningful commit messages
- Test on multiple Android versions
- Update documentation for new features

## 📝 Changelog

<details>
<summary>View Changelog</summary>

### [1.0.0] - 2024-01-15
#### Added
- Initial release with QR scanning and generation
- Material Design 3 interface
- Custom color themes
- Scan history functionality
- Save and share capabilities

#### Features
- Google ML Kit integration for scanning
- ZXing library for QR generation
- High error correction level support
- Auto-zoom scanning capability

</details>

## 🐛 Known Issues

- ML Kit module installation may take time on first run
- Custom colors require valid hex format input
- Large text content may not generate readable QR codes

## 🔮 Roadmap

- [ ] Batch QR code generation
- [ ] QR code templates (WiFi, Contact, etc.)
- [ ] Dark mode theme
- [ ] Export scan history
- [ ] QR code analytics
- [ ] Widget support for quick scanning

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Author

**Suraj**
- GitHub: [@Surajsinghyadav](https://github.com/Surajsinghyadav)
- LinkedIn: [Suraj singh yadav](https://www.linkedin.com/in/surajsinghyadav/)
- Email: [ysurajsingh56b@gmail.com](mailto:ysurajsingh56b@gmail.com)
## 🙏 Acknowledgments

- [ZXing Project](https://github.com/zxing/zxing) for QR code generation
- [Google ML Kit](https://developers.google.com/ml-kit) for scanning capabilities
- [Jetpack Compose](https://developer.android.com/jetpack/compose) for modern UI
- [Material Design](https://material.io/) for design guidelines

## 📊 Stats

![GitHub stars](https://img.shields.io/github/stars/yourusername/QRify?style=social)
![GitHub forks](https://img.shields.io/github/forks/yourusername/QRify?style=social)
![GitHub issues](https://img.shields.io/github/issues/yourusername/QRify)
![GitHub last commit](https://img.shields.io/github/last-commit/yourusername/QRify)

---

<div align="center">

**Made with ❤️ using Kotlin & Jetpack Compose**

[⬆ Back to Top](#qrify---qr-code-scanner--generator)

</div>
