package com.gurkha.hr.profile.model.history_screen

import org.jetbrains.compose.resources.StringResource

interface HistoryScreenViewAction {

    data class FromYear(val year: Int) : HistoryScreenViewAction
    data class FromMonth(val month: Int, val showMonth: String) : HistoryScreenViewAction

    data class YearPickerError(val error: StringResource?): HistoryScreenViewAction
    data class MonthPickerError(val error: StringResource?): HistoryScreenViewAction
    data class Submit(val employeeId: Int?) : HistoryScreenViewAction
    
}