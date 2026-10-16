package com.localagent.app.storage

import androidx.test.core.app.ApplicationProvider
import com.localagent.core.lifecycle.TaskLifecycle
import com.localagent.core.lifecycle.TaskState
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
class PersistentStorageUnitTest {

    private lateinit var storageManager: DurableMemoryStorageManager

    @Before
    fun setup() {
        storageManager = DurableMemoryStorageManager(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun testTaskLifecyclePersistenceAndEventLogging() = runBlocking {
        val lifecycle = TaskLifecycle(
            taskId = "task_123",
            correlationId = "corr_456",
            state = TaskState.CREATED
        )

        storageManager.saveTaskLifecycle(lifecycle, rawCommand = "Open Settings", intentType = "OPEN_APP")

        val retrieved = storageManager.getTask("task_123")
        assertNotNull(retrieved)
        assertEquals("task_123", retrieved!!.taskId)
        assertEquals("corr_456", retrieved.correlationId)
        assertEquals("OPEN_APP", retrieved.intentType)

        // Log Event
        storageManager.eventLogger.logEvent(
            correlationId = "corr_456",
            taskId = "task_123",
            eventType = "TASK_CREATED",
            payloadJson = "{\"status\":\"success\"}"
        )

        val trace = storageManager.eventLogger.getTraceForCorrelation("corr_456")
        assertEquals(1, trace.size)
        assertEquals("TASK_CREATED", trace[0].eventType)
    }
}
