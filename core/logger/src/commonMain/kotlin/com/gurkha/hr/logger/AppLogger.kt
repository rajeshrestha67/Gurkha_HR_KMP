package com.gurkha.hr.logger

import com.gurkha.hr.logger.remote.RemoteLogger
import com.gurkha.model.network.DataError
import com.gurkha.model.network.toException

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

object AppLogger : Logger {

    private var localStorage: LocalLogStorage? = null
    private var ktorRemoteLogger: RemoteLogger? = null
    private var firebaseLogger: RemoteLogger? = null

    var enableRemoteLogging = false
    var enableLocalLogging = false

    fun init(
        localStorage: LocalLogStorage?,
        ktorRemoteLogger: RemoteLogger?,
        firebaseLogger: RemoteLogger?
    ) {
        this.localStorage = localStorage
        this.ktorRemoteLogger = ktorRemoteLogger
        this.firebaseLogger = firebaseLogger
    }

    override fun d(tag: String, message: String) = log("DEBUG", tag, message)
    override fun i(tag: String, message: String) = log("INFO", tag, message)


    override fun w(tag: String, message: String, error: DataError?) =
        log("WARN", tag, message, error)

    override fun e(tag: String, message: String, error: DataError?) =
        log("ERROR", tag, message, error)

    @OptIn(ExperimentalTime::class)
    private fun log(level: String, tag: String, message: String, error: DataError? = null) {
        val timestamp = Clock.System.now().toEpochMilliseconds()
        val throwable = error?.toException()
        val entry = LogEntry(timestamp, level, tag, message, throwable?.stackTraceToString())

        // 1️⃣ Print locally
        println("[$level][$tag] $message ${throwable?.message ?: ""}")

        // 2️⃣ Save locally
        if (enableLocalLogging && localStorage != null) {
            CoroutineScope(Dispatchers.IO).launch {
                localStorage?.appendLog(entry)
            }
        }

        // 3️⃣ Optionally send remotely
        if (enableRemoteLogging) {
            if (ktorRemoteLogger != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    runCatching {
                        ktorRemoteLogger?.sendLogs(listOf(entry))
                    }.onFailure {
                        // Keep local backup if remote upload fails
                        localStorage?.appendLog(entry)
                    }
                }
            }
            if (firebaseLogger != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    runCatching {
                        firebaseLogger?.sendLogs(listOf(entry))
                    }.onFailure {
                        // Keep local backup if remote upload fails
                        localStorage?.appendLog(entry)
                    }
                }
            }
        }
    }
}