package com.localagent.app.accessibility

import android.view.accessibility.AccessibilityNodeInfo
import com.localagent.core.observation.AccessibilityNodeModel
import com.localagent.core.observation.ObservationSnapshot
import java.util.UUID

class ObservationSnapshotExtractor {

    fun extractSnapshot(
        rootNode: AccessibilityNodeInfo?,
        packageName: String?,
        windowTitle: String?,
        generationId: Long
    ): ObservationSnapshot {
        if (rootNode == null) {
            return ObservationSnapshot(
                snapshotId = UUID.randomUUID().toString(),
                generationId = generationId,
                packageName = packageName,
                windowTitle = windowTitle,
                nodes = emptyList(),
                isValid = false
            )
        }

        val extractedNodes = mutableListOf<AccessibilityNodeModel>()
        flattenAndRecycleNodes(rootNode, parentId = null, outputList = extractedNodes)

        return ObservationSnapshot(
            snapshotId = UUID.randomUUID().toString(),
            generationId = generationId,
            packageName = packageName ?: rootNode.packageName?.toString(),
            windowTitle = windowTitle,
            nodes = extractedNodes,
            isValid = true
        )
    }

    private fun flattenAndRecycleNodes(
        node: AccessibilityNodeInfo,
        parentId: String?,
        outputList: MutableList<AccessibilityNodeModel>
    ) {
        val currentId = node.windowId.toString() + "_" + System.identityHashCode(node)
        val childIds = mutableListOf<String>()

        val childCount = node.childCount
        for (i in 0 until childCount) {
            val child = node.getChild(i)
            if (child != null) {
                val childId = child.windowId.toString() + "_" + System.identityHashCode(child)
                childIds.add(childId)
                flattenAndRecycleNodes(child, currentId, outputList)
            }
        }

        val bounds = android.graphics.Rect()
        node.getBoundsInScreen(bounds)

        val model = AccessibilityNodeModel(
            nodeId = currentId,
            text = node.text?.toString(),
            contentDescription = node.contentDescription?.toString(),
            viewIdResourceName = node.viewIdResourceName,
            className = node.className?.toString(),
            packageName = node.packageName?.toString(),
            boundsInScreen = bounds.toShortString(),
            isClickable = node.isClickable,
            isLongClickable = node.isLongClickable,
            isEditable = node.isEditable,
            isScrollable = node.isScrollable,
            parentNodeId = parentId,
            childNodeIds = childIds
        )

        outputList.add(model)
        node.recycle() // Explicitly recycle node to prevent native heap leaks
    }
}
