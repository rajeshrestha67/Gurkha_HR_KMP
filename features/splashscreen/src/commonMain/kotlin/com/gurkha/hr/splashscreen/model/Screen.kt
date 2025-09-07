package com.gurkha.hr.splashscreen.model

import com.gurkha.hr.res.SharedRes


data class Screen(
    val title: String,
    val description: String,
    val image: String
)

object ScreenList {
    val screenList = listOf(
        Screen(
            "Welcome to Gurkha HR \n Simplify attendance , streamline your \n workday",
            "Track attendance, manage shifts, and stay connected \n with your team - all in one smart app",
            SharedRes.getRes("drawable/onboarding1.png")
        ),
        Screen(
            "Clock in clock out - stay punctual,stay \n productive.",
            "Log your attendance effortlessly, monitor work hours, \n and keep records accurate in real time",
            SharedRes.getRes("drawable/onboarding2.png")
        ),
        Screen(
            "Smarter insights for workforce \n decisions.",
            "Get real-time reposts, manager leaves, and optimize \nperformance with Gurkha HR's intelligent dashboard",
            SharedRes.getRes("drawable/onboarding3.png")
        )
    )
}