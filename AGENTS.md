# AGENTS.md - Repository Standards & Guidelines

This document provides architectural standards, development conventions, and operational guidelines for AI agents and developers working on the `contador_presencial` project.

---

## 1. Project Overview

- **Project Name:** `contador_presencial`
- **Description:** Android application designed to track and count in-person work days.
- **Application Package / Namespace:** `br.com.contador_presencial`
- **Application ID:** `br.com.contador_presencial`
- **Primary Language:** Kotlin (with Jetpack Compose)

---

## 2. Technology Stack & Environment

| Component | Version / Specification | Notes |
|---|---|---|
| **Kotlin** | `2.2.10` | Official code style (`kotlin.code.style=official`) |
| **Android Gradle Plugin (AGP)** | `9.5.1` | AGP 9.x architecture |
| **Gradle** | `9.8.2` | Kotlin DSL (`*.gradle.kts`), configuration cache enabled |
| **Compile SDK / Target SDK** | `37` (Android 17 preview/release 37) | Defined via `release(37)` |
| **Min SDK** | `24` | Android 7.0 (Nougat) |
| **JVM Target / Compatibility** | Java 11 (`JavaVersion.VERSION_11`) | Foojay toolchains resolver convention 1.0.0 |
| **Compose BOM** | `2026.02.01` | Bill of Materials for Compose dependencies |
| **UI Framework** | Jetpack Compose + Material 3 | Full declarative UI stack |
| **Navigation** | Jetpack Navigation Compose `2.10.2` | Type-safe navigation ready |
| **Serialization** | `kotlinx-serialization-json` `1.7.3` | Type-safe route arguments & data models |

---

## 3. Project Directory Structure

```text
contador_presencial/
├── .gitignore
├── AGENTS.md                             # AI Agent instructions and project standards
├── README.md                             # Project overview and introduction
├── build.gradle.kts                      # Root Gradle build script
├── settings.gradle.kts                   # Project repositories and module declarations
├── gradle.properties                     # JVM and Gradle flags (configuration-cache, UTF-8, etc.)
├── gradle/
│   ├── libs.versions.toml                # Centralized Gradle Version Catalog
│   └── wrapper/                          # Gradle wrapper binaries and properties (9.8.2)
└── app/
    ├── build.gradle.kts                  # App module configuration, plugins, and dependencies
    └── src/
        ├── main/
        │   ├── AndroidManifest.xml       # App manifest, MainActivity declaration, backup rules
        │   ├── keepRules/
        │   │   └── rules.keep            # AGP 9 combined R8 keep rules
        │   ├── java/br/com/contador_presencial/
        │   │   ├── MainActivity.kt       # Single activity entry point and top-level screen composables
        │   │   └── ui/theme/
        │   │       ├── Color.kt          # Material 3 color definitions (Dark & Light schemes)
        │   │       ├── Theme.kt          # Theme wrapper with dynamic color support (Android 12+)
        │   │       └── Type.kt           # Typography scale and text styles
        │   └── res/                      # Android resources (values, mipmaps, drawables, XML configs)
        ├── test/                         # Local JVM unit tests (JUnit 4)
        └── androidTest/                  # Instrumented on-device tests (Compose UI Test, Espresso)
```

---

## 4. Development & Coding Conventions

### 4.1 Dependency Management
- **Single Source of Truth:** All dependencies and plugins must be registered in `gradle/libs.versions.toml`.
- **Naming Pattern:** Use kebab-case for catalog alias names (e.g., `androidx-compose-material3`, `kotlinx-serialization-json`).
- Never hardcode dependency strings or versions in `app/build.gradle.kts`.

### 4.2 Jetpack Compose & UI Guidelines
- **Composable Function Naming:** Composable functions that return `Unit` must use **PascalCase** (e.g., `ContadorScreen`, `ConfigurationScreen`, `StatCard`).
- **Modifier Discipline:**
  - Every reusable composable must accept an optional `modifier: Modifier = Modifier` as its first optional parameter.
  - Apply the passed `modifier` to the root layout element within the composable.
- **Edge-to-Edge:**
  - The app invokes `enableEdgeToEdge()` in `MainActivity.onCreate()`.
  - Always handle window insets properly using `Scaffold`'s `innerPadding` or `Modifier.windowInsetsPadding()`.
- **State Management & UDF:**
  - Follow Unidirectional Data Flow (UDF): hoist state where necessary; pass data down and callbacks/events up (e.g., `onIncrement: () -> Unit`).
  - Use `rememberSaveable` for UI state that must survive configuration changes, or migrate complex state to an `AndroidViewModel` / `ViewModel`.
- **Theme Consistency:**
  - Rely on `MaterialTheme.colorScheme` and `MaterialTheme.typography` rather than hardcoded colors and text styles.
  - Preview composables should wrap components in `Contador_presencialTheme` with `showBackground = true`.

### 4.3 String & Resource Localization
- Extract user-facing text into `app/src/main/res/values/strings.xml`.
- Access strings in composables using `stringResource(R.string.<id>)`.
- Avoid hardcoded user-facing strings in UI functions.

### 4.4 AGP 9 & R8 Keep Rules
- Release builds enable AGP 9 optimization with scoped package optimization:
  ```kotlin
  optimization {
      enable = true
      packageScope = setOf("androidx.**", "kotlin.**", "kotlinx.**")
  }
  ```
- Any custom ProGuard/R8 keep rules should be placed in `app/src/main/keepRules/rules.keep`.

---

## 5. Build, Test, and Verification Commands

Execute the following Gradle tasks from the project root using `gradlew` (or `gradlew.bat` on Windows):

### 5.1 Compilation & Assembly
```bash
# Assemble debug APK
./gradlew :app:assembleDebug

# Assemble release APK
./gradlew :app:assembleRelease

# Check whole project build
./gradlew check
```

### 5.2 Unit Testing
```bash
# Run local JVM unit tests
./gradlew :app:testDebugUnitTest

# Run specific unit test class
./gradlew :app:testDebugUnitTest --tests "br.com.contador_presencial.ExampleUnitTest"
```

### 5.3 Instrumented & UI Testing
```bash
# Run connected Android instrumentation tests on an active device/emulator
./gradlew :app:connectedDebugAndroidTest
```

### 5.4 Code Analysis & Linting
```bash
# Run Android lint checks
./gradlew :app:lintDebug
```

---

## 6. Guidelines for AI Agents

1. **IDE Safety & File Edits:**
   - Always use IDE-aware file editing tools (`write_file`, `replace_file_content`, `multi_replace_file_content`) to prevent desynchronizing IDE memory buffers.
   - Do not use shell commands (`sed`, `cat >`, echo redirection) to modify project source code.
2. **Context Verification:**
   - Review `gradle/libs.versions.toml` before adding or updating libraries.
   - Check `app/build.gradle.kts` configuration flags before introducing plugins or language features.
3. **Preserve Architecture & Style:**
   - Respect the established package hierarchy (`br.com.contador_presencial`).
   - Maintain idiomatic Kotlin style with concise, type-safe implementations.
   - Keep documentation, comments, and docstrings intact unless directly modified by requirements.
4. **Validation:**
   - Run `:app:assembleDebug` or `:app:testDebugUnitTest` to verify that changes compile cleanly before concluding a task.
