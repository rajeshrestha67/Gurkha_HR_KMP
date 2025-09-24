package com.gurkha.hr.settings.model
import org.jetbrains.compose.resources.StringResource
import com.gurkha.hr.res.SharedRes



enum class SettingList(val title: StringResource){
    ChangePassword(title = SharedRes.Strings.change_password),
    Theme(title = SharedRes.Strings.theme),
    Language(title = SharedRes.Strings.language);


    companion object {
        private val typeMap =
            enumValues<SettingList>().associateBy { it.title }

        fun get(typeName: StringResource): SettingList =
            SettingList.typeMap[typeName] ?: ChangePassword

        val list: List<SettingList>
            get() = SettingList.entries.toList().map { it }
    }
}



