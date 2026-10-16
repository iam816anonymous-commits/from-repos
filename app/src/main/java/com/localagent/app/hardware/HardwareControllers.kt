package com.localagent.app.hardware

import android.content.Context
import android.hardware.camera2.CameraManager
import android.media.AudioManager
import android.os.Build
import android.provider.Settings
import com.localagent.core.capability.CapabilityRegistry
import com.localagent.core.result.ExecutionResult
import com.localagent.core.result.ResultCode

class HardwareControllers(
    private val context: Context,
    private val capabilityRegistry: CapabilityRegistry
) {

    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as? AudioManager
    private val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as? CameraManager

    fun adjustVolume(direction: String): ExecutionResult {
        if (audioManager == null) {
            return ExecutionResult(
                resultCode = ResultCode.UNSUPPORTED,
                message = "AudioManager unavailable"
            )
        }

        val flag = when (direction.uppercase()) {
            "UP" -> AudioManager.ADJUST_RAISE
            "DOWN" -> AudioManager.ADJUST_LOWER
            "MUTE" -> AudioManager.ADJUST_MUTE
            "UNMUTE" -> AudioManager.ADJUST_UNMUTE
            else -> return ExecutionResult(
                resultCode = ResultCode.INVALID_COMMAND,
                message = "Invalid volume direction $direction"
            )
        }

        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, flag, AudioManager.FLAG_SHOW_UI)
        return ExecutionResult(
            resultCode = ResultCode.SUCCESS,
            message = "Adjusted volume $direction"
        )
    }

    fun setScreenBrightness(brightnessValue: Int): ExecutionResult {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.System.canWrite(context)) {
            return ExecutionResult(
                resultCode = ResultCode.PERMISSION_REQUIRED,
                message = "WRITE_SETTINGS permission required to set brightness"
            )
        }

        val clamped = brightnessValue.coerceIn(0, 255)
        try {
            Settings.System.putInt(
                context.contentResolver,
                Settings.System.SCREEN_BRIGHTNESS,
                clamped
            )
            return ExecutionResult(
                resultCode = ResultCode.SUCCESS,
                message = "Set screen brightness to $clamped"
            )
        } catch (e: Exception) {
            return ExecutionResult(
                resultCode = ResultCode.EXECUTION_FAILED,
                message = "Failed to set screen brightness: ${e.message}"
            )
        }
    }

    fun setTorchMode(enable: Boolean): ExecutionResult {
        if (cameraManager == null) {
            return ExecutionResult(
                resultCode = ResultCode.UNSUPPORTED,
                message = "CameraManager unavailable"
            )
        }

        try {
            val cameraId = cameraManager.cameraIdList.firstOrNull()
                ?: return ExecutionResult(
                    resultCode = ResultCode.UNSUPPORTED,
                    message = "No camera hardware present"
                )

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cameraManager.setTorchMode(cameraId, enable)
                return ExecutionResult(
                    resultCode = ResultCode.SUCCESS,
                    message = "Set flashlight torch to $enable"
                )
            } else {
                return ExecutionResult(
                    resultCode = ResultCode.UNSUPPORTED,
                    message = "Torch API requires Android M+"
                )
            }
        } catch (e: Exception) {
            return ExecutionResult(
                resultCode = ResultCode.UNSUPPORTED,
                message = "Torch hardware unsupported or camera permission missing: ${e.message}"
            )
        }
    }
}
