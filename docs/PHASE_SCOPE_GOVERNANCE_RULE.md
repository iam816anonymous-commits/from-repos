# Phase Scope Governance Rule Specification

## 1. Executive Summary

This document defines the strict engineering governance and phase scoping rules for LocalAgent development. To prevent scope creep, architectural drift, accidental refactoring, and unverified work, all development activities MUST comply with this governance rule.

## 2. Core Governance Rules

### Rule 1: Phase Alignment Check Requirement
Before creating, editing, deleting, moving, or generating ANY project file:
1. Read `docs/PHASE_SCOPE_GOVERNANCE_RULE.md`.
2. Identify the currently authorized phase.
3. Inspect repository state and confirm that all predecessor phase exit gates have been satisfied and certified.
4. Generate a formal **Phase Alignment Check** in the phase completion report.

The **Phase Alignment Check** MUST contain:
- Active Phase ID and Phase Name.
- Approved Inputs and Prerequisites.
- Permitted Deliverables and Authorized Files.
- Explicitly Prohibited Future-Phase Work.
- Authorized Test Suites.
- Exit Criteria and Required Evidence Files.

### Rule 2: Strict Phase Boundary Enforcement
- Implement ONLY the deliverables authorized for the active phase.
- **NEVER pull forward future-phase code or functionality**, even if doing so appears convenient or trivial.
- Read access to reference files does NOT grant authorization to edit those files.
- Test execution authorization does NOT grant permission to modify tests or lower assertion thresholds.

### Rule 3: Stop Conditions & Blocker Escalation
Immediately STOP implementation and report a blocker if:
1. Required predecessor phase specifications or exit gates have NOT passed.
2. The repository state contradicts frozen specifications in `docs/`.
3. The assigned task requires modifying files explicitly prohibited in the active phase scope.
4. Missing dependencies or environment constraints prevent running phase-authorized tests.

### Rule 4: No Unilateral Refactoring or Architectural Alterations
- Do NOT refactor unrelated modules, rewrite existing architecture, or delete existing functionality to make a test pass.
- Fix defects ONLY within the authorized active phase scope.

### Rule 5: Honest Verification and Evidence Gate
- All test results MUST be recorded accurately and honestly.
- A failed test CANNOT be reported as a pass.
- A desktop JVM test or simulator test CANNOT be reported as physical Android low-RAM hardware certification.
- Never delete, disable, or suppress failing tests to achieve a passing report status.

### Rule 6: Explicit Authorization for Phase Transition
- **Do NOT advance automatically to the next phase** upon completing an active phase.
- Stop and wait for explicit human user authorization before commencing subsequent phases.

## 3. Detailed Phase Scope & Boundary Matrix

| Phase ID | Phase Name | Primary Scope & Authorized Deliverables | Strictly Prohibited Work | Required Exit Evidence File |
|---|---|---|---|---|
| **Phase 0** | Specification & Governance | Freeze specifications in `docs/`, `REFERENCE_REPOSITORY_AUDIT.md`, `PHASE_0_AUDIT.md`, `README.md`, `AGENTS.md`. | NO Kotlin application source files, NO Gradle build scripts, NO runtime code. | `docs/PHASE_0_AUDIT.md` |
| **Phase 1** | Foundation & Domain Core | Pure Kotlin models, command normalizer, capability registry, action policy engine, task lifecycle, goal dispatcher, result codes. Unit tests. | NO Android APIs (`android.*`), NO Room DB, NO UI, NO accessibility code. | `docs/PHASE_1_AUDIT.md` |
| **Phase 2** | Storage & Persistence | Database schema, SQLite/Room persistence, event logger, correlation tracking, migrations. | NO accessibility automation, NO UI overlay. | `docs/PHASE_2_AUDIT.md` |
| **Phase 3** | Permission Manager | Permission manager, capability adapters, settings navigation contracts. | NO accessibility action execution. | `docs/PHASE_3_AUDIT.md` |
| **Phase 4** | Accessibility Service | Accessibility service connection monitor, window tracking, binding state. | NO target resolution or action execution. | `docs/PHASE_4_AUDIT.md` |
| **Phase 5** | Observation & Snapshots | Observation snapshot extractor, snapshot diff engine, stale observation rejection, node recycling. | NO target resolution or action execution. | `docs/PHASE_5_AUDIT.md` |
| **Phase 6** | Target Resolution | Target resolver, actionable ancestor resolver, confidence scoring, target reacquisition. | NO global or UI action execution. | `docs/PHASE_6_AUDIT.md` |
| **Phase 7** | Action Execution | Global action executor, UI action executor, pre/post verification strategies. | NO app launcher or hardware controllers. | `docs/PHASE_7_AUDIT.md` |
| **Phase 8** | Application Control Engine | App control engine, package resolver, app launcher, foreground verifier. | NO hardware controls. | `docs/PHASE_8_AUDIT.md` |
