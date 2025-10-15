package com.gurkha.hr.profile.model.report_Screen

import org.jetbrains.compose.resources.StringResource

interface ReportScreenViewAction {
    data class OnItemSelected(val index: Int) : ReportScreenViewAction
    data class YearField(val year: Int) : ReportScreenViewAction
    data class MonthField(val month: Int, val showMonth: String) : ReportScreenViewAction

    data class YearFieldError(val error: StringResource?): ReportScreenViewAction
    data class MonthFieldError(val error: StringResource?): ReportScreenViewAction
    data class Submit(val employeeId: Int?) : ReportScreenViewAction
}