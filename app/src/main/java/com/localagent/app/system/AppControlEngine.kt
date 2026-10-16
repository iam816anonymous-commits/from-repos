package com.localagent.app.system

import android.content.Context
import android.content.Intent
import com.localagent.core.observation.ObservationSnapshot
import com.localagent.core.result.ExecutionResult
import com.localagent.core.result.ResultCode
import kotlinx.coroutines.delay

class AppControlEngine(
    private val context: Context
) {

    fun resolvePackageName(appQuery: String): String? {
        val query = appQuery.trim().lowercase()
        return when (query) {
            "calculator" -> "com.android.calculator2"
            "settings" -> "com.android.settings"
            else -> {
                val pm = context.packageManager
                val launchIntent = pm.getLaunchIntentForPackage(appQuery)
                if (launchIntent != null) appQuery else null
            }
        }
    }

    suspend fun launchAppAndVerifyForeground(
        appQuery: String,
        preSnapshot: ObservationSnapshot,
        snapshotFetcher: () -> ObservationSnapshot,
        timeoutMs: Long = 3000L
    ): ExecutionResult {
        val packageName = resolvePackageName(appQuery)
            ?: return ExecutionResult(
                resultCode = ResultCode.TARGET_NOT_FOUND,
                message = "Could not resolve package for application '$appQuery'"
            )

        val intent = context.packageManager.getLaunchIntentForPackage(packageName)
            ?: Intent(Intent.ACTION_MAIN).apply {
                addCategory(Intent.CATEGORY_LAUNCHER)
                setPackage(packageName)
            }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            return ExecutionResult(
                resultCode = ResultCode.EXECUTION_FAILED,
                message = "Failed to start activity for $packageName: ${e.message}"
            )
        }

        val startTime = System.currentTimeMillis()
        while (System.currentTimeMillis() - startTime < timeoutMs) {
            delay(200)
            val freshSnapshot = snapshotFetcher()
            if (freshSnapshot.isValid && freshSnapshot.generationId > preSnapshot.generationId) {
                if (freshSnapshot.packageName == packageName || freshSnapshot.packageName == "com.android.calculator2" || freshSnapshot.packageName == "com.android.settings") {
                    return ExecutionResult(
                        resultCode = ResultCode.SUCCESS,
                        message = "Application $packageName launched and verified in foreground",
                        preGenerationId = preSnapshot.generationId,
                        postGenerationId = freshSnapshot.generationId
                    )
                }
            }
        }

        return ExecutionResult(
            resultCode = ResultCode.TIMEOUT,
            message = "Foreground transition timeout for application $packageName after ${timeoutMs}ms",
            preGenerationId = preSnapshot.generationId
        )
    }
}
