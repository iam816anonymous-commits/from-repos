package com.localagent.app.accessibility

import android.os.Bundle
import android.view.accessibility.AccessibilityNodeInfo
import com.localagent.core.action.ActionRequest
import com.localagent.core.action.ActionType
import com.localagent.core.action.VerificationStrategies
import com.localagent.core.observation.ObservationSnapshot
import com.localagent.core.observation.SnapshotDiffEngine
import com.localagent.core.result.ExecutionResult
import com.localagent.core.result.ResultCode

class UiActionExecutor(
    private val liveTargetResolver: LiveTargetResolver = LiveTargetResolver(),
    private val snapshotDiffEngine: SnapshotDiffEngine = SnapshotDiffEngine(),
    private val verificationStrategies: VerificationStrategies = VerificationStrategies()
) {

    fun executeUiAction(
        request: ActionRequest,
        rootNode: AccessibilityNodeInfo?,
        preSnapshot: ObservationSnapshot,
        postSnapshotExtractor: () -> ObservationSnapshot
    ): ExecutionResult {
        if (rootNode == null || !preSnapshot.isValid) {
            return ExecutionResult(
                resultCode = ResultCode.STALE_OBSERVATION,
                message = "Invalid or stale pre-action observation snapshot"
            )
        }

        val selector = request.targetSelector
        if (selector.isNullOrEmpty() || selector.isBlank()) {
            return ExecutionResult(
                resultCode = ResultCode.INVALID_COMMAND,
                message = "Missing target selector for UI action"
            )
        }

        val resolvedPair = liveTargetResolver.resolveAndFindNode(rootNode, preSnapshot, selector)
            ?: return ExecutionResult(
                resultCode = ResultCode.TARGET_NOT_FOUND,
                message = "Target '$selector' not found or target confidence insufficient"
            )

        val (_, liveNode) = resolvedPair
        if (liveNode == null) {
            return ExecutionResult(
                resultCode = ResultCode.TARGET_NOT_FOUND,
                message = "Target '$selector' resolved in snapshot but live AccessibilityNode missing"
            )
        }

        val actionDispatched = when (request.actionType) {
            ActionType.UI_CLICK -> liveNode.performAction(AccessibilityNodeInfo.ACTION_CLICK)
            ActionType.UI_LONG_CLICK -> liveNode.performAction(AccessibilityNodeInfo.ACTION_LONG_CLICK)
            ActionType.UI_TEXT_INPUT -> {
                val args = Bundle().apply {
                    putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, request.textValue ?: "")
                }
                liveNode.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, args)
            }
            ActionType.UI_SCROLL_FORWARD -> liveNode.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD)
            ActionType.UI_SCROLL_BACKWARD -> liveNode.performAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD)
            else -> false
        }

        if (!actionDispatched) {
            return ExecutionResult(
                resultCode = ResultCode.EXECUTION_FAILED,
                message = "Accessibility action dispatch returned false"
            )
        }

        // Capture fresh post-action observation
        val postSnapshot = postSnapshotExtractor()
        val diff = snapshotDiffEngine.computeDiff(preSnapshot, postSnapshot)
        val verified = verificationStrategies.verifyActionOutcome(request.actionType, diff)

        return ExecutionResult(
            resultCode = if (verified) ResultCode.SUCCESS else ResultCode.EXECUTION_FAILED,
            message = if (verified) "UI Action verified successful" else "UI Action executed but post-action observation showed no semantic UI change",
            preGenerationId = preSnapshot.generationId,
            postGenerationId = postSnapshot.generationId
        )
    }
}
