package com.gurkha.hr.components

fun String?.extractInitials(): String {
    if (this.isNullOrBlank()) return ""

    val nameParts = this.trim().split("\\s+".toRegex()).filter { it.isNotBlank() }

    return when {
        nameParts.isEmpty() -> ""
        nameParts.size == 1 -> nameParts.first().first().uppercase()
        else -> {
            val first = nameParts.first().first().uppercase()
            val last = nameParts.last().first().uppercase()
            "$first$last"
        }
    }
}