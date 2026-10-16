package com.localagent.core.execution

import com.localagent.core.command.NormalizedCommand
import com.localagent.core.lifecycle.TaskLifecycle
import com.localagent.core.lifecycle.TaskState
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.PriorityBlockingQueue
import java.util.concurrent.atomic.AtomicBoolean

data class QueuedGoal(
    val taskId: String,
    val correlationId: String,
    val command: NormalizedCommand,
    val priority: Int = 0,
    val enqueuedAt: Long = System.currentTimeMillis()
) : Comparable<QueuedGoal> {
    override fun compareTo(other: QueuedGoal): Int {
        val priorityCompare = other.priority.compareTo(this.priority)
        return if (priorityCompare != 0) priorityCompare else this.enqueuedAt.compareTo(other.enqueuedAt)
    }
}

class GoalDispatcher {
    private val goalQueue = PriorityBlockingQueue<QueuedGoal>()
    private val taskMap = ConcurrentHashMap<String, TaskLifecycle>()
    private val executionLock = AtomicBoolean(false)

    fun submitGoal(command: NormalizedCommand, priority: Int = 0): TaskLifecycle {
        val taskId = UUID.randomUUID().toString()
        val correlationId = UUID.randomUUID().toString()
        val lifecycle = TaskLifecycle(taskId = taskId, correlationId = correlationId)

        taskMap[taskId] = lifecycle
        goalQueue.offer(QueuedGoal(taskId, correlationId, command, priority))
        return lifecycle
    }

    fun pollGoal(): QueuedGoal? {
        return goalQueue.poll()
    }

    fun acquireLock(): Boolean {
        return executionLock.compareAndSet(false, true)
    }

    fun releaseLock() {
        executionLock.set(false)
    }

    fun isLocked(): Boolean {
        return executionLock.get()
    }

    fun updateTaskState(taskId: String, state: TaskState, reason: String? = null): TaskLifecycle? {
        val current = taskMap[taskId] ?: return null
        val updated = current.transitionTo(state, reason)
        taskMap[taskId] = updated
        return updated
    }

    fun getTaskState(taskId: String): TaskLifecycle? {
        return taskMap[taskId]
    }
}
