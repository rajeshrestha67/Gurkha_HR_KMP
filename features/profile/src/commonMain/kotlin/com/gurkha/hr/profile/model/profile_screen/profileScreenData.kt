package com.gurkha.hr.profile.model.profile_screen

import com.gurkha.hr.res.SharedRes
import org.jetbrains.compose.resources.StringResource

enum class GeneralList(val title: StringResource) {
    Profile(SharedRes.Strings.profile),
    AllocatedLeave(SharedRes.Strings.allocated_leave),
    TimeAndAttendance(SharedRes.Strings.time_and_attendance),
    Document(SharedRes.Strings.document),
    CompanyAssets(SharedRes.Strings.company_assets),
    History(SharedRes.Strings.history);

    companion object {
        private val typeMap =
            enumValues<GeneralList>().associateBy { it.title }

        fun get(typeName: StringResource): GeneralList =
            GeneralList.typeMap[typeName] ?: Profile

        val list: List<GeneralList>
            get() = entries.toList().map { it }
    }
}


enum class AccountList(val title: StringResource){
    TermsAndServices(title = SharedRes.Strings.terms_and_services),
    PrivacyPolicy(title = SharedRes.Strings.privacy_policy),
    FAC(title = SharedRes.Strings.faq),
    Support(title = SharedRes.Strings.support),
    Settings(title = SharedRes.Strings.setting);
    companion object {
        private val typeMap =
            enumValues<AccountList>().associateBy { it.title }

        fun get(typeName: StringResource): AccountList =
            AccountList.typeMap[typeName] ?: TermsAndServices

        val list: List<AccountList>
            get() = entries.toList().map { it }
    }
}

