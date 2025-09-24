package com.gurkha.di

import com.gurkha.hr.profile.profile_info.ProfileInfoScreen
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.ksp.generated.module


fun initKoin(config: KoinAppDeclaration? = null) {

    startKoin {
        config?.invoke(this)
        modules(
            NetworkModule().module,
            AuthModule().module,
            PlatformMessageModule().module,
            DataStoreModule().module,
            FormModule().module,
            SplashScreenModule().module,
            DashboardScreenModule().module,
            ProfileScreenModule().module,
            ChangePasswordModule().module,
            HomeScreenModule().module,
            ProfileInfoScreenModule().module
        )
    }
}
