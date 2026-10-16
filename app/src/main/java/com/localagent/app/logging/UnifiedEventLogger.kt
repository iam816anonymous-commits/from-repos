package com.localagent.app.logging

import com.localagent.app.storage.dao.AgentEventDao
import com.localagent.app.storage.entity.AgentEventEntity
import java.util.UUID

class UnifiedEventLogger(
    private val eventDao: AgentEventDao
) {
    suspend fun logEvent(
        correlationId: String,
        taskId: String?,
        eventType: String,
        payloadJson: String,
        riskLevel: String = "SAFE"
    ): String {
        val eventId = UUID.randomUUID().toString()
        val entity = AgentEventEntity(
            eventId = eventId,
            correlationId = correlationId,
            taskId = taskId,
            eventType = eventType,
            timestamp = System.currentTimeMillis(),
            payloadJson = payloadJson,
            riskLevel = riskLevel
        )
        eventDao.insertEvent(entity)
        return eventId
    }

    suspend fun getTraceForCorrelation(correlationId: String): List<AgentEventEntity> {
        return eventDao.getEventsForCorrelation(correlationId)
    }

    suspend fun pruneOldLogs(retentionWindowMs: Long): Int {
        val cutoff = System.currentTimeMillis() - retentionWindowMs
        return eventDao.pruneEventsBefore(cutoff)
    }
}
