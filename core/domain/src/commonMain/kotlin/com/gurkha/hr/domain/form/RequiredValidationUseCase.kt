package com.gurkha.hr.domain.form

import com.gurkha.hr.components.textField.FormValidate
import com.gurkha.hr.components.textField.validate
import org.jetbrains.compose.resources.StringResource

class RequiredValidationUseCase {
    operator fun invoke(value: String?): StringResource? {
        return FormValidate.requiredValidationRules.validate(value)
    }
}