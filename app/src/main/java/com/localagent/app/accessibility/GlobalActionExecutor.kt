package com.localagent.app.accessibility

import android.accessibilityservice.AccessibilityService
import com.localagent.core.result.ExecutionResult
import com.localagent.core.result.ResultCode

class GlobalActionExecutor(
    private val accessibilityService: AccessibilityService?
) {

    fun executeGlobalAction(action: String): ExecutionResult {
        if (accessibilityService == null) {
            return ExecutionResult(
                resultCode = ResultCode.PERMISSION_REQUIRED,
                message = "Accessibility service not bound"
            )
        }

        val globalActionInt = when (action.uppercase()) {
            "BACK" -> AccessibilityService.GLOBAL_ACTION_BACK
            "HOME" -> AccessibilityService.GLOBAL_ACTION_HOME
            "RECENTS" -> AccessibilityService.GLOBAL_ACTION_RECENTS
            else -> return ExecutionResult(
                resultCode = ResultCode.INVALID_COMMAND,
                message = "Unsupported global action $action"
            )
        }

        val success = accessibilityService.performGlobalAction(globalActionInt)
        return ExecutionResult(
            resultCode = if (success) ResultCode.SUCCESS else ResultCode.EXECUTION_FAILED,
            message = if (success) "Executed global action $action" else "Failed global action $action"
        )
    }
}
