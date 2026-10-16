# LocalAgent — Engineering Governance & Guidelines

## Mission
Build LocalAgent as a modular, Android-native mobile agent runtime for Android API 27–36+ with pure Kotlin domain core and reliable on-device execution.

## Mandatory Phase Governance
Before creating, editing, deleting, moving, or generating any project file:
1. Read `docs/PHASE_SCOPE_GOVERNANCE_RULE.md`.
2. Identify the currently authorized phase.
3. Read the specifications and acceptance criteria applicable to that phase in `docs/`.
4. Inspect the repository's current state and existing implementation.
5. Produce a **Phase Alignment Check** before any file changes.

## Engineering Standards
- Follow the frozen specifications in `docs/` and established clean-room conventions.
- Core domain in `:core` MUST be pure Kotlin/JVM without `android.*` or Room dependencies.
- Every UI action execution MUST be verified using fresh post-action observation snapshot diffs.
- Never weaken assertions, remove tests, suppress failures, or fabricate evidence.
