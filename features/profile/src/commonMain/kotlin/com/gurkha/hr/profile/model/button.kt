package com.gurkha.hr.profile.model

import com.gurkha.hr.res.SharedRes
import gurkhahr.features.profile.generated.resources.Res
import org.jetbrains.compose.resources.StringResource


data class Screen(
    val title: StringResource,
)

object ScreenList {
    val screenList = listOf(
        Screen(title = SharedRes.Strings.profile),
        Screen(title = SharedRes.Strings.allocated_leave),
        Screen(title = SharedRes.Strings.time_and_attendance),
        Screen(title = SharedRes.Strings.document),
        Screen(title = SharedRes.Strings.company_assets),
        Screen(title = SharedRes.Strings.history)
    )

}