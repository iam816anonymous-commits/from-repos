package com.localagent.core.capability

enum class CapabilityName {
    ACCESSIBILITY_AUTOMATION,
    APP_CONTROL,
    SYSTEM_SETTINGS,
    AUDIO_CONTROL,
    CAMERA_FLASH,
    NETWORK_CONTROL
}

enum class CapabilityStatus {
    AVAILABLE,
    PERMISSION_REQUIRED,
    UNSUPPORTED,
    DEGRADED
}

data class CapabilityState(
    val capability: CapabilityName,
    val status: CapabilityStatus,
    val requiredPermission: String? = null,
    val details: String = ""
)

class CapabilityRegistry {
    private val capabilities = mutableMapOf<CapabilityName, CapabilityState>()

    fun registerCapability(state: CapabilityState) {
        capabilities[state.capability] = state
    }

    fun getCapabilityState(capability: CapabilityName): CapabilityState {
        return capabilities[capability] ?: CapabilityState(
            capability = capability,
            status = CapabilityStatus.UNSUPPORTED,
            details = "Unregistered capability"
        )
    }

    fun isAvailable(capability: CapabilityName): Boolean {
        return getCapabilityState(capability).status == CapabilityStatus.AVAILABLE
    }
}
