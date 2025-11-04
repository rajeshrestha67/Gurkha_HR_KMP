package com.gurkha.hr.profile.edit_profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurkha.hr.domain.form.RequiredValidationUseCase
import com.gurkha.hr.domain.userDetail.mapper.toDomain
import com.gurkha.hr.domain.userDetail.mapper.toLocal
import com.gurkha.hr.domain.userDetail.mapper.toUI
import com.gurkha.hr.domain.userDetail.usecase.FetchUserDetailUseCase
import com.gurkha.hr.domain.userDetail.usecase.UpdateUserDetailUseCase
import com.gurkha.hr.networkhelper.onError
import com.gurkha.hr.networkhelper.onSuccess
import com.gurkha.hr.profile.model.edit_profile_screen.EditProfileScreenState
import com.gurkha.hr.profile.model.edit_profile_screen.EditProfileViewAction
import com.gurkha.model.network.toErrorMessage
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditProfileViewModel(
    private val requiredValidationUseCase: RequiredValidationUseCase,
    private val fetchUserDetailUseCase: FetchUserDetailUseCase,
    private val updateUserDetailUseCase: UpdateUserDetailUseCase,

) : ViewModel() {
    private val _state = MutableStateFlow(EditProfileScreenState())

    private val _successChannel = Channel<String>()
    val successChannel = _successChannel.receiveAsFlow()

    private val _errorChannel = Channel<String>()
    val errorChannel = _errorChannel.receiveAsFlow()


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
                        profileSummaryList = it.profileSummaryList?.copy(
                            employeeTypes = action.employeeType.toString()
                        )
                    )
                }
            }

            is EditProfileViewAction.DateOfBirth -> {
                _state.update {
                    it.copy(
                        profileSummaryList = it.profileSummaryList?.copy(
                            dateOfBirth = action.date
                        )
                    )
                }
            }

            is EditProfileViewAction.JoinedDate -> {
                _state.update {
                    it.copy(
                        profileSummaryList = it.profileSummaryList?.copy(
                            joinedDate = action.date
                        )
                    )
                }
            }

            is EditProfileViewAction.OnBloodGroupChanged -> {
                _state.update {
                    it.copy(
                        profileSummaryList = it.profileSummaryList?.copy(
                            bloodGroup = action.bloodGroup
                        ),

                        )
                }
            }

            is EditProfileViewAction.OnGuardianNameChanged -> {
                _state.update {
                    it.copy(
                        profileSummaryList = it.profileSummaryList?.copy(
                            guardianName = action.guardianName
                        ),
                    )
                }
            }

            is EditProfileViewAction.OnGuardianPhoneChanged -> {
                _state.update {
                    it.copy(
                        profileSummaryList = it.profileSummaryList?.copy(
                            guardianPhone = action.guardianPhone
                        ),

                        )
                }
            }

            is EditProfileViewAction.PFNumberChanged -> {
                _state.update {
                    it.copy(
                        profileSummaryList = it.profileSummaryList?.copy(
                            pfNumber = action.pfNumber
                        ),

                        )
                }
            }

            is EditProfileViewAction.PANNumberChanged -> {
                _state.update {
                    it.copy(
                        profileSummaryList = it.profileSummaryList?.copy(
                            panNumber = action.panNumber
                        ),
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
        _state.update {
            it.copy(
                isUpdating = true
            )
        }
        state.value.profileSummaryList?.toDomain()?.let { domain ->

            updateUserDetailUseCase(
                data = domain
            ).onSuccess { data ->
                _state.update {
                    it.copy(
                        isUpdating = false
                    )
                }
                _successChannel.send(data.message)
            }.onError { error ->
                _state.update {
                    it.copy(
                        isUpdating = false,
                    )
                }
                _errorChannel.send(error.toErrorMessage())
            }

        }
    }

}