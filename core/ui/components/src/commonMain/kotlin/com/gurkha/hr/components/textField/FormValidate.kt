package com.gurkha.hr.components.textField

import com.gurkha.hr.res.SharedRes
import org.jetbrains.compose.resources.StringResource

object FormValidate {
    fun validateRules(text: String?, rules: List<Rule>): StringResource? {
        rules.map { rule ->
            val errorMessage = rule.check.invoke(text ?: "")
            if (errorMessage != null) {
                return@validateRules errorMessage
            }
        }
        return null
    }

    val requiredRule = Rule { text ->
        if (text.isEmpty()) {
            SharedRes.Strings.required
        } else {
            null
        }
    }


    val emailRule = Rule { text ->
        if (!text.matches(Regex("^[a-zA-Z0-9.!#\$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*\$"))) {
            SharedRes.Strings.invalidEmailAddress
        } else {
            null
        }
    }

    val upperCaseRule = Rule { text ->
        if (!text.matches(Regex(".*[A-Z].*"))) {
            SharedRes.Strings.invalidPasswordUppercase
        } else {
            null
        }
    }

    val lowerCaseRule = Rule { text ->
        if (!text.matches(Regex(".*[a-z].*"))) {
            SharedRes.Strings.invalidPasswordLowercase
        } else {
            null
        }
    }

    val digitRule = Rule { text ->
        if (!text.matches(Regex(".*[0-9].*"))) {
            SharedRes.Strings.invalidPasswordDigit
        } else {
            null
        }
    }
    val specialCharRule = Rule { text ->
        if (!text.matches(Regex(".*[^A-Za-z0-9].*"))) {
            SharedRes.Strings.invalidPasswordSpecialChar
        } else {
            null
        }
    }
    val passwordLengthRule = Rule { text ->
        if (!text.matches(Regex(".{6,}"))) {
            SharedRes.Strings.invalidPasswordLength
        } else {
            null
        }
    }
    val requiredValidationRules = listOf(requiredRule)
    val emailValidationRules = requiredValidationRules + listOf(emailRule)

    val passwordValidationRules = requiredValidationRules + listOf(
//        upperCaseRule,
//        lowerCaseRule,
//        digitRule,
//        specialCharRule,
        passwordLengthRule
    )
}

fun List<Rule>.validate(text: String?): StringResource? {
    return FormValidate.validateRules(text = text, this)
}