# Isolation Model Specification

## 1. Trust Boundaries
LocalAgent operates under strict execution trust boundaries:
- Pure Domain Core (`:core`): Fully isolated from platform APIs and UI.
- Local Agent App (`:app`): Bounded by Android runtime permissions and Accessibility Service system security boundaries.

## 2. Context & Data Isolation
- Observation snapshots contain only UI tree elements necessary for target resolution.
- Sensitive user credentials and password fields (`isPassword == true`) are filtered out from structured event logs and database persistence.
