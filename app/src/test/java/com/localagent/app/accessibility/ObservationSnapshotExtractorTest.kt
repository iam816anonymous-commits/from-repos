package com.localagent.app.accessibility

import com.localagent.core.observation.ObservationSnapshot
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class ObservationSnapshotExtractorTest {

    @Test
    fun testNullRootNodeReturnsInvalidSnapshot() {
        val extractor = ObservationSnapshotExtractor()
        val snapshot = extractor.extractSnapshot(
            rootNode = null,
            packageName = "com.android.calculator2",
            windowTitle = "Calculator",
            generationId = 101L
        )

        assertFalse(snapshot.isValid)
        assertEquals(101L, snapshot.generationId)
        assertEquals(0, snapshot.nodes.size)
    }
}
