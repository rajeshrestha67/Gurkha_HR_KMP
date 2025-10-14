package com.gurkha.hr.profile.model.history_screen


import com.gurkha.hr.domain.history.model.HistoryData
import org.jetbrains.compose.resources.StringResource

data class HistoryState(
    val isLoading: Boolean = false,
    val monthValue: Int = 6,
    val year: Int = 2082,
    val monthDisplay: String = "Asoj",
    val historySummaryList: List<HistoryData> = emptyList(),
    val employeeId: Int? = null,

    val endYearError: StringResource? = null,
    val endMonthError: StringResource? = null
)
