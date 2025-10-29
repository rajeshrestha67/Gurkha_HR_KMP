package com.gurkha.hr.domain.userDetail.ui

import com.gurkha.hr.components.date.DateData

data class EditProfileUI(
    val email: String ,
    val phoneNumber: String ,
    val fullName: String ,
    val levelName: String ,
    val employeeId: Int ,
    val branchName: String ,
    val joinedDate: DateData ?,
    val address: String ,
    val dateOfBirth: DateData?,
    val gender: String ,
    val maritalStatus: String ,
    val guardianName: String ,
    val guardianPhone: String ,
    val bloodGroup: String ,
    val designation: String ,
    val employeeTypes: String ,
    val panNumber: String ,
    val pfNumber: String ,
    val imageUrl: String? = "",
    val initials: String? = "",
)
