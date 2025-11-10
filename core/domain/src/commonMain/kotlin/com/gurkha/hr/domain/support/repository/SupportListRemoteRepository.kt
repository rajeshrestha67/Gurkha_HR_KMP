package com.gurkha.hr.domain.support.repository

import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.model.network.DataError
import com.gurkha.model.support.SupportResponseDto

interface SupportListRemoteRepository {
    suspend fun getSupportList(): ERPResult<SupportResponseDto, DataError>
}