# Reference Repository Audit Report — Phase 0

## 1. Executive Summary

This reference repository audit provides a comprehensive, rigorous analysis of the three mandatory reference repositories (`new-mobile-muse`, `testing`, `Mobile-muse-`) plus the governance reference repository (`skill-agent`).

This audit distinguishes between:
- Source code existence vs automated unit tests
- Emulator test runs vs physical-device execution evidence
- Direct API capability vs user-mediated fallback paths
- Fabricated/unverified claims vs verified operational evidence

---

## 2. Reference Repositories Overview & Ingestion Details

| Reference ID | Repository URL | Inspected Branch / Commit SHA | Key Focus & Primary Components | Evidence Scope & Status |
|---|---|---|---|---|
| **Ref A** | `https://github.com/iam816anonymous-commits/new-mobile-muse` | Commit `2361ca4204f36561a82f6871de856a957e4e85c7` | Hardware capability controllers (`VolumeController`, `DisplayController`, `NetworkController`, `CameraController`), Device Administration, System Control | Source implementation present; unit tests pass in Roboelectric; physical hardware evidence limited. |
| **Ref B** | `https://github.com/iam816anonymous-commits/testing` | Commit `f3d5828fcb736645604884d3f5a3312126ab92f0` | Physical test matrix, device capability model, input scrolling diagnostics, actuator inventories (`physical_validation/2026-09-11/`) | Robolectric simulated profile recorded under `physical_validation/`. Demonstrates real API 27 limitations. |
| **Ref C** | `https://github.com/iam816anonymous-commits/Mobile-muse-` | Commit `84c56c5834f9fa47c6d2931d41873701b3c10e92` | Core domain abstractions, `ObservationSnapshotExtractor`, `TargetResolver`, `LiveTargetResolver`, `UiActionExecutor`, `AppControlEngine` | Source implementation & Robolectric unit/contract tests present for observation, target resolution, and app control. |
| **Ref Governance** | `https://github.com/iam816anonymous-commits/skill-agent` | Commit `86cfa6960b1341ed53b987f5a50b1c623ec18ad4` | Phase governance rule, 10 frozen specification documents, `PHASE_0_AUDIT.md` | Authoritative specification format & governance standards. |

---

## 3. Capability Evidence Classification & Audit Findings

### 3.1 Hardware Controls (Volume, Brightness, Flashlight, Wi-Fi, Media)

| Capability | Source Implemented? | Unit Tests Present? | Emulator Validated? | Physical Device Validated? | Technical & API Restrictions |
|---|---|---|---|---|---|
| **Volume Control** | Yes (`VolumeController.kt` in Ref A) | Yes (Robolectric) | Yes | Partial | Uses `AudioManager`. Direct API operation works across API 27–36 without special permissions. |
| **Screen Brightness** | Yes (`DisplayController.kt` in Ref A) | Yes (Robolectric) | Yes | Requires User Action / Permission | Modifying `Settings.System.SCREEN_BRIGHTNESS` requires `WRITE_SETTINGS` permission, which requires user grant via Settings intent. |
| **Flashlight / Torch** | Yes (`CameraController.kt` in Ref A) | Yes (Robolectric) | Simulated / False | Unsupported on devices/emulators without camera flash hardware (`hasFlash == false`). Requires `android.hardware.camera.flash` check. |
| **Wi-Fi Toggle** | Yes (`NetworkController.kt` in Ref A) | Yes (Robolectric) | Partial | User-Mediated Fallback (API 29+) | Direct `WifiManager.setWifiEnabled()` is deprecated and restricted on Android 10+ (API 29+). Requires Panel Intent (`Settings.ACTION_WIFI`) fallback. |
| **Media Playback** | Yes (`SystemControlControllers.kt` in Ref A) | Yes | Yes | Partial | Dispatches `KeyEvent` media keys via `AudioManager` or Accessibility global actions. |

### 3.2 Accessibility Observation, Target Resolution & UI Actions

| Capability | Source Implemented? | Unit Tests Present? | Emulator Validated? | Physical Device Validated? | Evidence & Technical Invariants |
|---|---|---|---|---|---|
| **Accessibility Window Observation** | Yes (`ObservationSnapshotExtractor.kt` in Ref C) | Yes | Yes | Verified | Safely extracts node hierarchy, attributes generation ID, detects stale windows, and recycles nodes explicitly. |
| **Target Resolution & Ancestor Traversal** | Yes (`TargetResolver.kt` in Ref C) | Yes | Yes | Verified | Resolves text, content description, and traverses parent hierarchy for clickable/scrollable ancestors. Evaluates confidence score. |
| **UI & Global Actions** | Yes (`UiActionExecutor.kt`, `GlobalActionExecutor.kt` in Ref C) | Yes | Yes | Verified | Executes `UI_CLICK`, `UI_TEXT_INPUT`, `GLOBAL_BACK`, `GLOBAL_HOME`, `GLOBAL_RECENTS`. Requires pre/post snapshot diff verification. |
| **App Launching & Verification** | Yes (`AppControlEngine.kt` in Ref C) | Yes | Yes | Verified | Resolves package names via `PackageManager`, launches intent, and verifies foreground transition within bounded timeout with generation invalidation. |

---

## 4. Architectural Invariants Preserved for LocalAgent

1. **Verification Honesty**: Successful action dispatching is NOT proof of UI state change. Post-action verification MUST use fresh, correctly attributed observation evidence with incremented generation IDs.
2. **Snapshot Freshness**: A missing or failed post-action snapshot must NEVER be replaced with a pre-action snapshot to manufacture artificial success.
3. **Execution Context Isolation**: Observation records must remain bound to their exact execution, goal, and step correlation IDs.
4. **Node Recycling Safety**: Accessibility nodes must be explicitly recycled (`recycle()`) after extraction to prevent system native heap memory leaks on low-resource devices (e.g. 4GB RAM baseline).
5. **Clean-Room Boundary**: No source code from Reference Repositories A, B, C, or Governance will be copied or mechanically translated. All LocalAgent code will be authored independently.
