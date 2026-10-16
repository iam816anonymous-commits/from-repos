package com.localagent.app.storage

import android.content.Context
import androidx.room.Room
import com.localagent.core.lifecycle.TaskLifecycle
import com.localagent.app.logging.UnifiedEventLogger
import com.localagent.app.storage.entity.TaskRecordEntity

class DurableMemoryStorageManager(
    context: Context
) {
    val database: AgentDatabase = Room.inMemoryDatabaseBuilder(
        context.applicationContext,
        AgentDatabase::class.java
    ).allowMainThreadQueries().build()

    val eventLogger: UnifiedEventLogger = UnifiedEventLogger(database.agentEventDao())

    suspend fun saveTaskLifecycle(lifecycle: TaskLifecycle, rawCommand: String, intentType: String) {
        val entity = TaskRecordEntity(
            taskId = lifecycle.taskId,
            correlationId = lifecycle.correlationId,
            rawCommand = rawCommand,
            intentType = intentType,
            state = lifecycle.state.name,
            createdAt = lifecycle.createdAt,
            updatedAt = lifecycle.updatedAt,
            failureReason = lifecycle.failureReason
        )
        database.taskRecordDao().insertOrUpdateTask(entity)
    }

    suspend fun getTask(taskId: String): TaskRecordEntity? {
        return database.taskRecordDao().getTaskById(taskId)
    }
}
