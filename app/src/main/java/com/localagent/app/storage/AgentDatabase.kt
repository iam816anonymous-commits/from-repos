package com.localagent.app.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import com.localagent.app.storage.dao.AgentEventDao
import com.localagent.app.storage.dao.TaskRecordDao
import com.localagent.app.storage.entity.AgentEventEntity
import com.localagent.app.storage.entity.TaskRecordEntity

@Database(
    entities = [AgentEventEntity::class, TaskRecordEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AgentDatabase : RoomDatabase() {
    abstract fun agentEventDao(): AgentEventDao
    abstract fun taskRecordDao(): TaskRecordDao
}
