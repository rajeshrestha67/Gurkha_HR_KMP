package com.gurkha.hr.leave.model.leave

enum class LeaveStatusEnum(val value: String) {
    PENDING("Pending"),
    APPROVED("Approved"),
    REJECTED("Rejected");

    companion object {
        private val typeMap =
            enumValues<LeaveStatusEnum>().associateBy { it.value.lowercase() }

        fun get(typeName: String): LeaveStatusEnum =
            LeaveStatusEnum.typeMap[typeName.trim().lowercase()] ?: PENDING

        val list: List<LeaveStatusEnum>
            get() = entries.toList().map { it }
    }

}

val tabItemsList = LeaveStatusEnum.list


