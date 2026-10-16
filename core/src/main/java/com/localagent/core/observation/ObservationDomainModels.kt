package com.localagent.core.observation

data class AccessibilityNodeModel(
    val nodeId: String,
    val text: String? = null,
    val contentDescription: String? = null,
    val viewIdResourceName: String? = null,
    val className: String? = null,
    val packageName: String? = null,
    val boundsInScreen: String? = null,
    val isClickable: Boolean = false,
    val isLongClickable: Boolean = false,
    val isEditable: Boolean = false,
    val isScrollable: Boolean = false,
    val parentNodeId: String? = null,
    val childNodeIds: List<String> = emptyList()
)

data class ObservationSnapshot(
    val snapshotId: String,
    val generationId: Long,
    val packageName: String?,
    val windowTitle: String? = null,
    val capturedAt: Long = System.currentTimeMillis(),
    val nodes: List<AccessibilityNodeModel> = emptyList(),
    val isValid: Boolean = true
)
