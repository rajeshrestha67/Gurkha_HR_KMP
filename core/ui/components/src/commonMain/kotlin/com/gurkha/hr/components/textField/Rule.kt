package com.gurkha.hr.components.textField

import org.jetbrains.compose.resources.StringResource

data class Rule(var check: ((String) -> StringResource?)) {
    operator fun plus(other: Rule): Rule {
        return Rule {
            val firstError = this.check(it)
            firstError ?: other.check(it)
        }
    }
}