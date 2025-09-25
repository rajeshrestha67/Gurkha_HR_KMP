package com.gurkha.hr.profile.model.profile_screen

import com.gurkha.hr.domain.userDetail.model.UserDetailData

data class ProfileScreenState (
    val generalItems: List<GeneralList> = GeneralList.list,
    val accountItems: List<AccountList> = AccountList.list,
    val isProfileLoading: Boolean = false,
    val fullName: String = "",
    val levelName: String = "",
    val userProfileUrl: String? = null,
    val phoneNumber: String = "",


    val userDetail: UserDetailData? = null,
)
