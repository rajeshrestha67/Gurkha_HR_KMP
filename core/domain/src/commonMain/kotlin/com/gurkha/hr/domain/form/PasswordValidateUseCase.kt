package com.gurkha.hr.domain.form

import com.gurkha.hr.components.textField.FormValidate
import com.gurkha.hr.components.textField.validate
import org.jetbrains.compose.resources.StringResource

class PasswordValidateUseCase {
    operator fun invoke(password: String): StringResource? {
        return FormValidate.passwordValidationRules.validate(password)
    }
}