package com.gurkha.hr.data.userDetail

import com.gurkha.hr.domain.userDetail.repository.UserDetailRemoteRepository
import com.gurkha.hr.networkhelper.BaseUrl
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.EndPoint
import com.gurkha.hr.networkhelper.get
import com.gurkha.hr.networkhelper.safeCall
import com.gurkha.model.network.DataError
import com.gurkha.model.userDetail.UserDetailResponseDto
import io.ktor.client.HttpClient

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
}