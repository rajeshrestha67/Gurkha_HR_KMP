package com.gurkha.di

import com.gurkha.hr.components.permissions.ProgressNotification
import com.gurkha.hr.data.userDetail.KtorUserDetailRemoteRepository
import com.gurkha.hr.datastore.user_data.local.UserDataDataStore
import com.gurkha.hr.datastore.user_data.repository.LocalUserDataRepository
import com.gurkha.hr.datastore.user_data.repository.UserDataRepository
import com.gurkha.hr.domain.imageUpload.ImageUploadRepository
import com.gurkha.hr.domain.uploadImage.EmployeeImageUpload
import com.gurkha.hr.domain.uploadImage.UploadImageUseCase
import com.gurkha.hr.domain.userDetail.repository.UserDetailRemoteRepository
import com.gurkha.hr.domain.userDetail.usecase.FetchUserDetailUseCase
import com.gurkha.hr.domain.userDetail.usecase.UpdateUserDetailUseCase
import com.gurkha.hr.profile.document.DocumentScreenViewModel
import com.gurkha.hr.profile.profile_screen.ProfileScreenViewModel
import io.ktor.client.HttpClient
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module

@Module
class ProfileScreenModule {

    @Factory(binds = [UserDetailRemoteRepository::class])
    fun userDetailRemoteRepository(httpClient: HttpClient) =
        KtorUserDetailRemoteRepository(httpClient)

    @Factory(binds = [UserDataRepository::class])
    fun userDataRepository(
        userDataDataStore: UserDataDataStore
    ): UserDataRepository =
        LocalUserDataRepository(
            userDataDataStore = userDataDataStore
        )

    @Factory
    fun userDetailUseCase(
        userDetailRemoteRepository: UserDetailRemoteRepository,
        userDataRepository: UserDataRepository
    ): FetchUserDetailUseCase =
        FetchUserDetailUseCase(
            userDetailRemoteRepository, userDataRepository = userDataRepository
        )


    @Factory
    fun updateUserDetailUseCase(
        userDetailRemoteRepository: UserDetailRemoteRepository,
        userDataRepository : UserDataRepository
    ): UpdateUserDetailUseCase=UpdateUserDetailUseCase(
        userDetailRemoteRepository = userDetailRemoteRepository,
        userDataRepository = userDataRepository
    )


    @Factory
    fun employeeImageUpload(
        userDataRepository: UserDataRepository,
        imageUploadRepository: ImageUploadRepository,
    ): EmployeeImageUpload = EmployeeImageUpload(
        userDataRepository = userDataRepository,
        imageUploadRepository = imageUploadRepository,
    )


    @KoinViewModel
    fun getProfileScreenViewModel(
        userDetailUseCase: FetchUserDetailUseCase,
        uploadImageUseCase: UploadImageUseCase,
    ): ProfileScreenViewModel = ProfileScreenViewModel(
        userDetailUseCase = userDetailUseCase,
        uploadImageUseCase = uploadImageUseCase,
    )


    @KoinViewModel
    fun getDocumentScreenViewModel(
        uploadImageUseCase: UploadImageUseCase,
        employeeImageUpload: EmployeeImageUpload
    ): DocumentScreenViewModel = DocumentScreenViewModel(
        uploadImageUseCase = uploadImageUseCase,
        employeeImageUpload = employeeImageUpload,
    )
}