package com.localagent.app.accessibility

import com.localagent.core.result.ResultCode
import org.junit.Assert.assertEquals
import org.junit.Test

class ActionExecutorsTest {

    @Test
    fun testGlobalActionExecutorWhenServiceNull() {
        val executor = GlobalActionExecutor(null)
        val result = executor.executeGlobalAction("BACK")
        assertEquals(ResultCode.PERMISSION_REQUIRED, result.resultCode)
    }
}
