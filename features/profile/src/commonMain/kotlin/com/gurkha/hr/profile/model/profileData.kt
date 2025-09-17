package com.gurkha.hr.profile.model

import com.gurkha.hr.res.SharedRes
import org.jetbrains.compose.resources.StringResource

data class ScreenItem(
    val title: StringResource
)
//
//object ScreenList {
//    val general = listOf(
//        ScreenItem(title = SharedRes.Strings.profile),
//        ScreenItem(title = SharedRes.Strings.allocated_leave),
//        ScreenItem(title = SharedRes.Strings.time_and_attendance),
//        ScreenItem(title = SharedRes.Strings.document),
//        ScreenItem(title = SharedRes.Strings.company_assets),
//        ScreenItem(title = SharedRes.Strings.history),
//    )
//
//    val account = listOf(
//        ScreenItem(title = SharedRes.Strings.terms_and_services),
//        ScreenItem(title = SharedRes.Strings.privacy_policy),
//        ScreenItem(title = SharedRes.Strings.fac),
//        ScreenItem(title = SharedRes.Strings.support),
//        ScreenItem(title = SharedRes.Strings.setting),
//
//
//
//
//    )
//}


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
            get() = AccountList.entries.toList().map { it }
    }
}

