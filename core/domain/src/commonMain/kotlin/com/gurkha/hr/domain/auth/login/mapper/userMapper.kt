package com.gurkha.hr.domain.auth.login.mapper

import com.gurkha.hr.domain.auth.login.model.LoginData
import com.gurkha.model.auth.login.LoginResponseDto


fun LoginResponseDto.toData(): LoginData {
    return LoginData(
        token = token ?: "",
        role = role ?: ""
    )
}