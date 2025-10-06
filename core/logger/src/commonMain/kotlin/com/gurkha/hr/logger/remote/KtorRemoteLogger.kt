package com.gurkha.hr.logger.remote

import com.gurkha.hr.logger.LogEntry

class KtorRemoteLogger : RemoteLogger {
    override suspend fun sendLogs(logs: List<LogEntry>) {
        println("logs $logs")
    }
}