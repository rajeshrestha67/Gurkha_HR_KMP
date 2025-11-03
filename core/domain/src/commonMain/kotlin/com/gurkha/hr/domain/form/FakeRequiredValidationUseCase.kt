package com.gurkha.hr.domain.form

import org.jetbrains.compose.resources.StringResource

class FakeRequiredValidationUseCase {
    var shouldReturnError: StringResource? = null

    operator fun invoke(value: String?): StringResource? = shouldReturnError
}
