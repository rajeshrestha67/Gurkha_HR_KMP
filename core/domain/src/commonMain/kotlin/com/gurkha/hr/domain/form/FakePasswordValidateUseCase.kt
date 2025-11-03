package com.gurkha.hr.domain.form

class FakePasswordValidateUseCase {
    var shouldReturnError: String? = null

    operator fun invoke(password: String): String? = shouldReturnError
}