package com.localagent.core.resolver

import com.localagent.core.observation.AccessibilityNodeModel
import com.localagent.core.observation.ObservationSnapshot

class TargetResolver {

    fun resolveTarget(snapshot: ObservationSnapshot, selector: String): TargetMatch? {
        if (!snapshot.isValid || selector.isBlank()) return null

        val nodeMap = snapshot.nodes.associateBy { it.nodeId }
        val targetQuery = selector.trim().lowercase()

        val exactMatches = snapshot.nodes.filter { node ->
            node.text?.lowercase() == targetQuery ||
            node.contentDescription?.lowercase() == targetQuery ||
            node.viewIdResourceName?.lowercase()?.endsWith(targetQuery) == true
        }

        if (exactMatches.isEmpty()) {
            val partialMatches = snapshot.nodes.filter { node ->
                node.text?.lowercase()?.contains(targetQuery) == true ||
                node.contentDescription?.lowercase()?.contains(targetQuery) == true
            }

            if (partialMatches.isEmpty()) return null

            val bestMatchNode = partialMatches.first()
            return resolveActionableAncestor(bestMatchNode, nodeMap, 0.7f, "Partial text match")
        }

        val bestMatchNode = exactMatches.first()
        return resolveActionableAncestor(bestMatchNode, nodeMap, 1.0f, "Exact text match")
    }

    private fun resolveActionableAncestor(
        node: AccessibilityNodeModel,
        nodeMap: Map<String, AccessibilityNodeModel>,
        initialConfidence: Float,
        reason: String
    ): TargetMatch {
        if (node.isClickable || node.isLongClickable || node.isEditable) {
            return TargetMatch(
                node = node,
                confidenceScore = initialConfidence,
                isActionableAncestor = false,
                matchReason = reason
            )
        }

        var current: AccessibilityNodeModel? = node
        while (current?.parentNodeId != null) {
            val parent = nodeMap[current.parentNodeId] ?: break
            if (parent.isClickable || parent.isLongClickable || parent.isEditable) {
                return TargetMatch(
                    node = parent,
                    confidenceScore = initialConfidence * 0.9f,
                    isActionableAncestor = true,
                    matchReason = "$reason (Traversed to actionable parent ${parent.nodeId})"
                )
            }
            current = parent
        }

        return TargetMatch(
            node = node,
            confidenceScore = initialConfidence * 0.5f,
            isActionableAncestor = false,
            matchReason = "$reason (Non-actionable node)"
        )
    }
}
