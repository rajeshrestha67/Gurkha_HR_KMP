package com.gurkha.model.upComingBirthday

import kotlinx.serialization.Serializable

@Serializable
data class UserUpComingBirthdayDetailDto(
    val status: String? = null,
    val message: String? = null,
    val detail: List<UserDetailDto>? = null,
    val success: Boolean? = null
)

@Serializable
data class UserDetailDto(
    val id: Int? = null,
    val fullName: String? = null,
    val email: String? = null,
    val dateOfBirth: String? = null,
    val gender: String? = null,
    val phoneNumber: String? = null,
    val address: String? = null,
    val maritalStatus: String? = null,
    val country: String? = null,
    val guardianName: String? = null,
    val guardianNumber: String? = null,
    val employeeRole: String? = null,
    val password: String? = null,
    val confirmPassword: String? = null,
    val departmentId: Int? = null,
    val designationName: String? = null,
    val joinedDate: String? = null,
    val branchId: Int? = null,
    val branchName: String? = null,
    val levelId: Long? = null,
    val levelName: String? = null,
    val designationId: Int? = null,
    val userRoleId: Int? = null,
    val bloodGroup: String? = null,
    val enableManualAttendance: String? = null,
    val enableImageAttendance: String? = null,
    val mapId: String? = null,
    val profileId: Int? = null,
    val dateOfBirthBs: String? = null,
    val lastModifiedDateBs: String? = null,
    val citizenshipFrontImage: String? = null,
    val citizenshipBackImage: String? = null,
    val imageUrl: String? = null,
    val slcDocument: String? = null,
    val experienceDocuments: String? = null,
    val panImage: String? = null,
    val nationalId: String? = null,
    val plusTwoImage: String? = null,
    val bachelorImage: String? = null,
    val masterImage: String? = null,
    val startDate: String? = null,
    val endDate: String? = null,
    val totalHolidays: Int? = null,
    val totalLeaveTaken: Int? = null,
    val totalOffPeriodInMinutes: Int? = null,
    val totalOffPeriodsAmount: Double? = null,
    val taxAmount: Double? = null,
    val totalCalculationDays: Int? = null,
    val totalWorkingDays: Int? = null,
    val grossSalary: Double? = null,
    val netSalary: Double? = null,
    val pfAmount: Double? = null,
    val adjustedSalary: Double? = null,
    val leaveDaysSalaryCut: Double? = null,
    val advance: Double? = null,
    val pfFromCompany: Double? = null,
    val salaryOffered: Double? = null,
    val salaryAfterLeaveAdjustment: Double? = null,
    val salaryAfterLeaveAndPfAdjustment: Double? = null,
    val employeeType: String? = null,
    val panNumber: String? = null,
    val pfNumber: String? = null,
    val dashainBonus: Double? = null,
    val assignedAmountList: List<AssignedAmountDto>? = null,
    val sst: Double? = null,
    val cit: Double? = null
)

@Serializable
data class AssignedAmountDto(
    val basicSalary: Double? = null,
    val basicSalaryId: Int? = null,
    val amount: Double? = null,
    val name: String? = null,
    val incrementId: Int? = null,
    val decrementId: Int? = null,
    val isAuto: String? = null
)
