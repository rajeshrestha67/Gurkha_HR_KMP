package com.gurkha.hr.profile.model.report_Screen

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import com.gurkha.hr.domain.history.model.HistoryData
import com.gurkha.hr.domain.reportScreen.model.ReportData
import com.gurkha.hr.res.SharedRes
import com.gurkha.hr.res.theme.holidayBlueColor
import com.gurkha.hr.res.theme.lightGreenColor
import com.gurkha.hr.res.theme.lightRedColor
import com.gurkha.model.history.ui.AttendanceStatusColorUi
import org.jetbrains.compose.resources.StringResource

data class ReportScreenState(
    val selectedTab: Int = 0,
    val isLoading: Boolean = false,
    val monthValue: Int = 6,
    val year: Int = 2082,
    val monthDisplay: String = "Asoj",
    val employeeId: Int? = null,
    val historySummaryList: List<HistoryData> = emptyList(),

    val endYearError: StringResource? = null,
    val endMonthError: StringResource? = null,

    val reportListItems: List<ReportItems> = listOf<ReportItems>(
        ReportItems(
            title = SharedRes.Strings.totalDays,
            days = ""
            ),
        ReportItems(
            title = SharedRes.Strings.holidays,
            days = ""),
        ReportItems(
            title = SharedRes.Strings.totalWorkingDays,
            days =  ""),
        ReportItems(
            title = SharedRes.Strings.totalWorkedDays,
            days = ""),
        ReportItems(
            title = SharedRes.Strings.totalLeaveTaken,
            days = ""),
        ReportItems(
            title = SharedRes.Strings.totalPresentDays,
            days = ""),
        ReportItems(
            title = SharedRes.Strings.totalAbsentDays,
            days = "")
    )
)

data class ReportItems(
    val title: StringResource,
    val days: String,
)


data class ColorUI(
    val colorUI: AttendanceStatusColorUi
)
//UI Class and one for enum