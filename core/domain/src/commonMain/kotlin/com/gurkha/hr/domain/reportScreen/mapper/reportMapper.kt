package com.gurkha.hr.domain.reportScreen.mapper

import com.gurkha.hr.domain.reportScreen.model.ReportData
import com.gurkha.model.reportScreen.ReportSummaryResponseDTO

fun ReportSummaryResponseDTO.toData(): List<ReportData>{
    return detail?.map {
        ReportData(
            fullName = it.fullName ?:"",
            department = it.branch ?:"",
            totalDays = it.totalDays ?:0,
            holidays = it.holidays ?:0,
            totalWorkingDays = it.totalWorkingDays ?:0,
            totalLeaveTaken = it.totalLeaveTaken ?:0.0,
            totalPresentDays = it.totalPresentDays ?:0.0,
            totalAbsentDays = it.totalAbsentDays ?:0.0,
            totalWorkedDays = it.totalWorkedDays ?:0.0,
            imageUrl = it.imageUrl ?:""
        )
    }?: emptyList()

}