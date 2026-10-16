# Security & Permission Model Specification

## 1. Classification of Capabilities & Risk Levels
- `SAFE`: Reading active window title, system navigation (`GLOBAL_BACK`, `GLOBAL_HOME`).
- `MODERATE`: App launching, clicking standard buttons, adjusting audio volume.
- `HIGH_RISK`: Text input into forms, modifying system settings (`WRITE_SETTINGS`), toggling network state.

## 2. Policy Enforcement & Permission Gates
- Every action MUST be checked against `ActionPolicyEngine` prior to execution.
- If a required runtime permission is missing, return `PERMISSION_REQUIRED` with the relevant Android settings intent.
- Platform security and Android permission boundaries must never be bypassed or mocked as granted when missing.
