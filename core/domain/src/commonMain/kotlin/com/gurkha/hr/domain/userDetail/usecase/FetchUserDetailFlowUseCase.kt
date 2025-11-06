package com.gurkha.hr.domain.userDetail.usecase

import com.gurkha.hr.datastore.user_data.repository.UserDataRepository
import com.gurkha.model.user_data.UserData
import kotlinx.coroutines.flow.Flow

class FetchUserDetailFlowUseCase(
    private val userDataRepository: UserDataRepository

) {
    operator fun invoke(): Flow<UserData>{
        return userDataRepository.userDataFlow
    }
}