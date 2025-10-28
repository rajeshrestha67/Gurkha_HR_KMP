package com.gurkha.hr.profile.model.report_Screen

import com.gurkha.hr.profile.model.history_screen.HistoryDataUI
import com.gurkha.hr.res.SharedRes
import org.jetbrains.compose.resources.StringResource

data class ReportScreenState(
    val isLoading: Boolean = false,
    val monthValue: Int = 6,
    val year: Int = 2082,
    val monthDisplay: String = "Asoj",
    val employeeId: Int? = null,
    val historySummaryList: List<HistoryDataUI> = emptyList(),

    val endYearError: StringResource? = null,
    val endMonthError: StringResource? = null,

    val isRefreshing: Boolean = false,

    val reportListItems: List<ReportItems> = listOf<ReportItems>(
        ReportItems(
            title = SharedRes.Strings.totalDays,
            days = ""
        ),
        ReportItems(
            title = SharedRes.Strings.holidays,
            days = ""
        ),
        ReportItems(
            title = SharedRes.Strings.totalWorkingDays,
            days = ""
        ),
        ReportItems(
            title = SharedRes.Strings.totalWorkedDays,
            days = ""
        ),
        ReportItems(
            title = SharedRes.Strings.totalLeaveTaken,
            days = ""
        ),
        ReportItems(
            title = SharedRes.Strings.totalPresentDays,
            days = ""
        ),
        ReportItems(
            title = SharedRes.Strings.totalAbsentDays,
            days = ""
        )
    ),
    val items: List<ReportType> = ReportType.list,
    val selectedTab: ReportType = ReportType.MonthlyAttendance
)

data class ReportItems(
    val title: StringResource,
    val days: String,
)

