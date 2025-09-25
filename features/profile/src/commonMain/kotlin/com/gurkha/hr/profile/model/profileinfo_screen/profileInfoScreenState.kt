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


//    val contactInformation:(ProfileInfoScreenState)-> List<ProfileInfo> ={state ->listOf(
//        ProfileInfo(
//            name = SharedRes.Strings.email,
//            value = state.email
//        ),
//        ProfileInfo(
//            name = SharedRes.Strings.phone,
//            value = state.phoneNumber
//        ),
//        ProfileInfo(
//            name = SharedRes.Strings.address,
//            value = state.address
//        )
//    ) },
//    val personalDetailsInfo: List<ProfileInfo> = listOf(
//        ProfileInfo(
//            name = SharedRes.Strings.date_of_birth,
//            value = "2062-02-26"
//        ),
//        ProfileInfo(
//            name = SharedRes.Strings.gender,
//            value = "Male"
//        ),
//        ProfileInfo(
//            name = SharedRes.Strings.nationality,
//            value = "N/A"
//        ),
//        ProfileInfo(
//            name = SharedRes.Strings.marital_status,
//            value = "UNMARRIED"
//        )
//
//    ),
//    val guardianInformation: List<ProfileInfo> = listOf(
//        ProfileInfo(
//            name = SharedRes.Strings.guardian_name,
//            value = "N/A"
//        ),
//        ProfileInfo(
//            name = SharedRes.Strings.guardian_phone,
//            value = "N/A"
//        )
//    ),
    val selectedTab:Int = 0
)