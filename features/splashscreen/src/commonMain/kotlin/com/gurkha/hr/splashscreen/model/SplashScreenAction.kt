package com.gurkha.hr.splashscreen.model

sealed interface SplashScreenAction {
    data object CheckFirstUser : SplashScreenAction
}