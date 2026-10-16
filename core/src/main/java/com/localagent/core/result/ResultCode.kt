package com.localagent.core.result

enum class ResultCode {
    SUCCESS,
    INVALID_COMMAND,
    POLICY_DENIED,
    PERMISSION_REQUIRED,
    TARGET_NOT_FOUND,
    AMBIGUOUS_TARGET,
    STALE_OBSERVATION,
    UNSUPPORTED,
    TIMEOUT,
    EXECUTION_FAILED,
    CANCELLED
}

data class ExecutionResult(
    val resultCode: ResultCode,
    val isSuccess: Boolean = resultCode == ResultCode.SUCCESS,
    val message: String,
    val preGenerationId: Long = -1L,
    val postGenerationId: Long = -1L,
    val details: Map<String, String> = emptyMap()
)
