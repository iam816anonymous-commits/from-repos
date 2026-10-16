package com.localagent.app.system

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.content.ContextCompat
import com.localagent.core.capability.CapabilityName
import com.localagent.core.capability.CapabilityRegistry
import com.localagent.core.capability.CapabilityState
import com.localagent.core.capability.CapabilityStatus

class PermissionManager(
    private val context: Context,
    private val capabilityRegistry: CapabilityRegistry
) {

    fun revalidateCapabilities(isAccessibilityBound: Boolean = false) {
        // Accessibility Automation
        val accessibilityStatus = if (isAccessibilityBound) CapabilityStatus.AVAILABLE else CapabilityStatus.PERMISSION_REQUIRED
        capabilityRegistry.registerCapability(
            CapabilityState(
                capability = CapabilityName.ACCESSIBILITY_AUTOMATION,
                status = accessibilityStatus,
                requiredPermission = "android.permission.BIND_ACCESSIBILITY_SERVICE",
                details = if (isAccessibilityBound) "Bound" else "Accessibility service not bound"
            )
        )

        // App Control
        capabilityRegistry.registerCapability(
            CapabilityState(
                capability = CapabilityName.APP_CONTROL,
                status = CapabilityStatus.AVAILABLE,
                details = "App launcher available"
            )
        )

        // Audio Control
        capabilityRegistry.registerCapability(
            CapabilityState(
                capability = CapabilityName.AUDIO_CONTROL,
                status = CapabilityStatus.AVAILABLE,
                details = "AudioManager available"
            )
        )

        // System Settings (Write Settings for Brightness)
        val canWriteSettings = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.System.canWrite(context)
        } else {
            true
        }
        capabilityRegistry.registerCapability(
            CapabilityState(
                capability = CapabilityName.SYSTEM_SETTINGS,
                status = if (canWriteSettings) CapabilityStatus.AVAILABLE else CapabilityStatus.PERMISSION_REQUIRED,
                requiredPermission = "android.permission.WRITE_SETTINGS",
                details = if (canWriteSettings) "Write settings granted" else "Write settings permission required"
            )
        )

        // Camera Flash
        val hasFlash = context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
        val cameraGranted = ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        val torchStatus = when {
            !hasFlash -> CapabilityStatus.UNSUPPORTED
            !cameraGranted -> CapabilityStatus.PERMISSION_REQUIRED
            else -> CapabilityStatus.AVAILABLE
        }
        capabilityRegistry.registerCapability(
            CapabilityState(
                capability = CapabilityName.CAMERA_FLASH,
                status = torchStatus,
                requiredPermission = android.Manifest.permission.CAMERA,
                details = if (!hasFlash) "No flash hardware" else if (!cameraGranted) "Camera permission required" else "Torch available"
            )
        )

        // Network Control (Wi-Fi)
        val wifiStatus = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            CapabilityStatus.DEGRADED // API 29+ panel intent fallback
        } else {
            CapabilityStatus.AVAILABLE
        }
        capabilityRegistry.registerCapability(
            CapabilityState(
                capability = CapabilityName.NETWORK_CONTROL,
                status = wifiStatus,
                details = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) "Panel intent fallback" else "Direct Wi-Fi toggle permitted"
            )
        )
    }

    fun getAccessibilitySettingsIntent(): Intent {
        return Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
    }

    fun getWriteSettingsIntent(): Intent {
        return Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS).apply {
            data = Uri.parse("package:${context.packageName}")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
    }

    fun getAppDetailsIntent(): Intent {
        return Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.parse("package:${context.packageName}")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
    }
}
