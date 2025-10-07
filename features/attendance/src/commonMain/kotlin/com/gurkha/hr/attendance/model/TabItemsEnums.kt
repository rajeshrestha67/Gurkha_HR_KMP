package com.gurkha.hr.attendance.model

enum class TabItemsEnums(val value: String) {
    PENDING("Pending"),
    APPROVED("Approved"),
    REJECTED("Rejected");

    companion object {
        private val typeMap =
            enumValues<TabItemsEnums>().associateBy { it.value.lowercase() }

        fun get(typeName: String): TabItemsEnums =
            TabItemsEnums.typeMap[typeName.trim().lowercase()] ?: PENDING

        val list: List<TabItemsEnums>
            get() = entries.toList().map { it }
    }

}
