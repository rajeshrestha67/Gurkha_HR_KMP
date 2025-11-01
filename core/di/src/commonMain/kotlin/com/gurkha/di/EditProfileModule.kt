package com.gurkha.di

import com.gurkha.hr.domain.form.RequiredValidationUseCase
import com.gurkha.hr.domain.userDetail.usecase.FetchUserDetailUseCase
import com.gurkha.hr.domain.userDetail.usecase.UpdateUserDetailUseCase
import com.gurkha.hr.profile.edit_profile.EditProfileViewModel
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Module

@Module
class EditProfileModule {


    @KoinViewModel
    fun getEditProfileScreenViewModel(
        fetchUserDetailUseCase: FetchUserDetailUseCase,
        requiredValidationUseCase: RequiredValidationUseCase,
        updateUserDetailUseCase: UpdateUserDetailUseCase,
    ): EditProfileViewModel = EditProfileViewModel(
        fetchUserDetailUseCase = fetchUserDetailUseCase,
        requiredValidationUseCase = requiredValidationUseCase,
        updateUserDetailUseCase = updateUserDetailUseCase,
    )
}