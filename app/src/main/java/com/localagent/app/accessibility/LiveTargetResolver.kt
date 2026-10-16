package com.localagent.app.accessibility

import android.view.accessibility.AccessibilityNodeInfo
import com.localagent.core.observation.ObservationSnapshot
import com.localagent.core.resolver.TargetMatch
import com.localagent.core.resolver.TargetResolver

class LiveTargetResolver(
    private val targetResolver: TargetResolver = TargetResolver()
) {

    fun resolveAndFindNode(
        rootNode: AccessibilityNodeInfo?,
        snapshot: ObservationSnapshot,
        selector: String
    ): Pair<TargetMatch, AccessibilityNodeInfo?>? {
        if (rootNode == null || !snapshot.isValid) return null

        val match = targetResolver.resolveTarget(snapshot, selector) ?: return null
        if (match.confidenceScore < 0.5f) return null

        val liveNode = findLiveNodeById(rootNode, match.node.nodeId)
        return Pair(match, liveNode)
    }

    private fun findLiveNodeById(root: AccessibilityNodeInfo, targetId: String): AccessibilityNodeInfo? {
        val currentId = root.windowId.toString() + "_" + System.identityHashCode(root)
        if (currentId == targetId) return root

        for (i in 0 until root.childCount) {
            val child = root.getChild(i)
            if (child != null) {
                val found = findLiveNodeById(child, targetId)
                if (found != null) return found
            }
        }
        return null
    }
}
