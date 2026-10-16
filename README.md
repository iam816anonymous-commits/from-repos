# LocalAgent — Mobile Autonomous Agent Framework

## Overview
LocalAgent is an Android-native mobile agent framework designed for on-device screen observation, target resolution, action execution, application control, and system management across Android API 27 through API 36+.

## Architecture
- `:core`: Pure Kotlin domain module free of `android.*` platform or Room dependencies.
- `:app`: Android application module providing Room SQLite persistence, AccessibilityService integration, observation snapshot extraction, live target resolution, UI/Global action execution, application control engine, and hardware management.

## Specifications & Documentation
All architectural specifications, governance rules, and reference audits are located in `docs/`:
- `docs/REFERENCE_REPOSITORY_AUDIT.md`: Complete audit of reference repositories and physical device evidence.
- `docs/PHASE_SCOPE_GOVERNANCE_RULE.md`: Phase scope boundaries and alignment rules.
- `docs/PHASE_0_AUDIT.md`: Phase 0 freeze and specification audit report.
- `docs/SKILL_DEFINITION.md`: Normalized command and skill taxonomy.
- `docs/SUBAGENT_LIFECYCLE.md`: Goal and task lifecycle state machine.
- `docs/RESOURCE_BUDGET.md`: Low-RAM baseline budget (Tecno Camon i Click, API 27+).
- `docs/SECURITY_MODEL.md`: Security policies, risk classification, and permissions.
- `docs/API_LEVEL_MATRIX.md`: Android API 27–36+ capability and adaptation matrix.
