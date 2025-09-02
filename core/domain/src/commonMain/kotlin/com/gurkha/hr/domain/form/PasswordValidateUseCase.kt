package com.gurkha.hr.domain.form

import com.gurkha.hr.components.textField.ErrorStatus
import com.gurkha.hr.components.textField.FormValidate
import com.gurkha.hr.components.textField.validate

class PasswordValidateUseCase {
    operator fun invoke(password: String): ErrorStatus? {
        return FormValidate.passwordValidationRules.validate(password)
    }
}