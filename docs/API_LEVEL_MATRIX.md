# API Level Compatibility Matrix Specification

## 1. Supported Android API Range
- Baseline API Level: API 27 (Android 8.1 Oreo).
- Target API Level: API 35/36 (Android 15/16).

## 2. Platform Feature Adaptations

| Feature / Capability | API 27–28 Behavior | API 29–33 Behavior | API 34+ Behavior |
|---|---|---|---|
| **Accessibility Node Traversal** | Standard `AccessibilityNodeInfo` hierarchy extraction | Standard hierarchy extraction | Enhanced window layer tracking |
| **Wi-Fi Toggle** | Direct `WifiManager.setWifiEnabled()` permitted | Deprecated; requires Settings Panel intent (`Settings.ACTION_WIFI`) | Requires Settings Panel intent (`Settings.ACTION_WIFI`) |
| **Volume Control** | `AudioManager.setStreamVolume()` direct API | `AudioManager.setStreamVolume()` direct API | `AudioManager.setStreamVolume()` direct API |
| **System Settings (Brightness)** | `Settings.System.SCREEN_BRIGHTNESS` (Requires `WRITE_SETTINGS`) | Requires `WRITE_SETTINGS` | Requires `WRITE_SETTINGS` |
| **Torch / Flashlight** | `CameraManager.setTorchMode()` (Requires camera flash hardware) | `CameraManager.setTorchMode()` | `CameraManager.setTorchMode()` |
