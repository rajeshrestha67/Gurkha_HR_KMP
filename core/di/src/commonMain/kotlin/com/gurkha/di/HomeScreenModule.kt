package com.gurkha.di

import com.gurkha.hr.data.attendance.KtorAttendanceRemoteRepository
import com.gurkha.hr.data.upComingBirthday.KtorUpComingBirthdayRemoteRepository
import com.gurkha.hr.data.upComingWorkAnniversary.KtorUpComingWorkAnniversaryRemoteRepository
import com.gurkha.hr.data.userDetail.KtorUserDetailRemoteRepository
import com.gurkha.hr.datastore.user_data.local.UserDataDataStore
import com.gurkha.hr.datastore.user_data.repository.LocalUserDataRepository
import com.gurkha.hr.datastore.user_data.repository.UserDataRepository
import com.gurkha.hr.domain.attendance.attendanceReport.repository.AttendanceRemoteRepository
import com.gurkha.hr.domain.attendance.attendanceReport.usecase.AttendanceUseCase
import com.gurkha.hr.domain.upComingBirthday.repository.UpComingBirthdayRemoteRepository
import com.gurkha.hr.domain.upComingBirthday.usecase.UpComingBirthdayUseCase
import com.gurkha.hr.domain.upComingWorkAnniversaries.repository.UpComingWorkAnniversaryRemoteRepository
import com.gurkha.hr.domain.upComingWorkAnniversaries.useCase.UpComingWorkAnniversaryUseCase
import com.gurkha.hr.domain.userDetail.repository.UserDetailRemoteRepository
import com.gurkha.hr.domain.userDetail.usecase.FetchUserDetailUseCase
import com.gurkha.hr.home.HomeScreenViewModel
import io.ktor.client.HttpClient
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module

@Module
class HomeScreenModule {
    @Factory(binds = [AttendanceRemoteRepository::class])
    fun attendanceRepository(httpClient: HttpClient) = KtorAttendanceRemoteRepository(httpClient)

    @Factory(binds = [UserDetailRemoteRepository::class])
    fun userDetailRemoteRepository(httpClient: HttpClient) =
        KtorUserDetailRemoteRepository(httpClient)

    @Factory(binds = [UpComingBirthdayRemoteRepository::class])
    fun upComingBirthdayRemoteRepository(httpClient: HttpClient) =
        KtorUpComingBirthdayRemoteRepository(httpClient)

    @Factory(binds = [UpComingWorkAnniversaryRemoteRepository::class])
    fun upComingWorkAnniversaryRemoteRepository(httpClient: HttpClient) =
        KtorUpComingWorkAnniversaryRemoteRepository(httpClient)

    @Factory(binds = [UserDataRepository::class])
    fun userDataRepository(
        userDataDataStore: UserDataDataStore
    ): UserDataRepository =
        LocalUserDataRepository(
            userDataDataStore = userDataDataStore
        )


    @Factory
    fun attendanceUseCase(attendanceRemoteRepository: AttendanceRemoteRepository): AttendanceUseCase =
        AttendanceUseCase(attendanceRemoteRepository)

    @Factory
    fun userDetailUseCase(
        userDetailRemoteRepository: UserDetailRemoteRepository,
        userDataRepository: UserDataRepository
    ): FetchUserDetailUseCase =
        FetchUserDetailUseCase(
            userDetailRemoteRepository, userDataRepository = userDataRepository
        )

    @Factory
    fun upComingBirthdayUseCase(upComingBirthdayRemoteRepository: UpComingBirthdayRemoteRepository): UpComingBirthdayUseCase =
        UpComingBirthdayUseCase(upComingBirthdayRemoteRepository)

    @Factory
    fun upComingWorkAnniversaryUseCase(upComingWorkAnniversaryRemoteRepository: UpComingWorkAnniversaryRemoteRepository): UpComingWorkAnniversaryUseCase =
        UpComingWorkAnniversaryUseCase(upComingWorkAnniversaryRemoteRepository)

    @KoinViewModel
    fun getHomeScreenViewModel(
        attendanceUseCase: AttendanceUseCase,
        userDetailUseCase: FetchUserDetailUseCase,
        upComingBirthdayUseCase: UpComingBirthdayUseCase,
        upComingWorkAnniversaryUseCase: UpComingWorkAnniversaryUseCase
    ): HomeScreenViewModel = HomeScreenViewModel(
        attendanceUseCase = attendanceUseCase,
        userDetailUseCase = userDetailUseCase,
        upComingBirthdayUseCase = upComingBirthdayUseCase,
        upComingWorkAnniversaryUseCase = upComingWorkAnniversaryUseCase
    )
}


