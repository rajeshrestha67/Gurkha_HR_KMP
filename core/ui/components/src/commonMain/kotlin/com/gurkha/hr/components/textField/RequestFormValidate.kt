package com.gurkha.hr.components.textField


import com.gurkha.hr.res.SharedRes

object RequestFormValidate {
    fun validateRules(text: String, rules: List<Rule>): ErrorStatus? {
        rules.map { rule ->
            val errorMessage = rule.check.invoke(text)
            if (errorMessage != null) {
                return@validateRules errorMessage
            }
        }
        return null
    }

    val requiredRule = Rule { text ->
        if (text.isEmpty()) {
            ErrorStatus(isError = true, errorMsg = SharedRes.Strings.required)
        } else {
            null
        }
    }


    val dateValidationRules = listOf(requiredRule)

    val dropDownValidationRules = listOf(
        requiredRule
    )
    val reasonValidationRules = listOf(
        requiredRule
    )

}

fun List<Rule>.validateLeave(text: String): ErrorStatus? {
    return FormValidate.validateRules(text = text, this)
}