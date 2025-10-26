@file:OptIn(ExperimentalTime::class)

package com.gurkha.hr.data.userDetail

import com.gurkha.hr.domain.userDetail.model.UserDetailData
import com.gurkha.hr.domain.userDetail.repository.UserDetailRemoteRepository
import com.gurkha.hr.networkhelper.BaseUrl
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.EndPoint
import com.gurkha.hr.networkhelper.get
import com.gurkha.hr.networkhelper.put
import com.gurkha.hr.networkhelper.safeCall
import com.gurkha.model.network.DataError
import com.gurkha.model.userDetail.UpdateRequestUserDto
import com.gurkha.model.userDetail.UserDetailResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class KtorUserDetailRemoteRepository(
    val httpClient: HttpClient
) : UserDetailRemoteRepository {
    override suspend fun fetchUserDetail(): ERPResult<UserDetailResponseDto, DataError> {
        return safeCall {
            httpClient.get(
                baseUrl = BaseUrl.Generic,
                endPoint = EndPoint.CURRENT_USER_DETAIL_END_POINT
            )
        }
    }

    override suspend fun userDataUpdate(data: UserDetailData): ERPResult<UserDetailResponseDto, DataError> {
        return safeCall {
            httpClient.put(
                baseUrl = BaseUrl.Generic,
                endPoint = EndPoint.EDIT_USER_DETAILS_ENDPOINT + "/${data.employeeId}"
            ) {
                setBody(
                    UpdateRequestUserDto(
                        fullName = data.fullName,
                        phoneNumber = data.phoneNumber,
                        address = data.address,
                        gender = data.gender,
                        bloodGroup = data.bloodGroup,
                        guardianName = data.guardianName,
                        guardianNumber = data.guardianNumber,
                        level = data.levelName,
                        designation = data.designation,
                        email = data.email,
                        joinedDate = data.joinedDate,
                        maritalStatus = data.maritalStatus,
                        dateOfBirth = data.dateOfBirth,
                        panNumber = data.panNumber,
                        pfNumber = data.pfNumber,
                        bachelorImage = data.bachelorImage,
                        branchId = data.branchId,
                        citizenshipBackImage = data.citizenshipBackImage,
                        citizenshipFrontImage = data.citizenshipFrontImage,
                        departmentId = data.departmentId,
                        designationId = data.designationId,
                        employeeType = data.employeeType,
                        enableImageAttendance = data.enableImageAttendance,
                        enableManualAttendance = data.enableManualAttendance,
                        experienceDocuments = data.experienceDocuments,
                        imageUrl = data.imageUrl,
                        levelId = Clock.System.now().toEpochMilliseconds(),
                        mapId = data.mapId,
                        masterImage = data.masterImage,
                        nationalId = data.nationalId,
                        panImage = data.panImage,
                        password = data.password,
                        plusTwoImage = data.plusTwoImage,
                        profileId = data.profileId,
                        slcDocument = data.slcDocument,
                    )
                )

            }
        }
    }
}