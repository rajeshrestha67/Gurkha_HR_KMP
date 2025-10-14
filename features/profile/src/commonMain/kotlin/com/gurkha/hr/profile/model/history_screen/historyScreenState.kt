package com.gurkha.hr.profile.model.history_screen


import com.gurkha.hr.domain.history.model.HistoryData
import org.jetbrains.compose.resources.StringResource

data class HistoryState(
    val isLoading: Boolean = false,
    val bsMonth: Int = 6,
    val bsYear: Int = 2082,
    val historySummaryList: List<HistoryData> = emptyList(),

    val endYearError: StringResource? = null,
    val endMonthError: StringResource? = null
)
