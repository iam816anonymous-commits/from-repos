# Subagent & Task Lifecycle Specification

## 1. Task Lifecycle State Machine
Every goal/task submitted to LocalAgent follows a strict deterministic lifecycle state machine:
- `CREATED`: Task instantiated and queued.
- `VALIDATED`: Command normalized and policy/permission verified.
- `RUNNING`: Execution lock acquired, active observation captured, and action in progress.
- `PAUSED`: Execution suspended pending user input or permission grant.
- `COMPLETED`: Execution verified successful by post-action observation diff.
- `FAILED`: Execution failed with explicit failure category and diagnostic code.
- `CANCELLED`: Execution explicitly cancelled by user or watchdog timeout.

## 2. Execution Locking & Coordination
- Single-threaded execution lock prevents concurrent UI mutations.
- Priority queue routes high-priority commands (e.g. panic/stop) ahead of standard task steps.
- Cancellation token checked before every state-changing step.
