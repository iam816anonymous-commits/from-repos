package com.localagent.core.policy

import com.localagent.core.capability.CapabilityName
import com.localagent.core.capability.CapabilityRegistry
import com.localagent.core.capability.CapabilityStatus
import com.localagent.core.command.IntentType
import com.localagent.core.command.NormalizedCommand
import com.localagent.core.result.ExecutionResult
import com.localagent.core.result.ResultCode

class ActionPolicyEngine(
    private val capabilityRegistry: CapabilityRegistry
) {

    fun evaluate(command: NormalizedCommand): ExecutionResult {
        if (command.intentType == IntentType.UNKNOWN) {
            return ExecutionResult(
                resultCode = ResultCode.INVALID_COMMAND,
                message = "Unknown or unparseable command"
            )
        }

        val requiredCapability = mapIntentToCapability(command.intentType, command.parameters)

        if (requiredCapability != null) {
            val state = capabilityRegistry.getCapabilityState(requiredCapability)
            when (state.status) {
                CapabilityStatus.UNSUPPORTED -> {
                    return ExecutionResult(
                        resultCode = ResultCode.UNSUPPORTED,
                        message = "Capability $requiredCapability is unsupported on this device"
                    )
                }
                CapabilityStatus.PERMISSION_REQUIRED -> {
                    return ExecutionResult(
                        resultCode = ResultCode.PERMISSION_REQUIRED,
                        message = "Permission required for capability $requiredCapability (${state.requiredPermission ?: "Unknown"})"
                    )
                }
                CapabilityStatus.DEGRADED -> {
                    // Allowed in degraded mode
                }
                CapabilityStatus.AVAILABLE -> {
                    // Allowed
                }
            }
        }

        return ExecutionResult(
            resultCode = ResultCode.SUCCESS,
            message = "Command policy evaluation passed"
        )
    }

    private fun mapIntentToCapability(
        intentType: IntentType,
        parameters: Map<String, String>
    ): CapabilityName? {
        return when (intentType) {
            IntentType.CLICK_TARGET,
            IntentType.LONG_CLICK_TARGET,
            IntentType.INPUT_TEXT,
            IntentType.SCROLL_FORWARD,
            IntentType.SCROLL_BACKWARD,
            IntentType.GLOBAL_NAVIGATION -> CapabilityName.ACCESSIBILITY_AUTOMATION
            IntentType.OPEN_APP -> CapabilityName.APP_CONTROL
            IntentType.HARDWARE_CONTROL -> {
                when (parameters["control"]) {
                    "VOLUME" -> CapabilityName.AUDIO_CONTROL
                    "BRIGHTNESS" -> CapabilityName.SYSTEM_SETTINGS
                    "TORCH" -> CapabilityName.CAMERA_FLASH
                    "WIFI" -> CapabilityName.NETWORK_CONTROL
                    else -> null
                }
            }
            else -> null
        }
    }
}
