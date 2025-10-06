package com.gurkha.hr.logger

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
fun setupLogger(platform: String) {
    val localLogStorage = LocalLogStorage()
    AppLogger.enableLocalLogging = true
    AppLogger.init(localLogStorage, ktorRemoteLogger = null, firebaseLogger = null)
    CoroutineScope(Dispatchers.Default).launch {
        localLogStorage.appendLog(
            LogEntry(
                timestamp = Clock.System.now().toEpochMilliseconds(),
                level = "INFO",
                tag = "Startup",
                message = "$platform App launched successfully"
            )
        )
    }
}