package com.gurkha.hr.profile.model.profileinfo_screen

import com.gurkha.hr.domain.userDetail.model.UserDetailData
import com.gurkha.hr.profile.model.profile_screen.AccountList
import com.gurkha.hr.profile.model.profile_screen.GeneralList
import com.gurkha.hr.res.SharedRes

data class ProfileInfoScreenState(
    val generalItems: List<GeneralList> = GeneralList.list,
    val accountItems: List<AccountList> = AccountList.list,
    val isProfileLoading: Boolean = false,
    val fullName: String = "",
    val levelName: String = "",
    val userProfileUrl: String? = null,
    val phoneNumber: String = "",
    val employeeId: String = "",
    val branchName:String = "",
    val address:String = "",
    val joinedDate: String = "",
    var contactInfo: List<ProfileInfo> = emptyList(),
    var personalDetails: List<ProfileInfo> = emptyList(),
    var guardianInfo: List<ProfileInfo> = emptyList(),


    val selectedTab:Int = 0
)