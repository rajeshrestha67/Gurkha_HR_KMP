package com.gurkha.hr.logger.remote

import com.gurkha.hr.logger.LogEntry

class FirebaseLogger : RemoteLogger {
    override suspend fun sendLogs(logs: List<LogEntry>) {
        println("logs $logs")
    }
}