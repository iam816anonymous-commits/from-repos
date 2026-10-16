package com.localagent.app.accessibility

import java.util.concurrent.atomic.AtomicBoolean

class AccessibilityServiceConnectionMonitor {
    private val _isConnected = AtomicBoolean(false)

    val isConnected: Boolean
        get() = _isConnected.get()

    fun onServiceConnected() {
        _isConnected.set(true)
    }

    fun onServiceDisconnected() {
        _isConnected.set(false)
    }
}
