package com.gurkha.hr.splashscreen.model

sealed interface OnBoardingAction {
    data object CheckFirstUser : OnBoardingAction
    data object OnNext: OnBoardingAction
    data object OnSkip : OnBoardingAction
}