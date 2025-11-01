package com.gurkha.hr.profile.model.profileinfo_screen

import com.gurkha.hr.profile.model.profile_screen.AccountList
import com.gurkha.hr.profile.model.profile_screen.GeneralList

data class ProfileInfoScreenState(
    val generalItems: List<GeneralList> = GeneralList.list,
    val accountItems: List<AccountList> = AccountList.list,
    val fullName: String = "",
    val initials: String = "",
    val levelName: String = "",
    val userProfileUrl: String? = null,
    val phoneNumber: String = "",
    val employeeId: Int = 0,
    val branchName: String = "",
    val address: String = "",
    val joinedDate: String = "",
    val contactInfo: List<ProfileInfo> = emptyList(),
    val personalDetails: List<ProfileInfo> = emptyList(),
    val guardianInfo: List<ProfileInfo> = emptyList(),
    val infoList: List<InfoList> = InfoList.list,
    val isProfileLoading: Boolean = false,
    val selectedTab: InfoList = InfoList.PersonalInfo,

    val isRefreshing: Boolean = false
)

