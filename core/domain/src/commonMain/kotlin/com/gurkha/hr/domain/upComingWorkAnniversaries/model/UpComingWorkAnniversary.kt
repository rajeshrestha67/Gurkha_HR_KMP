package com.gurkha.hr.domain.upComingWorkAnniversaries.model

import androidx.compose.ui.graphics.Color

data class UpComingWorkAnniversaryData(
    val fullName: String,
    val designationName: String,
    val imageUrl: String,
    val branchName: String,
    val joinedDate: String,
    val initials: String,
    val backgroundColor: Color
)
