package com.gurkha.hr.leave.model

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import com.gurkha.hr.res.SharedRes
import org.jetbrains.compose.resources.StringResource

data class LeaveItem(
    val title: StringResource,
    val days: String,
    val color: Color,
    val backGroundColor : Color
)

enum class AttendanceStatusEnum(val value:String) {
    PENDING("pending"),
    APPROVED("approved"),
    CANCELLED("cancelled");

    companion object{
        private val typeMap =
            enumValues<AttendanceStatusEnum>().associateBy { it.value.lowercase() }

        fun get(typeName: String): AttendanceStatusEnum =
            AttendanceStatusEnum.typeMap[typeName.trim().lowercase()] ?: PENDING

        val list: List<String>
            get() = entries.toList().map { it.value }
    }

}

val leaveItemsList = listOf(
    LeaveItem(title = SharedRes.Strings.leave_balance, days = "20", color =  Color(0xFF81D4FA), backGroundColor = Color(0xFFE1F5FE) ),
    LeaveItem(title = SharedRes.Strings.leave_approved, days = "2", color =  Color(0xFFA5D6A7), backGroundColor = Color(0xFFE8F5E9) ),
    LeaveItem(title = SharedRes.Strings.leave_pending, days = "5", color =  Color(0xFFC5E1A5), backGroundColor = Color(0xFFF1F8E9)),
    LeaveItem(title = SharedRes.Strings.leave_cancelled, days = "7", color =  Color(0xFFEF9A9A) , backGroundColor = Color(0xFFFFEBEE)),
)
val tabItemsList = AttendanceStatusEnum.list


