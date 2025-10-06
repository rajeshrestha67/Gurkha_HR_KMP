package com.gurkha.hr.logger

import kotlinx.serialization.Serializable

@Serializable
data class LogEntry(
    val timestamp: Long,
    val level: String,
    val tag: String,
    val message: String,
    val error: String? = null
)