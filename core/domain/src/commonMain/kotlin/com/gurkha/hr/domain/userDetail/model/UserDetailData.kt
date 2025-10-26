package com.gurkha.hr.domain.userDetail.model


data class UserDetailData(
    val email: String,
    val phoneNumber: String,
    val userProfileUrl: String,
    val fullName: String,
    val initials: String,
    val levelName: String,
    val employeeId: Int,
    val branchName: String,
    val joinedDate: String,
    val address: String,
    val dateOfBirth: String,
    val gender: String,
    val nationality: String,
    val maritalStatus: String,
    val guardianName: String,
    val guardianPhone: String

)