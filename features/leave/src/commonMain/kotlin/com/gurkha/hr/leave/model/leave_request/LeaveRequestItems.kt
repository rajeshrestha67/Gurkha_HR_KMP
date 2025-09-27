package com.gurkha.hr.leave.model.leave_request

import com.gurkha.hr.res.SharedRes
import org.jetbrains.compose.resources.StringResource

data class DropDownValue(
    val title: StringResource
)

val LeaveDurationList = listOf<DropDownValue>(
    DropDownValue(title = SharedRes.Strings.fullDay),
    DropDownValue(title = SharedRes.Strings.halfMorning),
    DropDownValue(title = SharedRes.Strings.halfAfternoon),
)

val LeaveTypeList = listOf<DropDownValue>(
    DropDownValue(title = SharedRes.Strings.sickLeave),
    DropDownValue(title = SharedRes.Strings.annualLeave),
    DropDownValue(title = SharedRes.Strings.maternityLeave),
    DropDownValue(title = SharedRes.Strings.mourningLeave),
)

