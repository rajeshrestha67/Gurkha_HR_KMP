package com.gurkha.hr.components.textField

import kotlinx.serialization.Serializable

@Serializable
data class DateData(
    val displayValue: String,
    val actualValue: Long
)