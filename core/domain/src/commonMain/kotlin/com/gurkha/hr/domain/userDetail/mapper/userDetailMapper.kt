package com.gurkha.hr.domain.userDetail.mapper

import com.gurkha.hr.components.extractInitials
import com.gurkha.hr.domain.userDetail.model.UserDetailData
import com.gurkha.model.userDetail.UserDetailResponseDto
import com.gurkha.model.user_data.UserData

fun UserDetailResponseDto.toData(): UserDetailData {
    return UserDetailData(
        email = detail?.email ?: "",
        phoneNumber = detail?.employeeDetails?.phoneNumber ?: "",
        userProfileUrl = detail?.imageUrl ?: "",
        fullName = detail?.fullName ?: "",
        levelName = detail?.levelName ?: "",
        employeeId = detail?.employeeId ?: 0,
        address = detail?.employeeDetails?.address ?: "",
        branchName = detail?.employeeDetails?.user?.branch?.branchName ?: "",
        dateOfBirth = detail?.employeeDetails?.dateOfBirth ?: "",
        gender = detail?.employeeDetails?.gender ?: "",
        joinedDate = detail?.employeeDetails?.joinedDate ?: "",
        nationality = detail?.employeeDetails?.country ?: "",
        maritalStatus = detail?.employeeDetails?.maritalStatus ?: "",
        guardianName = detail?.employeeDetails?.guardianName ?: "",
        guardianPhone = detail?.employeeDetails?.guardianNumber ?: "",
        initials = detail?.fullName?.extractInitials() ?: ""
    )
}

fun UserData.toDetail(): UserDetailData {
    return UserDetailData(
        email = email,
        phoneNumber = phoneNumber,
        userProfileUrl = imageUrl,
        fullName = fullName,
        levelName = levelName,
        employeeId = employeeId,
        address = address,
        branchName = branchName,
        dateOfBirth = dateOfBirth,
        gender = gender,
        joinedDate = joinedDate,
        nationality = nationality,
        maritalStatus = maritalStatus,
        guardianName = guardianName,
        guardianPhone = guardianPhone,
        initials = initials
    )
}

