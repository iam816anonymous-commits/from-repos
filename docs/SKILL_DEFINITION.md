# Skill & Command Definition Specification

## 1. Overview
This document specifies the normalized command and skill representation for LocalAgent.

## 2. Command Representation
Commands are parsed into a normalized structure independent of Android platform UI classes:
- `rawCommand`: The original text instruction.
- `intent`: The parsed intent (e.g., `OPEN_APP`, `CLICK`, `INPUT_TEXT`, `GLOBAL_NAVIGATION`, `HARDWARE_CONTROL`).
- `targetSelector`: Optional target UI element selector (text, contentDescription, resourceId).
- `parameters`: Key-value parameters (e.g., app name, text input value, volume level).
- `riskLevel`: Risk classification (`SAFE`, `MODERATE`, `HIGH_RISK`).

## 3. Skill & Action Taxonomy
1. **System & Global Actions**: `GLOBAL_BACK`, `GLOBAL_HOME`, `GLOBAL_RECENTS`.
2. **UI Interaction Actions**: `UI_CLICK`, `UI_LONG_CLICK`, `UI_TEXT_INPUT`, `UI_SCROLL_FORWARD`, `UI_SCROLL_BACKWARD`.
3. **Application Actions**: `APP_RESOLVE`, `APP_LAUNCH`, `APP_VERIFY_FOREGROUND`.
4. **Hardware Actions**: `VOLUME_SET`, `BRIGHTNESS_SET`, `TORCH_SET`, `MEDIA_CONTROL`, `WIFI_SET_STATE`.

## 4. Execution Result Contracts
All action executions return a structured result:
- `isSuccess`: Boolean status.
- `resultCode`: Result code enum (`SUCCESS`, `TARGET_NOT_FOUND`, `POLICY_DENIED`, `PERMISSION_REQUIRED`, `STALE_OBSERVATION`, `TIMEOUT`).
- `observationDiff`: Diff between pre-action and post-action observation snapshots.
- `message`: Diagnostic message.
