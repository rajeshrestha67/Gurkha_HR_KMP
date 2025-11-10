package com.gurkha.hr.domain.upComingBirthday.model

import androidx.compose.ui.graphics.Color


data class UpComingBirthdayData(
    val fullName: String,
    val dateOfBirth : String,
    val designationName : String,
    val branchName : String,
    val imageUrl : String?,
    val initials: String,
    val employeeId: Long,
    val chatId: String,
    val employeeName: String,
    val phoneNumber: String,
    val backgroundColor : Color
)