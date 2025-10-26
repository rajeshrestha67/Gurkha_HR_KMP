package com.gurkha.hr.profile.edit_profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurkha.hr.domain.form.RequiredValidationUseCase
import com.gurkha.hr.domain.userDetail.mapper.toDomain
import com.gurkha.hr.domain.userDetail.mapper.toUI
import com.gurkha.hr.domain.userDetail.usecase.FetchUserDetailUseCase
import com.gurkha.hr.domain.userDetail.usecase.UpdateUserDetailUseCase
import com.gurkha.hr.networkhelper.onSuccess
import com.gurkha.hr.profile.model.edit_profile_screen.EditProfileScreenState
import com.gurkha.hr.profile.model.edit_profile_screen.EditProfileViewAction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditProfileViewModel(
    private val requiredValidationUseCase: RequiredValidationUseCase,
    private val fetchUserDetailUseCase: FetchUserDetailUseCase,
    private val updateUserDetailUseCase: UpdateUserDetailUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(EditProfileScreenState())
    val state = _state
        .onStart {
            onFetchData()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = EditProfileScreenState()
        )

    private fun onFetchData(
    ) = viewModelScope.launch {
        _state.update { it.copy(isLoading = true) }

        fetchUserDetailUseCase()
            .onSuccess { userDetail ->
                println("ProfileInfoData $userDetail")
                _state.update {
                    it.copy(
                        isLoading = false,
                        employeeId = userDetail.employeeId,
                        profileSummaryList = userDetail.toUI(),
                    )
                }
            }
    }

    fun onAction(action: EditProfileViewAction) {
        when (action) {
            is EditProfileViewAction.OnItemSelected -> {
                _state.update {
                    it.copy(
                        selectedTab = action.index
                    )
                }
            }

            is EditProfileViewAction.EmployeeType -> {
                _state.update {
                    it.copy(
                        employeeTypeList = action.employeeType
                    )
                }
            }

            is EditProfileViewAction.DateOfBirth -> {
                _state.update {
                    it.copy(
                        dob = action.date,
                        dobError = null,
                        dateOfBirth = state.value.profileSummaryList?.dateOfBirth

                    )
                }
            }

            is EditProfileViewAction.JoinedDate -> {
                _state.update {
                    it.copy(
                        joinDate = action.date,
                        joinDateError = null
                    )
                }
            }

            is EditProfileViewAction.OnBloodGroupChanged -> {
                _state.update {
                    it.copy(
                        profileSummaryList = it.profileSummaryList?.copy(
                            bloodGroup = action.bloodGroup
                        ),
                        bloodGroup = action.bloodGroup

                    )
                }
            }

            is EditProfileViewAction.OnGuardianNameChanged -> {
                _state.update {
                    it.copy(
                        profileSummaryList = it.profileSummaryList?.copy(
                            guardianName = action.guardianName
                        ),
                        guardianName = action.guardianName
                    )
                }
            }

            is EditProfileViewAction.OnGuardianPhoneChanged -> {
                _state.update {
                    it.copy(
                        profileSummaryList = it.profileSummaryList?.copy(
                            guardianPhone = action.guardianPhone
                        ),
                        guardianNumber = action.guardianPhone

                    )
                }
            }

            is EditProfileViewAction.PFNumberChanged -> {
                _state.update {
                    it.copy(
                        profileSummaryList = it.profileSummaryList?.copy(
                            pfNumber = action.pfNumber
                        ),
                        pfNumber = action.pfNumber

                    )
                }
            }

            is EditProfileViewAction.PANNumberChanged -> {
                _state.update {
                    it.copy(
                        profileSummaryList = it.profileSummaryList?.copy(
                            panNumber = action.panNumber
                        ),
                        panNumber = action.panNumber

                    )
                }
            }

            is EditProfileViewAction.Submit -> {

                submit()
            }

            else -> Unit


        }
    }

    private fun submit(

    ) = viewModelScope.launch {
        println("Unsuccess")
        state.value.profileSummaryList?.let {
            println("Unsuccess2")
            updateUserDetailUseCase(
                data = state.value.profileSummaryList!!.toDomain()
            ).onSuccess {

            }
            _state.update {
                it.copy(
                    isLoading = false,
                    isSubmitSuccess = true
                )
            }
        }

    }

}