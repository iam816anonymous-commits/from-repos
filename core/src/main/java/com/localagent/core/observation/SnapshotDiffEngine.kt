package com.localagent.core.observation

data class SnapshotDiffResult(
    val generationDiff: Long,
    val packageChanged: Boolean,
    val nodesAddedCount: Int,
    val nodesRemovedCount: Int,
    val hasSemanticChange: Boolean
)

class SnapshotDiffEngine {

    fun computeDiff(preSnapshot: ObservationSnapshot, postSnapshot: ObservationSnapshot): SnapshotDiffResult {
        if (!preSnapshot.isValid || !postSnapshot.isValid) {
            return SnapshotDiffResult(
                generationDiff = postSnapshot.generationId - preSnapshot.generationId,
                packageChanged = preSnapshot.packageName != postSnapshot.packageName,
                nodesAddedCount = 0,
                nodesRemovedCount = 0,
                hasSemanticChange = false
            )
        }

        val preMap = preSnapshot.nodes.associateBy { it.nodeId }
        val postMap = postSnapshot.nodes.associateBy { it.nodeId }

        val preNodeIds = preMap.keys
        val postNodeIds = postMap.keys

        val added = postNodeIds - preNodeIds
        val removed = preNodeIds - postNodeIds

        val common = preNodeIds.intersect(postNodeIds)
        val contentChanged = common.any { id ->
            val preNode = preMap[id]
            val postNode = postMap[id]
            preNode?.text != postNode?.text ||
            preNode?.contentDescription != postNode?.contentDescription
        }

        val packageChanged = preSnapshot.packageName != postSnapshot.packageName
        val hasSemanticChange = packageChanged || added.isNotEmpty() || removed.isNotEmpty() || contentChanged

        return SnapshotDiffResult(
            generationDiff = postSnapshot.generationId - preSnapshot.generationId,
            packageChanged = packageChanged,
            nodesAddedCount = added.size,
            nodesRemovedCount = removed.size,
            hasSemanticChange = hasSemanticChange
        )
    }
}
