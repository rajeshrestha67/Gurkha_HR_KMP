package com.gurkha.hr.profile.model

import com.gurkha.hr.res.SharedRes
import org.jetbrains.compose.resources.StringResource

data class ScreenItem(
    val title: StringResource,
)

object ScreenList {
    val general = listOf(
        ScreenItem(title = SharedRes.Strings.profile),
        ScreenItem(title = SharedRes.Strings.allocated_leave),
        ScreenItem(title = SharedRes.Strings.time_and_attendance),
        ScreenItem(title = SharedRes.Strings.document),
        ScreenItem(title = SharedRes.Strings.company_assets),
        ScreenItem(title = SharedRes.Strings.history),
    )

    val account = listOf(
        ScreenItem(title = SharedRes.Strings.terms_and_services),
        ScreenItem(title = SharedRes.Strings.privacy_policy),
        ScreenItem(title = SharedRes.Strings.fac),
        ScreenItem(title = SharedRes.Strings.support),
        ScreenItem(title = SharedRes.Strings.setting),




    )
}
