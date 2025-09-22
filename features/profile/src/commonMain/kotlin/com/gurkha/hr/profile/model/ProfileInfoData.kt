package com.gurkha.hr.profile.model


import com.gurkha.hr.res.SharedRes
import org.jetbrains.compose.resources.StringResource


enum class InfoList(val title: StringResource) {
    PersonalInfo(SharedRes.Strings.personal_info),
    EmergencyContact(SharedRes.Strings.emergency_contact),
    SkillQualification(SharedRes.Strings.skill_and_qualification);

    companion object {
        private val typeMap =
            enumValues<InfoList>().associateBy { it.title }

        fun get(typeName: StringResource): InfoList =
            InfoList.typeMap[typeName] ?: PersonalInfo

        val list: List<InfoList>
            get() = entries.toList().map { it }
    }
}


