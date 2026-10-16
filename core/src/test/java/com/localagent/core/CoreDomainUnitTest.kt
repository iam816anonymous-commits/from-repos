package com.localagent.core

import com.localagent.core.command.CommandNormalizer
import com.localagent.core.command.IntentType
import com.localagent.core.observation.AccessibilityNodeModel
import com.localagent.core.observation.ObservationSnapshot
import com.localagent.core.observation.SnapshotDiffEngine
import com.localagent.core.resolver.TargetResolver
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CoreDomainUnitTest {

    @Test
    fun testCommandNormalization() {
        val normalizer = CommandNormalizer()

        val appCommand = normalizer.normalize("Open Calculator")
        assertEquals(IntentType.OPEN_APP, appCommand.intentType)
        assertEquals("Calculator", appCommand.parameters["appName"])

        val clickCommand = normalizer.normalize("Click 7")
        assertEquals(IntentType.CLICK_TARGET, clickCommand.intentType)
        assertEquals("7", clickCommand.targetSelector)
    }

    @Test
    fun testTargetResolverAndAncestorTraversal() {
        val childNode = AccessibilityNodeModel(
            nodeId = "child_1",
            text = "7",
            isClickable = false,
            parentNodeId = "parent_1"
        )
        val parentNode = AccessibilityNodeModel(
            nodeId = "parent_1",
            isClickable = true,
            childNodeIds = listOf("child_1")
        )

        val snapshot = ObservationSnapshot(
            snapshotId = "s1",
            generationId = 100L,
            packageName = "com.android.calculator2",
            nodes = listOf(childNode, parentNode)
        )

        val resolver = TargetResolver()
        val match = resolver.resolveTarget(snapshot, "7")

        assertNotNull(match)
        assertEquals("parent_1", match!!.node.nodeId)
        assertTrue(match.isActionableAncestor)
    }

    @Test
    fun testSnapshotDiffEngine() {
        val preSnapshot = ObservationSnapshot(
            snapshotId = "pre",
            generationId = 100L,
            packageName = "com.android.calculator2",
            nodes = listOf(
                AccessibilityNodeModel(nodeId = "1", text = "0")
            )
        )

        val postSnapshot = ObservationSnapshot(
            snapshotId = "post",
            generationId = 101L,
            packageName = "com.android.calculator2",
            nodes = listOf(
                AccessibilityNodeModel(nodeId = "1", text = "7")
            )
        )

        val diffEngine = SnapshotDiffEngine()
        val diff = diffEngine.computeDiff(preSnapshot, postSnapshot)

        assertEquals(1L, diff.generationDiff)
        assertTrue(diff.hasSemanticChange)
    }
}
