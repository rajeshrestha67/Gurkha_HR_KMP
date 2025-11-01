package com.gurkha.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.ksp.generated.module


fun initKoin(config: KoinAppDeclaration? = null) {

    startKoin {
        config?.invoke(this)
        modules(
            NetworkModule().module,
            NotificationModule().module,
            AuthModule().module,
            PlatformModule().module,
            DataStoreModule().module,
            FormModule().module,
            SplashScreenModule().module,
            DashboardScreenModule().module,
            HomeScreenModule().module,
            LeaveScreenModule().module,
            ProfileScreenModule().module,
            ChangePasswordModule().module,
            HomeScreenModule().module,
            ProfileInfoScreenModule().module,
            ChatModule().module,
            AttendanceScreenModule().module,
            AllocatedLeaveModule().module,
            TimeAndAttendanceModule().module,
            CompanyAssetsModule().module,
            HistoryModule().module,
            HistoryModule().module,
            ReportScreenModule().module,
            NoteScreenModule().module,
            UploadImageModule().module,
            AppModule().module,
            SettingsModule().module,

            EditProfileModule().module,
            NoteScreenModule().module
        )
    }
}
