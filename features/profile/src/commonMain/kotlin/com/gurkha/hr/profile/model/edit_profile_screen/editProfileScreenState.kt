package com.gurkha.hr.profile.model.edit_profile_screen

import com.gurkha.hr.components.date.DateData
import com.gurkha.hr.domain.userDetail.model.UserDetailData
import com.gurkha.hr.domain.userDetail.ui.EditProfileUI
import com.gurkha.hr.res.SharedRes
import org.jetbrains.compose.resources.StringResource

data class EditProfileScreenState(
    val selectedTab: Int = 0,
    val isLoading: Boolean = false,
    val employeeId: Int = 0,
    val profileSummaryList: EditProfileUI? = null,
    val itemList: List<DropDownItems> = DropDownItems.list,

    val employeeTypeList: DropDownItems = DropDownItems.list[0],
    val bloodGroup: String = "",
    val guardianName: String = "",
    val guardianNumber: String = "",
    val panNumber: String = "",
    val pfNumber: String = "",

    val dateOfBirth: String? = "",
    val joinedDate: String = "",
    val dob: DateData? = null,
    val joinDate: DateData? = null,

    val isSubmitSuccess: Boolean = false,
    val dobError: StringResource? = null,
    val joinDateError: StringResource? = null,

)






