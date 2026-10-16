package com.localagent.core.resolver

import com.localagent.core.observation.AccessibilityNodeModel

data class TargetMatch(
    val node: AccessibilityNodeModel,
    val confidenceScore: Float,
    val isActionableAncestor: Boolean = false,
    val matchReason: String = ""
)
