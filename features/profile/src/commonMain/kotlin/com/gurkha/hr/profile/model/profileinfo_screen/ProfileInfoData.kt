package com.gurkha.hr.profile.model.profileinfo_screen


import com.gurkha.hr.res.SharedRes
import org.jetbrains.compose.resources.StringResource


enum class InfoList(val title: StringResource) {
    PersonalInfo(SharedRes.Strings.personal_info),
    EmergencyContact(SharedRes.Strings.emergency_contact);


    companion object {
        private val typeMap =
            enumValues<InfoList>().associateBy { it.title }

        fun get(typeName: StringResource): InfoList =
            InfoList.typeMap[typeName] ?: PersonalInfo

        val list: List<InfoList>
            get() = entries.toList().map { it }
    }
}


data class ProfileInfo(
    val name:StringResource,
    val value: String
)

data class EditProfileData(
    val fullName: String,
    val initials: String ,
    val levelName: String ,
    val userProfileUrl: String,
    val phoneNumber: String,
    val employeeId: Int = 0,
    val branchName: String,
    val address: String ,
    val joinedDate: String ,
)
