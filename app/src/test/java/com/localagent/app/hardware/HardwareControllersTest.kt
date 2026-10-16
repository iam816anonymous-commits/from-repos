package com.localagent.app.hardware

import androidx.test.core.app.ApplicationProvider
import com.localagent.core.capability.CapabilityRegistry
import com.localagent.core.result.ResultCode
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [27])
class HardwareControllersTest {

    private lateinit var registry: CapabilityRegistry
    private lateinit var hardwareControllers: HardwareControllers

    @Before
    fun setup() {
        registry = CapabilityRegistry()
        hardwareControllers = HardwareControllers(
            ApplicationProvider.getApplicationContext(),
            registry
        )
    }

    @Test
    fun testVolumeAdjustment() {
        val resultUp = hardwareControllers.adjustVolume("UP")
        assertEquals(ResultCode.SUCCESS, resultUp.resultCode)

        val resultInvalid = hardwareControllers.adjustVolume("INVALID")
        assertEquals(ResultCode.INVALID_COMMAND, resultInvalid.resultCode)
    }
}
