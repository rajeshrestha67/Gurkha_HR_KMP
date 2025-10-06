package com.gurkha.hr.data.upComingBirthday

import com.gurkha.hr.domain.upComingBirthday.repository.UpComingBirthdayRemoteRepository
import com.gurkha.hr.networkhelper.BaseUrl
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.EndPoint
import com.gurkha.hr.networkhelper.get
import com.gurkha.hr.networkhelper.safeCall
import com.gurkha.model.network.DataError
import com.gurkha.model.upComingBirthday.UserUpComingBirthdayDetailDto
import io.ktor.client.HttpClient

class KtorUpComingBirthdayRemoteRepository(
    private val httpClient: HttpClient
) : UpComingBirthdayRemoteRepository {
    override suspend fun fetchUpComingBirthday(): ERPResult<UserUpComingBirthdayDetailDto, DataError> {
        return safeCall {
            httpClient.get(
                baseUrl = BaseUrl.Generic,
                endPoint = EndPoint.UPCOMING_BIRTHDAY_END_POINT
            )
        }
    }

}