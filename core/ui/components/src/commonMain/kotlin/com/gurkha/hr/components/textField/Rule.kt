package com.gurkha.hr.components.textField

data class Rule(var check: ((String) -> ErrorStatus?)) {
    operator fun plus(other: Rule): Rule {
        return Rule {
            val firstError = this.check(it)
            firstError ?: other.check(it)
        }
    }
}