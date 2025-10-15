package com.gurkha.hr.profile.model.report_Screen

import com.gurkha.hr.domain.history.model.HistoryData
import com.gurkha.hr.domain.reportScreen.model.ReportData
import org.jetbrains.compose.resources.StringResource

data class ReportScreenState(
    val selectedTab: Int = 0,
    val isLoading: Boolean = false,
    val monthValue: Int = 6,
    val year: Int = 2082,
    val monthDisplay: String = "Asoj",
    val employeeId: Int? = null,
    val reportSummaryList: List<ReportData> = emptyList(),
    val historySummaryList: List<HistoryData> = emptyList(),

    val endYearError: StringResource? = null,
    val endMonthError: StringResource? = null,

    val reportListItems: List<ReportItems> = listOf<ReportItems>(
        ReportItems(
            title = "Total Days",
            days = ""
            ),
        ReportItems(
            title = "Holidays",
            days = ""),
        ReportItems(
            title = "Total Working Days",
            days =  ""),
        ReportItems(
            title = "Total Worked Days",
            days = ""),
        ReportItems(
            title = "Total Leave Taken",
            days = ""),
        ReportItems(
            title = "Total Present Days",
            days = ""),
        ReportItems(
            title = "Total Absent Days",
            days = "")
    )
)

data class ReportItems(
    val title: String,
    val days: String,
)

