package com.gurkha.hr.profile.model.profileinfo_screen

import com.gurkha.hr.res.SharedRes

data class ProfileInfoScreenState(
    val contactInformation: List<ProfileInfo> = listOf(
        ProfileInfo(
            name = SharedRes.Strings.email,
            value = "test@gmail.com2"
        ),
        ProfileInfo(
            name = SharedRes.Strings.phone,
            value = "9866290535"
        ),
        ProfileInfo(
            name = SharedRes.Strings.address,
            value = "baneshwor"
        )
    ),
    val personalDetailsInfo: List<ProfileInfo> = listOf(
        ProfileInfo(
            name = SharedRes.Strings.date_of_birth,
            value = "2062-02-26"
        ),
        ProfileInfo(
            name = SharedRes.Strings.gender,
            value = "Male"
        ),
        ProfileInfo(
            name = SharedRes.Strings.nationality,
            value = "N/A"
        ),
        ProfileInfo(
            name = SharedRes.Strings.marital_status,
            value = "UNMARRIED"
        )

    ),
    val guardianInformation: List<ProfileInfo> = listOf(
        ProfileInfo(
            name = SharedRes.Strings.guardian_name,
            value = "N/A"
        ),
        ProfileInfo(
            name = SharedRes.Strings.guardian_phone,
            value = "N/A"
        )
    ),
    val selectedTab:Int = 0
)