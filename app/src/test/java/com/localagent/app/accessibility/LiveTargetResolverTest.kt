package com.localagent.app.accessibility

import com.localagent.core.observation.ObservationSnapshot
import com.localagent.core.resolver.TargetResolver
import org.junit.Assert.assertNull
import org.junit.Test

class LiveTargetResolverTest {

    @Test
    fun testNullRootReturnsNull() {
        val resolver = LiveTargetResolver(TargetResolver())
        val snapshot = ObservationSnapshot("s1", 100L, "com.app", nodes = emptyList(), isValid = false)

        val result = resolver.resolveAndFindNode(null, snapshot, "7")
        assertNull(result)
    }
}
