package com.gurkha.hr.components.textField

import org.jetbrains.compose.resources.StringResource

data class ErrorStatus(
    val isError: Boolean,
    val errorMsg: StringResource? = null,
)
