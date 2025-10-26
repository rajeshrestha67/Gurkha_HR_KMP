package com.gurkha.hr.data.change_password

import com.gurkha.hr.domain.changePassword.repository.ChangePasswordRemoteRepository
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.EndPoint.CHANGE_PASSWORD_END_POINT
import com.gurkha.hr.networkhelper.post
import com.gurkha.hr.networkhelper.safeCall
import com.gurkha.model.changePassword.ChangePasswordRequestDTO
import com.gurkha.model.changePassword.ChangePasswordResponseDTO
import com.gurkha.model.network.DataError
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody

class KtorChangePasswordRemoteRepository(
    private val httpClient: HttpClient
) : ChangePasswordRemoteRepository {

    override suspend fun changePassword(
        email: String,
        newPassword: String,
        confirmPassword: String
    ): ERPResult<ChangePasswordResponseDTO, DataError> {
        val requestDto = ChangePasswordRequestDTO(
            email = email,
            password = newPassword,
            confirmPassword = confirmPassword
        )
        return safeCall {
            httpClient.post(
                endPoint = CHANGE_PASSWORD_END_POINT
            ) {
                setBody(
                    requestDto
                )
            }
        }
    }
}