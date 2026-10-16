package com.localagent.app.accessibility

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import java.util.concurrent.atomic.AtomicLong

class AgentAccessibilityService : AccessibilityService() {

    companion object {
        var instance: AgentAccessibilityService? = null
            private set

        val connectionMonitor = AccessibilityServiceConnectionMonitor()
        private val generationCounter = AtomicLong(1000L)

        fun getNextGenerationId(): Long = generationCounter.incrementAndGet()
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        instance = this
        connectionMonitor.onServiceConnected()
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // Passive observation event handling
    }

    override fun onInterrupt() {
        // Interrupted
    }

    override fun onDestroy() {
        connectionMonitor.onServiceDisconnected()
        instance = null
        super.onDestroy()
    }
}
