package com.localagent.core.action

enum class ActionType {
    GLOBAL_BACK,
    GLOBAL_HOME,
    GLOBAL_RECENTS,
    UI_CLICK,
    UI_LONG_CLICK,
    UI_TEXT_INPUT,
    UI_SCROLL_FORWARD,
    UI_SCROLL_BACKWARD,
    APP_LAUNCH,
    HARDWARE_CONTROL
}

data class ActionRequest(
    val actionId: String,
    val correlationId: String,
    val actionType: ActionType,
    val targetSelector: String? = null,
    val textValue: String? = null,
    val parameters: Map<String, String> = emptyMap()
)
