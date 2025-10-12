package com.gurkha.hr.datastore.user_data.model

import kotlinx.serialization.Serializable

@Serializable
data class UserData(
    val email: String = "",
    val phoneNumber: String = "",
    val imageUrl: String = "",
    val fullName: String = "",
    val initials: String = "",
    val levelName: String = "",
    val employeeId: Int = 0,
    val branchName: String = "",
    val joinedDate: String = "",
    val address: String = "",
    val dateOfBirth: String = "N/A",
    val gender: String = "N/A",
    val nationality: String = "N/A",
    val maritalStatus: String = "N/A",
    val guardianName: String = "N/A",
    val guardianPhone: String = "N/A"

)
