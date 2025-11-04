package com.gurkha.hr.domain.userDetail.mapper

import com.gurkha.hr.components.date.DateData
import com.gurkha.hr.components.extractInitials
import com.gurkha.hr.domain.userDetail.model.UserDetailData
import com.gurkha.hr.domain.userDetail.model.UserUpdateData
import com.gurkha.hr.domain.userDetail.ui.EditProfileUI
import com.gurkha.model.userDetail.UpdateProfileResponseDto
import com.gurkha.model.userDetail.UpdateRequestUserDto
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
        dateOfBirth = detail?.employeeDetails?.dateOfBirth?.split("T")[0]  ?: "",
        gender = detail?.employeeDetails?.gender ?: "",
        joinedDate = detail?.employeeDetails?.joinedDate?.split("T")[0] ?: "",
        nationality = detail?.employeeDetails?.country ?: "",
        maritalStatus = detail?.employeeDetails?.maritalStatus ?: "",
        guardianName = detail?.employeeDetails?.guardianName ?: "",
        guardianNumber = detail?.employeeDetails?.guardianNumber ?: "",
        initials = detail?.fullName?.extractInitials() ?: "",
        isCompleteProfile = if(detail?.isCompleteProfile == "Y") true else false,
        bloodGroup = detail?.employeeDetails?.bloodGroup ?: "",
        designation = detail?.designation ?: "",
        panNumber = detail?.employeeDetails?.panNumber ?: "",
        pfNumber = detail?.employeeDetails?.pfNumber ?: "",
        bachelorImage = detail?.employeeDetails?.bachelorImageUrl?.let {
            "https://mbank.gurkhahr.com/erp-images/${it}"
        } ,
        branchId =detail?.employeeDetails?.user?.branch?.id ?: 0,
        citizenshipBackImage = detail?.employeeDetails?.citizenshipBackImage?.let {
            "https://mbank.gurkhahr.com/erp-images/${it}"
        } ,
        citizenshipFrontImage = detail?.employeeDetails?.citizenshipFrontImage?.let {
            "https://mbank.gurkhahr.com/erp-images/${it}"
        } ,
        departmentId = detail?.departmentId?:0,
        designationId =detail?.designationId?: 0 ,
        employeeType = detail?.employeeType ?: "",
        enableImageAttendance = detail?.employeeDetails?.enableImageAttendance ?: "",
        enableManualAttendance = detail?.employeeDetails?.enableManualAttendance ?: "",
        experienceDocuments = detail?.employeeDetails?.experienceDocumentsUrl?.let {
            "https://mbank.gurkhahr.com/erp-images/${it}"
        } ,
        imageUrl = detail?.imageUrl ,
        levelId = detail?.levelId ?: 0,
        mapId = detail?.employeeDetails?.mapId ?: 0,
        masterImage = detail?.employeeDetails?.masterImageUrl?.let {
            "https://mbank.gurkhahr.com/erp-images/${it}"
        } ,
        nationalId = detail?.employeeDetails?.nationalIdImage?.let {
            "https://mbank.gurkhahr.com/erp-images/${it}"
        } ,
        panImage = detail?.employeeDetails?.panImage?.let {
            "https://mbank.gurkhahr.com/erp-images/${it}"
        },
        password = "",
        plusTwoImage = detail?.employeeDetails?.plusTwoImage?.let {
            "https://mbank.gurkhahr.com/erp-images/${it}"
        } ,
        profileId = detail?.profileId ?: detail?.employeeDetails?.profileId ?: 0,
        slcDocument = detail?.employeeDetails?.slcDocumentUrl?.let {
            "https://mbank.gurkhahr.com/erp-images/${it}"
        }
    )
}

fun UserData.toDetail(): UserDetailData {
    return UserDetailData(
        email = email,
        phoneNumber = phoneNumber,
        fullName = fullName,
        levelName = levelName,
        employeeId = employeeId,
        address = address,
        dateOfBirth = dateOfBirth,
        gender = gender,
        joinedDate = joinedDate,
        nationality = nationality,
        maritalStatus = maritalStatus,
        guardianName = guardianName,
        guardianNumber = guardianPhone,
        initials = initials,
        isCompleteProfile = isCompleteProfile,
        bloodGroup = bloodGroup,
        designation = designation,
        employeeType = employeeTypes,
        panNumber = panNumber,
        pfNumber = pfNumber,
        branchName = branchName,
        bachelorImage =bachelorImage ,
        branchId = branchId,
        citizenshipBackImage = citizenshipBackImage,
        citizenshipFrontImage = citizenshipFrontImage,
        departmentId = departmentId,
        designationId = designationId,
        enableImageAttendance = enableImageAttendance,
        enableManualAttendance = enableManualAttendance,
        experienceDocuments = experienceDocuments,
        imageUrl = imageUrl,
        levelId = levelId,
        mapId = mapId,
        masterImage =masterImage,
        nationalId = nationalId,
        panImage = panImage,
        password = password,
        plusTwoImage = plusTwoImage,
        profileId =  profileId,
        slcDocument = slcDocument,
        userProfileUrl = userProfileUrl,
    )
}

fun UserDetailData.toUI(): EditProfileUI{
    return EditProfileUI(
        fullName = fullName,
        email = email,
        levelName = levelName,
        phoneNumber = phoneNumber,
        employeeId = employeeId,
        branchName = branchName,
        address = address,
        joinedDate = if(joinedDate.isNotBlank())DateData.fromDisplayAD(joinedDate )else null,
        maritalStatus = maritalStatus,
        gender = gender,
        dateOfBirth = if(dateOfBirth.isNotBlank())DateData.fromDisplayAD(dateOfBirth ) else null,
        bloodGroup = bloodGroup,
        guardianName = guardianName,
        guardianPhone = guardianNumber,
        designation = designation,
        employeeTypes = employeeType,
        panNumber = panNumber,
        pfNumber = pfNumber,
    initials = initials,
        imageUrl = imageUrl

    )
}

fun EditProfileUI.toDomain(): UpdateRequestUserDto {
    return UpdateRequestUserDto(
        joinedDate = joinedDate?.displayValueAD,
        id = employeeId,
        startDate = dateOfBirth?.displayValueAD,
        guardianName = guardianName,
        guardianNumber = guardianPhone,
        bloodGroup = bloodGroup,
        employeeType = employeeTypes,
        panNumber = panNumber,
        pfNumber = pfNumber,
    )
}

fun UpdateProfileResponseDto.toData(): UserUpdateData{
    return UserUpdateData(
        message = message ?: ""
    )
}

fun EditProfileUI.toLocal(): UserData {
    return UserData(
        email = email,
        phoneNumber = phoneNumber,
        fullName = fullName,
        levelName = levelName,
        employeeId = employeeId,
        branchName = branchName,
        joinedDate = joinedDate?.displayValueAD ?: "",
        address = address,
        dateOfBirth = dateOfBirth?.displayValueAD ?: "",
        gender = gender,
        maritalStatus = maritalStatus,
        guardianName = guardianName,
        guardianPhone = guardianPhone,
        bloodGroup = bloodGroup,
        designation = designation,
        employeeTypes = employeeTypes,
        panNumber = panNumber,
        pfNumber = pfNumber,
        imageUrl = imageUrl ?: "",
        initials = initials ?:"",

    )
}
