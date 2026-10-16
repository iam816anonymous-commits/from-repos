package com.localagent.app.system

import androidx.test.core.app.ApplicationProvider
import com.localagent.core.observation.ObservationSnapshot
import com.localagent.core.result.ResultCode
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [27])
class AppControlEngineTest {

    private lateinit var appControlEngine: AppControlEngine

    @Before
    fun setup() {
        appControlEngine = AppControlEngine(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun testResolveKnownPackages() {
        val calcPkg = appControlEngine.resolvePackageName("Calculator")
        assertEquals("com.android.calculator2", calcPkg)

        val settingsPkg = appControlEngine.resolvePackageName("Settings")
        assertEquals("com.android.settings", settingsPkg)
    }

    @Test
    fun testLaunchAndVerifyForeground() = runBlocking {
        val preSnapshot = ObservationSnapshot("pre", 100L, "com.android.launcher", isValid = true)

        var callCount = 0
        val snapshotFetcher = {
            callCount++
            ObservationSnapshot("post", 101L, "com.android.calculator2", isValid = true)
        }

        val result = appControlEngine.launchAppAndVerifyForeground(
            appQuery = "Calculator",
            preSnapshot = preSnapshot,
            snapshotFetcher = snapshotFetcher,
            timeoutMs = 1000L
        )

        assertEquals(ResultCode.SUCCESS, result.resultCode)
        assertEquals(100L, result.preGenerationId)
        assertEquals(101L, result.postGenerationId)
    }
}
