package com.gurkha.hr.domain.auth.login.usecase

import com.gurkha.hr.domain.auth.login.model.LoginData
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.model.network.DataError

class FakeLoginUseCase(
    var result: ERPResult<LoginData, DataError> =
        ERPResult.Success(LoginData("fakeToken", "HRM_EMPLOYEE", null))
) {
    suspend operator fun invoke(
        username: String,
        password: String
    ): ERPResult<LoginData, DataError> {
        return result
    }
}