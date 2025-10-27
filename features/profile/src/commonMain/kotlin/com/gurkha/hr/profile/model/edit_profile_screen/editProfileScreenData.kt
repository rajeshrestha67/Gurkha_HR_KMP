package com.gurkha.hr.profile.model.edit_profile_screen

import com.gurkha.hr.res.SharedRes
import org.jetbrains.compose.resources.StringResource

enum class Title(val title: StringResource) {
    PersonalDetails(SharedRes.Strings.personal),
    OthersDetails(SharedRes.Strings.others);

    companion object{
        val list = entries.toList()
    }

}

enum class DropDownItems(val dropDownItems: StringResource){
    Contract(SharedRes.Strings.labelContract),
    Probation(SharedRes.Strings.labelProbation),
    Permanent(SharedRes.Strings.labelPermanent);

    companion object {
        private val typeMap =
            enumValues<DropDownItems>().associateBy { it.dropDownItems }

        fun get(typeName: StringResource): DropDownItems =
            DropDownItems.typeMap[typeName] ?: Contract

        val list: List<DropDownItems>
            get() = DropDownItems.entries.toList().map { it }
    }
}