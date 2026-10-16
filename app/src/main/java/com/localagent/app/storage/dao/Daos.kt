package com.localagent.app.storage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.localagent.app.storage.entity.AgentEventEntity
import com.localagent.app.storage.entity.TaskRecordEntity

@Dao
interface AgentEventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: AgentEventEntity): Long

    @Query("SELECT * FROM agent_events WHERE correlationId = :correlationId ORDER BY timestamp ASC")
    suspend fun getEventsForCorrelation(correlationId: String): List<AgentEventEntity>

    @Query("DELETE FROM agent_events WHERE timestamp < :cutoffTimestamp")
    suspend fun pruneEventsBefore(cutoffTimestamp: Long): Int

    @Query("SELECT COUNT(*) FROM agent_events")
    suspend fun getEventCount(): Int
}

@Dao
interface TaskRecordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateTask(task: TaskRecordEntity)

    @Query("SELECT * FROM task_records WHERE taskId = :taskId")
    suspend fun getTaskById(taskId: String): TaskRecordEntity?

    @Query("SELECT * FROM task_records ORDER BY createdAt DESC")
    suspend fun getAllTasks(): List<TaskRecordEntity>
}
