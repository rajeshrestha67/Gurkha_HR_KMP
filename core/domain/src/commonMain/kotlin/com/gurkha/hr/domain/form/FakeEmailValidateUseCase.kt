package com.gurkha.hr.domain.form

class FakeEmailValidateUseCase {
    var shouldReturnError: String? = null

    operator fun invoke(email: String): String? = shouldReturnError
}