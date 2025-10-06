package com.gurkha.hr.logger.remote

import com.gurkha.hr.logger.LogEntry

interface RemoteLogger {
    suspend fun sendLogs(logs: List<LogEntry>)
}