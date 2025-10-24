package com.gurkha.di

import com.gurkha.hr.data.attendance.KtorAttendanceRemoteRepository
import com.gurkha.hr.data.notification.KtorNotificationRemoteRepository
import com.gurkha.hr.data.upComingBirthday.KtorUpComingBirthdayRemoteRepository
import com.gurkha.hr.data.upComingEvent.KtorEventRemoteRepository
import com.gurkha.hr.data.upComingWorkAnniversary.KtorUpComingWorkAnniversaryRemoteRepository
import com.gurkha.hr.data.userDetail.KtorUserDetailRemoteRepository
import com.gurkha.hr.datastore.notificationCount.local.NotificationCountDataStore
import com.gurkha.hr.datastore.notificationCount.repository.LocalNotificationCountDataRepository
import com.gurkha.hr.datastore.notificationCount.repository.NotificationCountDataRepository
import com.gurkha.hr.datastore.user_data.local.UserDataDataStore
import com.gurkha.hr.datastore.user_data.repository.LocalUserDataRepository
import com.gurkha.hr.datastore.user_data.repository.UserDataRepository
import com.gurkha.hr.date.data.model.CalendarModel
import com.gurkha.hr.date.data.model.CalendarModelImpl
import com.gurkha.hr.domain.attendance.attendanceReport.repository.AttendanceRemoteRepository
import com.gurkha.hr.domain.attendance.attendanceReport.usecase.AttendanceUseCase
import com.gurkha.hr.domain.notification.notificationCount.useCase.NotificationCountUseCase
import com.gurkha.hr.domain.notification.notificationData.repository.NotificationRemoteRepository
import com.gurkha.hr.domain.notification.notificationData.useCase.NotificationUseCase
import com.gurkha.hr.domain.upComingBirthday.repository.UpComingBirthdayRemoteRepository
import com.gurkha.hr.domain.upComingBirthday.usecase.UpComingBirthdayUseCase
import com.gurkha.hr.domain.upComingEvent.repository.EventRemoteRepository
import com.gurkha.hr.domain.upComingEvent.useCase.EventUseCase
import com.gurkha.hr.domain.upComingWorkAnniversaries.repository.UpComingWorkAnniversaryRemoteRepository
import com.gurkha.hr.domain.upComingWorkAnniversaries.useCase.UpComingWorkAnniversaryUseCase
import com.gurkha.hr.domain.userDetail.repository.UserDetailRemoteRepository
import com.gurkha.hr.domain.userDetail.usecase.FetchUserDetailUseCase
import com.gurkha.hr.home.HomeScreenViewModel
import com.gurkha.hr.notification.NotificationViewModel
import com.gurkha.hr.viewAllScreen.ViewAllScreenViewModel
import io.ktor.client.HttpClient
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module

@Module
class HomeScreenModule {
    @Factory(binds = [AttendanceRemoteRepository::class])
    fun attendanceRepository(httpClient: HttpClient) = KtorAttendanceRemoteRepository(httpClient)

    @Factory(binds = [EventRemoteRepository::class])
    fun eventRemoteRepository(httpClient: HttpClient) = KtorEventRemoteRepository(httpClient)

    @Factory(binds = [CalendarModel::class])
    fun getCalendarModel() = CalendarModelImpl()

    @Factory(binds = [UserDetailRemoteRepository::class])
    fun userDetailRemoteRepository(httpClient: HttpClient) =
        KtorUserDetailRemoteRepository(httpClient)

    @Factory(binds = [UpComingBirthdayRemoteRepository::class])
    fun upComingBirthdayRemoteRepository(httpClient: HttpClient) =
        KtorUpComingBirthdayRemoteRepository(httpClient)

    @Factory(binds = [UpComingWorkAnniversaryRemoteRepository::class])
    fun upComingWorkAnniversaryRemoteRepository(httpClient: HttpClient) =
        KtorUpComingWorkAnniversaryRemoteRepository(httpClient)

    @Factory(binds = [NotificationRemoteRepository::class])
    fun notificationRemoteRepository(httpClient: HttpClient) =
        KtorNotificationRemoteRepository(httpClient)


    @Factory
    fun notificationUseCase(
        notificationRemoteRepository: NotificationRemoteRepository,
    ): NotificationUseCase = NotificationUseCase(
        notificationRemoteRepository = notificationRemoteRepository,
    )

    @Factory(binds = [UserDataRepository::class])
    fun notificationCountDataRepository(
        notificationCountDataStore: NotificationCountDataStore
    ): NotificationCountDataRepository =
        LocalNotificationCountDataRepository(
            notificationCountDataStore = notificationCountDataStore
        )


    @Factory
    fun notificationCountUseCase(
        notificationRemoteRepository: NotificationRemoteRepository,
        notificationCountDataRepository:NotificationCountDataRepository
    ): NotificationCountUseCase = NotificationCountUseCase(
        notificationRemoteRepository = notificationRemoteRepository,
        notificationCountDataRepository = notificationCountDataRepository
    )

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
    fun eventUseCase(eventRemoteRepository: EventRemoteRepository): EventUseCase =
        EventUseCase(eventRemoteRepository = eventRemoteRepository)

    @Factory
    fun fetchUserDetailUseCase(
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
        upComingWorkAnniversaryUseCase: UpComingWorkAnniversaryUseCase,
        calendarModel: CalendarModel,
        eventUseCase: EventUseCase,
        notificationUseCase: NotificationUseCase,
        notificationCountUseCase: NotificationCountUseCase
    ): HomeScreenViewModel = HomeScreenViewModel(
        attendanceUseCase = attendanceUseCase,
        userDetailUseCase = userDetailUseCase,
        upComingBirthdayUseCase = upComingBirthdayUseCase,
        upComingWorkAnniversaryUseCase = upComingWorkAnniversaryUseCase,
        calendarModel = calendarModel,
        eventUseCase = eventUseCase,
        notificationCountUseCase = notificationCountUseCase,
        notificationUseCase = notificationUseCase
    )

    @KoinViewModel
    fun getViewAllViewModel(): ViewAllScreenViewModel = ViewAllScreenViewModel()

    @KoinViewModel
    fun getNotificationViewModel(
        notificationUseCase: NotificationUseCase
    ): NotificationViewModel= NotificationViewModel(
        notificationUseCase = notificationUseCase
    )
}


