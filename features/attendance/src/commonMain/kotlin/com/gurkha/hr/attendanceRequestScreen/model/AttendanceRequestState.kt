package com.gurkha.hr.attendanceRequestScreen.model

import com.gurkha.hr.components.textField.DateData
import com.gurkha.hr.res.SharedRes
import org.jetbrains.compose.resources.StringResource

data class AttendanceRequestState(
    val date : DateData? = null,
    val dateError : StringResource? = SharedRes.Strings.required
)