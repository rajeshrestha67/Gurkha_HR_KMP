package com.gurkha.hr.profile.model.edit_profile_screen

import com.gurkha.hr.domain.userDetail.ui.EditProfileUI
import org.jetbrains.compose.resources.StringResource

data class EditProfileScreenState(
    val selectedTab: Int = 0,
    val isLoading: Boolean = false,
    val employeeId: Int = 0,
    val profileSummaryList: EditProfileUI? = null,
    val itemList: List<DropDownItems> = DropDownItems.list,

    val isSubmitSuccess: Boolean = false,
    val joinDateError: StringResource? = null,

    val isUpdating: Boolean = false
)






