package com.gurkha.hr.profile.profile_info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurkha.hr.domain.userDetail.usecase.FetchLocalUserDetailUseCase
import com.gurkha.hr.domain.userDetail.usecase.FetchRemoteUserDetailUseCase
import com.gurkha.hr.networkhelper.onError
import com.gurkha.hr.networkhelper.onSuccess
import com.gurkha.hr.profile.model.profileinfo_screen.ProfileInfo
import com.gurkha.hr.profile.model.profileinfo_screen.ProfileInfoScreenState
import com.gurkha.hr.profile.model.profileinfo_screen.ProfileInfoViewAction
import com.gurkha.hr.res.SharedRes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileInfoScreenViewModel(
    private val userDetailUseCase: FetchLocalUserDetailUseCase

) : ViewModel() {
    private val _state = MutableStateFlow(ProfileInfoScreenState())
    val state = _state
        .combine(userDetailUseCase()){ data, userDetail ->
            data.copy(
                fullName = userDetail.fullName,
                levelName = userDetail.levelName,
                employeeId = userDetail.employeeId,
                branchName = userDetail.branchName,
                address = userDetail.address,
                joinedDate = userDetail.joinedDate,
                contactInfo = listOf(
                    ProfileInfo(
                        name = SharedRes.Strings.email,
                        value = userDetail.email
                    ),
                    ProfileInfo(
                        name = SharedRes.Strings.phone,
                        value = userDetail.phoneNumber
                    ),
                    ProfileInfo(
                        name = SharedRes.Strings.address,
                        value = userDetail.address
                    )
                ),
                personalDetails = listOf(
                    ProfileInfo(
                        name = SharedRes.Strings.date_of_birth,
                        value = userDetail.dateOfBirth
                    ),
                    ProfileInfo(
                        name = SharedRes.Strings.gender,
                        value = userDetail.gender
                    )  ,
                    ProfileInfo(
                        name = SharedRes.Strings.nationality,
                        value = userDetail.nationality.ifEmpty { "N/A" }
                    ) ,
                    ProfileInfo(
                        name = SharedRes.Strings.marital_status,
                        value = userDetail.maritalStatus
                    )
                ),
                guardianInfo = listOf(
                    ProfileInfo(
                        name = SharedRes.Strings.guardian_name,
                        value = userDetail.guardianPhone.ifEmpty { "N/A" }
                    ),
                    ProfileInfo(
                        name = SharedRes.Strings.guardian_phone,
                        value = userDetail.guardianPhone.ifEmpty { "N/A" }
                    )
                )
            )
        }
        .onStart {
            action(ProfileInfoViewAction.OnFetchData)
        }
        .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ProfileInfoScreenState()
    )


    fun action(action: ProfileInfoViewAction) {
        when (action) {
            is ProfileInfoViewAction.OnFetchData -> {
                //fetchUserDetails()
            }
            is ProfileInfoViewAction.OnItemSelected -> {
                _state.update {
                    it.copy(
                        selectedTab = action.index
                    )
                }
            }
        }
    }
//    private fun fetchUserDetails()= viewModelScope.launch{
//        _state.update {
//            it.copy(
//                isProfileLoading = true
//            )
//        }
//        userDetailUseCase().onSuccess { data ->
////            _state.update {
////                it.copy(
////                    isProfileLoading = false,
////                    fullName = data.fullName,
////                    levelName = data.levelName,
////                    userProfileUrl = data.userProfileUrl,
////                    phoneNumber = data.phoneNumber,
////                    id = data.employeeId,
////                    email = data.email,
////                    address = data.address,
////                    dob = data.dateOfBirth,
////                    gender = data.gender,
////                    nationality = data.nationality.ifEmpty { "N/A" },
////                    maritalStatus = data.maritalStatus,
////                    guardianPhone = data.guardianPhone.ifEmpty{"N/A"},
////                    guardianName = data.guardianName.ifEmpty {"N/A" },
////                    branchName = data.branchName,
////                    joinedDate = data.joinedDate
////                )
////
////            }
//
//            _state.update {
//                it.copy(
//                    branchName = data.branchName,
//                    address = data.address,
//                    joinedDate = data.joinedDate,
//                    contactInfo = listOf(
//                        ProfileInfo(
//                            name = SharedRes.Strings.email,
//                            value = data.email
//                        ),
//                        ProfileInfo(
//                            name = SharedRes.Strings.phone,
//                            value = data.phoneNumber
//                        ),
//                        ProfileInfo(
//                            name = SharedRes.Strings.address,
//                            value = data.address
//                        )
//                    ),
//                    personalDetails = listOf(
//                        ProfileInfo(
//                            name = SharedRes.Strings.date_of_birth,
//                            value = data.dateOfBirth
//                        ),
//                        ProfileInfo(
//                            name = SharedRes.Strings.gender,
//                            value = data.gender
//                        )  ,
//                        ProfileInfo(
//                            name = SharedRes.Strings.nationality,
//                            value = data.nationality
//                        ) ,
//                        ProfileInfo(
//                            name = SharedRes.Strings.marital_status,
//                            value = data.maritalStatus
//                        )
//                    ),
//                    guardianInfo = listOf(
//                        ProfileInfo(
//                            name = SharedRes.Strings.guardian_name,
//                            value = data.guardianPhone
//                        ),
//                        ProfileInfo(
//                            name = SharedRes.Strings.guardian_phone,
//                            value = data.guardianPhone
//                        )
//                    )
//                )
//            }
//        }.onError {
//            _state.update {
//                it.copy(
//                    isProfileLoading = false
//                )
//            }
//        }
//
//    }



}