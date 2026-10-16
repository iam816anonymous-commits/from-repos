package com.localagent.core.lifecycle

enum class TaskState {
    CREATED,
    VALIDATED,
    RUNNING,
    PAUSED,
    COMPLETED,
    FAILED,
    CANCELLED
}

data class TaskLifecycle(
    val taskId: String,
    val correlationId: String,
    val state: TaskState = TaskState.CREATED,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val failureReason: String? = null
) {
    fun transitionTo(newState: TaskState, reason: String? = null): TaskLifecycle {
        return copy(
            state = newState,
            updatedAt = System.currentTimeMillis(),
            failureReason = reason ?: failureReason
        )
    }
}
