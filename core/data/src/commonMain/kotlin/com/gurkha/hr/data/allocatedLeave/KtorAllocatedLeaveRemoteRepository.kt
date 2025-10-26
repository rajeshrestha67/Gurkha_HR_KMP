package com.gurkha.hr.data.allocatedLeave

import com.gurkha.hr.domain.allocatedLeave.repository.AllocatedLeaveRemoteRepository
import com.gurkha.hr.networkhelper.BaseUrl
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.EndPoint
import com.gurkha.hr.networkhelper.get
import com.gurkha.hr.networkhelper.safeCall
import com.gurkha.model.allocatedLeave.AllocatedLeaveResponseDto
import com.gurkha.model.network.DataError

import io.ktor.client.HttpClient

class KtorAllocatedLeaveRemoteRepository(private val httpClient: HttpClient) :
    AllocatedLeaveRemoteRepository {
    override suspend fun getAllocatedLeave(id: Int): ERPResult<AllocatedLeaveResponseDto, DataError> {
        return safeCall {
            httpClient.get(
                baseUrl = BaseUrl.Generic,
                endPoint = EndPoint.ALLOCATED_LEAVE_ENDPOINT+"/$id"
            )
        }
    }


}