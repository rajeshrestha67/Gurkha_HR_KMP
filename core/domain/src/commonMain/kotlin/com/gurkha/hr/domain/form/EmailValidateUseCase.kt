package com.gurkha.hr.domain.form

import com.gurkha.hr.components.textField.FormValidate
import com.gurkha.hr.components.textField.validate
import org.jetbrains.compose.resources.StringResource

class EmailValidateUseCase {

    operator fun invoke(email: String): StringResource? {
        return FormValidate.emailValidationRules.validate(email)
    }

}