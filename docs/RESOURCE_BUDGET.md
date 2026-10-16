# Resource Budget Specification

## 1. Target Hardware Baseline
- Primary Low-Resource Baseline: Tecno Camon i Click (4 GB RAM, 64 GB Storage, Android 8.1 API 27+).
- Target Android API Range: API 27 through API 36+.

## 2. Resource Memory & Storage Limits
- Maximum Memory Overhead: Heap footprint < 35 MB during idle observation; < 80 MB peak during snapshot extraction & diff calculation.
- Database Footprint: Max WAL database size < 15 MB with automatic retention prune policy.
- Snapshot Node Recycling: Active accessibility node references MUST be explicitly recycled immediately after extraction into immutable value snapshots to prevent Android AccessibilityNodeInfo native leaks.

## 3. Execution Timeouts
- Foreground Launch Verification Timeout: Bounded 3.0 seconds maximum wait for target application window.
- Action Execution Timeout: Bounded 2.0 seconds maximum per UI action dispatch.
