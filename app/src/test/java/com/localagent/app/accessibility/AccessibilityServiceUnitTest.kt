package com.localagent.app.accessibility

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AccessibilityServiceUnitTest {

    private lateinit var monitor: AccessibilityServiceConnectionMonitor

    @Before
    fun setup() {
        monitor = AccessibilityServiceConnectionMonitor()
    }

    @Test
    fun testConnectionMonitorLifecycle() {
        assertFalse(monitor.isConnected)

        monitor.onServiceConnected()
        assertTrue(monitor.isConnected)

        monitor.onServiceDisconnected()
        assertFalse(monitor.isConnected)
    }

    @Test
    fun testGenerationCounter() {
        val gen1 = AgentAccessibilityService.getNextGenerationId()
        val gen2 = AgentAccessibilityService.getNextGenerationId()
        assertTrue(gen2 > gen1)
    }
}
