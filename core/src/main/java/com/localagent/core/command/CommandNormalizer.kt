package com.localagent.core.command

import java.util.UUID

enum class IntentType {
    OPEN_APP,
    CLICK_TARGET,
    LONG_CLICK_TARGET,
    INPUT_TEXT,
    SCROLL_FORWARD,
    SCROLL_BACKWARD,
    GLOBAL_NAVIGATION,
    HARDWARE_CONTROL,
    UNKNOWN
}

enum class RiskClassification {
    SAFE,
    MODERATE,
    HIGH_RISK
}

data class NormalizedCommand(
    val commandId: String,
    val rawCommand: String,
    val intentType: IntentType,
    val targetSelector: String? = null,
    val textValue: String? = null,
    val parameters: Map<String, String> = emptyMap(),
    val riskClassification: RiskClassification = RiskClassification.SAFE,
    val timestamp: Long = System.currentTimeMillis()
)

class CommandNormalizer {

    fun normalize(rawInput: String): NormalizedCommand {
        val trimmed = rawInput.trim()
        if (trimmed.isEmpty()) {
            return NormalizedCommand(
                commandId = UUID.randomUUID().toString(),
                rawCommand = rawInput,
                intentType = IntentType.UNKNOWN,
                riskClassification = RiskClassification.SAFE
            )
        }

        val lower = trimmed.lowercase()

        return when {
            lower.startsWith("open ") -> {
                val appName = trimmed.substring(5).trim()
                NormalizedCommand(
                    commandId = UUID.randomUUID().toString(),
                    rawCommand = rawInput,
                    intentType = IntentType.OPEN_APP,
                    parameters = mapOf("appName" to appName),
                    riskClassification = RiskClassification.SAFE
                )
            }
            lower.startsWith("click ") -> {
                val target = trimmed.substring(6).trim()
                NormalizedCommand(
                    commandId = UUID.randomUUID().toString(),
                    rawCommand = rawInput,
                    intentType = IntentType.CLICK_TARGET,
                    targetSelector = target,
                    riskClassification = RiskClassification.SAFE
                )
            }
            lower.startsWith("long click ") -> {
                val target = trimmed.substring(11).trim()
                NormalizedCommand(
                    commandId = UUID.randomUUID().toString(),
                    rawCommand = rawInput,
                    intentType = IntentType.LONG_CLICK_TARGET,
                    targetSelector = target,
                    riskClassification = RiskClassification.SAFE
                )
            }
            lower.startsWith("type ") -> {
                val text = trimmed.substring(5).trim()
                NormalizedCommand(
                    commandId = UUID.randomUUID().toString(),
                    rawCommand = rawInput,
                    intentType = IntentType.INPUT_TEXT,
                    textValue = text,
                    riskClassification = RiskClassification.MODERATE
                )
            }
            lower == "back" || lower == "home" || lower == "recents" -> {
                NormalizedCommand(
                    commandId = UUID.randomUUID().toString(),
                    rawCommand = rawInput,
                    intentType = IntentType.GLOBAL_NAVIGATION,
                    parameters = mapOf("action" to lower.uppercase()),
                    riskClassification = RiskClassification.SAFE
                )
            }
            lower.startsWith("volume ") -> {
                val valStr = trimmed.substring(7).trim()
                NormalizedCommand(
                    commandId = UUID.randomUUID().toString(),
                    rawCommand = rawInput,
                    intentType = IntentType.HARDWARE_CONTROL,
                    parameters = mapOf("control" to "VOLUME", "value" to valStr),
                    riskClassification = RiskClassification.SAFE
                )
            }
            lower.startsWith("brightness ") -> {
                val valStr = trimmed.substring(11).trim()
                NormalizedCommand(
                    commandId = UUID.randomUUID().toString(),
                    rawCommand = rawInput,
                    intentType = IntentType.HARDWARE_CONTROL,
                    parameters = mapOf("control" to "BRIGHTNESS", "value" to valStr),
                    riskClassification = RiskClassification.MODERATE
                )
            }
            else -> {
                NormalizedCommand(
                    commandId = UUID.randomUUID().toString(),
                    rawCommand = rawInput,
                    intentType = IntentType.UNKNOWN,
                    riskClassification = RiskClassification.SAFE
                )
            }
        }
    }
}
