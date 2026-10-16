# Orchestration Protocol Specification

## 1. Event & Command Bus Message Format
All internal agent operations communicate using structured event logs and message envelopes:
- `messageId`: Unique UUID string.
- `correlationId`: Correlation ID linking all steps within a single goal/task.
- `stepId`: Sequential step counter.
- `timestamp`: Epoch millisecond timestamp.
- `eventType`: Enum type (`COMMAND_SUBMITTED`, `POLICY_EVALUATED`, `OBSERVATION_CAPTURED`, `ACTION_DISPATCHED`, `VERIFICATION_COMPLETED`).
- `payload`: Structured JSON payload.

## 2. Event Correlation Protocol
Correlation IDs must be attached across the entire execution chain from command normalizer -> policy check -> snapshot extraction -> target resolution -> action execution -> verification -> Room persistence.
