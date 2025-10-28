@file:OptIn(ExperimentalTime::class)

package com.gurkha.hr.data.userDetail

import com.gurkha.hr.domain.userDetail.model.UserDetailData
import com.gurkha.hr.domain.userDetail.repository.UserDetailRemoteRepository
import com.gurkha.hr.networkhelper.BaseUrl
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.EndPoint
import com.gurkha.hr.networkhelper.get
import com.gurkha.hr.networkhelper.post
import com.gurkha.hr.networkhelper.put
import com.gurkha.hr.networkhelper.safeCall
import com.gurkha.model.network.DataError
import com.gurkha.model.userDetail.UpdateProfileResponseDto
import com.gurkha.model.userDetail.UpdateRequestUserDto
import com.gurkha.model.userDetail.UserDetailResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.request.post
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

    override suspend fun userDataUpdate(data: UpdateRequestUserDto): ERPResult<UpdateProfileResponseDto, DataError> {
        return safeCall {
            httpClient.post(
                baseUrl = BaseUrl.Generic,
                endPoint = EndPoint.EDIT_USER_DETAILS_ENDPOINT + "/${data.id}"
            ) {
                setBody(
                    UpdateRequestUserDto(
                        bloodGroup = data.bloodGroup,
                        guardianName = data.guardianName,
                        guardianNumber = data.guardianNumber,
                        joinedDate = data.joinedDate,
                        panNumber = data.panNumber,
                        pfNumber = data.pfNumber,
                        startDate = data.startDate,
                        id = data.id
                    )
                )

            }
        }
    }
}