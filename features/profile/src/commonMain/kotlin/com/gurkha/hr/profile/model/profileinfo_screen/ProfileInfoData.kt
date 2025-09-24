package com.gurkha.hr.profile.model.profileinfo_screen


import com.gurkha.hr.profile.profile_info.ProfileInfoRow
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

val contactInfo: List<ProfileInfo>
    get() = listOf(
        ProfileInfo(
            name = SharedRes.Strings.email,
            value = "test@gmail.com2"
        ),
        ProfileInfo(
            name = SharedRes.Strings.phone,
            value = "9866290535"
        ),
        ProfileInfo(
            name = SharedRes.Strings.address,
            value = "baneshwor"
        )
    )

val personalDetails: List<ProfileInfo>
    get() = listOf(
        ProfileInfo(
            name = SharedRes.Strings.date_of_birth,
            value = "2062-02-26"
        ),
        ProfileInfo(
            name = SharedRes.Strings.gender,
            value = "Male"
        )  ,
        ProfileInfo(
            name = SharedRes.Strings.nationality,
            value = "N/A"
        ) ,
        ProfileInfo(
            name = SharedRes.Strings.marital_status,
            value = "UNMARRIED"
        )

    )

val guardianInfo: List<ProfileInfo>
    get() = listOf(
        ProfileInfo(
            name = SharedRes.Strings.guardian_name,
            value = "N/A"
        ),
        ProfileInfo(
            name = SharedRes.Strings.guardian_phone,
            value = "N/A"
    )
    )
