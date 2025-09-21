package com.gurkha.model.userDetail

import kotlinx.serialization.Serializable

@Serializable
data class UserDetailResponseDto(
    val status: String? = null,
    val message: String? = null,
    val detail: UserDetailDto? = null,
    val success: Boolean? = null
)

@Serializable
data class UserDetailDto(
    val fullName: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val levelName: String? = null,
    val userRole: String? = null,
    val employeeId: Int? = null,
    val profileId: Int? = null,
    val roles: List<String>? = null,
    val employeeDetails: EmployeeDetailsDto? = null,
    val isSupervisor: Boolean? = null,
    val isCheckedIn: String? = null,
    val isCheckedOut: String? = null,
    val checkedInTime: String? = null,
    val checkedOutTime: String? = null,
    val profileCode: String? = null,
    val enableManualAttendance: String? = null,
    val enableImageAttendance: String? = null,
    val imageUrl: String? = null,
    val experienceDocumentsUrl: String? = null,
    val citizenshipFrontImage: String? = null,
    val citizenshipBackImage: String? = null,
    val panImage: String? = null,
    val nationalIdImage: String? = null,
    val plusTwoImage: String? = null,
    val bachelorImageUrl: String? = null,
    val masterImageUrl: String? = null,
    val slcDocumentUrl: String? = null,
    val dateOfBirth: String? = null,
    val joinedDate: String? = null,
    val maxApprovalLimit: Int? = null,
    val isCompleteProfile: String? = null,
    val socketPrefix: String? = null,
    val allowAttendanceApproval: String? = null,
    val editApproveLeave: String? = null
)

@Serializable
data class EmployeeDetailsDto(
    val id: Int? = null,
    val createdDate: String? = null,
    val createdByUserId: Int? = null,
    val createdBy: String? = null,
    val modifiedBy: String? = null,
    val lastModified: String? = null,
    val fullName: String? = null,
    val email: String? = null,
    val dateOfBirth: String? = null,
    val gender: String? = null,
    val phoneNumber: String? = null,
    val address: String? = null,
    val maritalStatus: String? = null,
    val guardianName: String? = null,
    val guardianNumber: String? = null,
    val country: String? = null,
    val bloodGroup: String? = null,
    val employeeRole: String? = null,
    val joinedDate: String? = null,
    val panNumber: String? = null,
    val pfNumber: String? = null,
    val employeeType: String? = null,
    val user: UserDto? = null,
    val lastSalaryCalculated: String? = null,
    val active: String? = null,
    val profileId: Int? = null,
    val enableManualAttendance: String? = null,
    val mapId: String? = null,
    val isDetailComplete: String? = null,
    val enableImageAttendance: String? = null,
    val imageUrl: String? = null,
    val experienceDocumentsUrl: String? = null,
    val citizenshipFrontImage: String? = null,
    val citizenshipBackImage: String? = null,
    val panImage: String? = null,
    val nationalIdImage: String? = null,
    val plusTwoImage: String? = null,
    val bachelorImageUrl: String? = null,
    val masterImageUrl: String? = null,
    val slcDocumentUrl: String? = null
)

@Serializable
data class UserDto(
    val id: Int? = null,
    val createdDate: String? = null,
    val createdByUserId: Int? = null,
    val createdBy: String? = null,
    val modifiedBy: String? = null,
    val lastModified: String? = null,
    val name: String? = null,
    val email: String? = null,
    val gender: String? = null,
    val confirmPassword: String? = null,
    val userStatus: Boolean? = null,
    val contact: String? = null,
    val profileId: Int? = null,
    val branch: BranchDto? = null,
    val active: String? = null,
    val isFirstLogin: String? = null,
    val allowAttendanceApproval: String? = null,
    val maxApproverLimit: Int? = null
)

@Serializable
data class BranchDto(
    val id: Int? = null,
    val createdDate: String? = null,
    val createdByUserId: Int? = null,
    val createdBy: String? = null,
    val modifiedBy: String? = null,
    val lastModified: String? = null,
    val branchName: String? = null,
    val branchCode: String? = null,
    val address: String? = null,
    val contactNumber: String? = null,
    val branchAdmin: String? = null,
    val active: String? = null,
    val targetHolidays: String? = null,
    val isDefaultHoliday: String? = null
)

