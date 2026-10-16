# Skill Manifest Schema Specification

## 1. Overview
This document specifies the JSON Schema and structure for Skill Definitions and Manifests.

## 2. Manifest Schema Requirements
A skill manifest specifies:
- `id`: Unique identifier (e.g. `com.localagent.skill.calculator`).
- `version`: SemVer string (e.g. `1.0.0`).
- `name`: Display name.
- `requiredCapabilities`: List of required capabilities (`ACCESSIBILITY`, `SYSTEM_SETTINGS`, `CAMERA_FLASH`, `AUDIO_CONTROL`).
- `minApiLevel`: Minimum Android API Level (default: 27).
- `actions`: List of exported action signatures and expected parameters.
