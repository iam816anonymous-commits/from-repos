# Mobile to Desktop Scaling & Platform Adaptation Specification

## 1. Domain Core Platform Independence
The domain core (`:core`) is authored as a pure Kotlin/JVM module without `android.*` platform classes.

## 2. Platform Abstraction Layer
Platform capabilities are accessed via explicit interface adapters:
- `AccessibilityProvider` -> Implemented by `AgentAccessibilityService` on Android.
- `StorageRepository` -> Implemented by Room SQLite on Android / JDBC SQLite on Desktop JVM.
- `ApplicationLauncher` -> Implemented by Android `PackageManager` Intent launcher on Android.
- `HardwareController` -> Implemented by Android `AudioManager` / `CameraManager` / `Settings` on Android.
