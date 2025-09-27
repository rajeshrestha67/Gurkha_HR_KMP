package com.gurkha.hr.domain.userDetail.usecase

import com.gurkha.hr.datastore.user_data.model.UserData
import com.gurkha.hr.datastore.user_data.repository.UserDataRepository
import kotlinx.coroutines.flow.Flow

class FetchLocalUserDetailUseCase(
    private val userDataRepository: UserDataRepository
) {

    operator fun invoke(): Flow<UserData>{
        return userDataRepository.userDataFlow
    }
}