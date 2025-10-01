package com.gurkha.hr.domain.chat.repository

import com.gurkha.hr.networkhelper.DataError
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.model.chat.list.EmployListResponseDto

interface ChatRemoteRepository {

    suspend fun fetchEmployList(): ERPResult<EmployListResponseDto, DataError>
}