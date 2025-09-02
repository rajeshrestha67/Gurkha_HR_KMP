package com.gurkha.hr.domain.form

import com.gurkha.hr.components.textField.ErrorStatus
import com.gurkha.hr.components.textField.FormValidate
import com.gurkha.hr.components.textField.validate

class EmailValidateUseCase {
    
    operator fun invoke(email: String): ErrorStatus? {
        return FormValidate.emailValidationRules.validate(email)
    }

}