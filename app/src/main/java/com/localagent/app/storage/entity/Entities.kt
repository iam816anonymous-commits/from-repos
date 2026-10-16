package com.localagent.app.storage.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "agent_events")
data class AgentEventEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val eventId: String,
    val correlationId: String,
    val taskId: String?,
    val eventType: String,
    val timestamp: Long,
    val payloadJson: String,
    val riskLevel: String
)

@Entity(tableName = "task_records")
data class TaskRecordEntity(
    @PrimaryKey val taskId: String,
    val correlationId: String,
    val rawCommand: String,
    val intentType: String,
    val state: String,
    val createdAt: Long,
    val updatedAt: Long,
    val failureReason: String?
)
