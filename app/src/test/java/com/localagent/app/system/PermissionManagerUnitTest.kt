package com.localagent.app.system

import androidx.test.core.app.ApplicationProvider
import com.localagent.core.capability.CapabilityName
import com.localagent.core.capability.CapabilityRegistry
import com.localagent.core.capability.CapabilityStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [27])
class PermissionManagerUnitTest {

    private lateinit var registry: CapabilityRegistry
    private lateinit var permissionManager: PermissionManager

    @Before
    fun setup() {
        registry = CapabilityRegistry()
        permissionManager = PermissionManager(
            ApplicationProvider.getApplicationContext(),
            registry
        )
    }

    @Test
    fun testRevalidateCapabilitiesWhenAccessibilityUnbound() {
        permissionManager.revalidateCapabilities(isAccessibilityBound = false)

        val accessibilityState = registry.getCapabilityState(CapabilityName.ACCESSIBILITY_AUTOMATION)
        assertEquals(CapabilityStatus.PERMISSION_REQUIRED, accessibilityState.status)

        val appControlState = registry.getCapabilityState(CapabilityName.APP_CONTROL)
        assertEquals(CapabilityStatus.AVAILABLE, appControlState.status)

        val audioState = registry.getCapabilityState(CapabilityName.AUDIO_CONTROL)
        assertEquals(CapabilityStatus.AVAILABLE, audioState.status)
    }

    @Test
    fun testRevalidateCapabilitiesWhenAccessibilityBound() {
        permissionManager.revalidateCapabilities(isAccessibilityBound = true)

        val accessibilityState = registry.getCapabilityState(CapabilityName.ACCESSIBILITY_AUTOMATION)
        assertEquals(CapabilityStatus.AVAILABLE, accessibilityState.status)
    }

    @Test
    fun testSettingsIntents() {
        val intent = permissionManager.getAccessibilitySettingsIntent()
        assertNotNull(intent)
        assertEquals("android.settings.ACCESSIBILITY_SETTINGS", intent.action)
    }
}
