package com.gurkha.hr.model.attendanceRequestScreen

import com.gurkha.hr.components.textField.DateData
import com.gurkha.model.leave.ui.AssigneeUi
import org.jetbrains.compose.resources.StringResource

data class AttendanceRequestState(
    val date: DateData? = null,
    val dateError: StringResource? = null,

    val clockInTime: String? = null,
    val clockInTimeError: StringResource? = null,

    val clockOutTime: String? = null,
    val clockOutTimeError: StringResource? = null,

    val reason: String? = null,
    val reasonError: StringResource? = null,

    val isAssigneeLoading: Boolean = false,
    val isAssigneeFetchingError: Boolean = false,
    val assignee: AssigneeUi? = null,
    val assigneeList: List<AssigneeUi>? = emptyList(),
    val assigneeError: StringResource? = null,
)