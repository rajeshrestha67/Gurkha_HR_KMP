package com.gurkha.hr.profile.profile_info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurkha.hr.domain.userDetail.usecase.FetchUserDetailUseCase
import com.gurkha.hr.networkhelper.onError
import com.gurkha.hr.networkhelper.onSuccess
import com.gurkha.hr.profile.model.profileinfo_screen.ProfileInfo
import com.gurkha.hr.profile.model.profileinfo_screen.ProfileInfoScreenState
import com.gurkha.hr.profile.model.profileinfo_screen.ProfileInfoViewAction
import com.gurkha.hr.res.SharedRes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileInfoScreenViewModel(
    private val fetchUserDetailUseCase: FetchUserDetailUseCase

) : ViewModel() {
    private val _state = MutableStateFlow(ProfileInfoScreenState())
    val state = _state
        .onStart {
            fetchUserDetails(isRefreshing = false)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ProfileInfoScreenState()
        )


    fun action(action: ProfileInfoViewAction) {
        when (action) {
            is ProfileInfoViewAction.OnItemSelected -> {
                _state.update {
                    it.copy(
                        selectedTab = action.index
                    )
                }
            }

            is ProfileInfoViewAction.OnRefresh -> {
                refresh()
            }
        }
    }

    private fun fetchUserDetails(isRefreshing: Boolean) = viewModelScope.launch {
        _state.update {
            it.copy(
                isProfileLoading = true
            )
        }

        fetchUserDetailUseCase(force = isRefreshing)
            .onSuccess { userDetail ->
                _state.update {
                    it.copy(
                        isRefreshing = false,
                        userProfileUrl = userDetail.imageUrl,
                        isProfileLoading = false,
                        fullName = userDetail.fullName,
                        initials = userDetail.initials,
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
                                value = userDetail.dateOfBirth.ifEmpty { "N/A" }
                            ),
                            ProfileInfo(
                                name = SharedRes.Strings.gender,
                                value = userDetail.gender
                            ),
                            ProfileInfo(
                                name = SharedRes.Strings.nationality,
                                value = userDetail.nationality.ifEmpty { "N/A" }
                            ),
                            ProfileInfo(
                                name = SharedRes.Strings.marital_status,
                                value = userDetail.maritalStatus
                            )
                        ),
                        guardianInfo = listOf(
                            ProfileInfo(
                                name = SharedRes.Strings.guardian_name,
                                value = userDetail.guardianNumber.ifEmpty { "N/A" }
                            ),
                            ProfileInfo(
                                name = SharedRes.Strings.guardian_phone,
                                value = userDetail.guardianNumber.ifEmpty { "N/A" }
                            )
                        )
                    )
                }
            }.onError {
                _state.update {
                    it.copy(
                        isProfileLoading = false
                    )
                }
            }
    }

    private fun refresh() = viewModelScope.launch {
        _state.update {
            it.copy(
                isRefreshing = true
            )
        }
        fetchUserDetails(isRefreshing = true)
    }


}