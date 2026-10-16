package com.localagent.core.action

import com.localagent.core.observation.SnapshotDiffResult

class VerificationStrategies {

    fun verifyActionOutcome(
        actionType: ActionType,
        diff: SnapshotDiffResult,
        expectedPackage: String? = null
    ): Boolean {
        return when (actionType) {
            ActionType.GLOBAL_BACK,
            ActionType.GLOBAL_HOME,
            ActionType.GLOBAL_RECENTS -> {
                diff.hasSemanticChange || diff.packageChanged
            }
            ActionType.UI_CLICK,
            ActionType.UI_LONG_CLICK,
            ActionType.UI_TEXT_INPUT,
            ActionType.UI_SCROLL_FORWARD,
            ActionType.UI_SCROLL_BACKWARD -> {
                diff.hasSemanticChange
            }
            ActionType.APP_LAUNCH -> {
                diff.packageChanged || (expectedPackage != null && diff.hasSemanticChange)
            }
            ActionType.HARDWARE_CONTROL -> {
                true
            }
        }
    }
}
