package com.gurkha.hr.res

import gurkhahr.core.ui.res.generated.resources.Poppins_Bold
import gurkhahr.core.ui.res.generated.resources.Poppins_Italic
import gurkhahr.core.ui.res.generated.resources.Poppins_Medium
import gurkhahr.core.ui.res.generated.resources.Res
import gurkhahr.core.ui.res.generated.resources.attendance
import gurkhahr.core.ui.res.generated.resources.email
import gurkhahr.core.ui.res.generated.resources.enter_your_email
import gurkhahr.core.ui.res.generated.resources.enter_your_password
import gurkhahr.core.ui.res.generated.resources.enter_your_username
import gurkhahr.core.ui.res.generated.resources.getStarted
import gurkhahr.core.ui.res.generated.resources.home
import gurkhahr.core.ui.res.generated.resources.invalid_email_address
import gurkhahr.core.ui.res.generated.resources.invalid_password_digit
import gurkhahr.core.ui.res.generated.resources.invalid_password_length
import gurkhahr.core.ui.res.generated.resources.invalid_password_lowercase
import gurkhahr.core.ui.res.generated.resources.invalid_password_special_char
import gurkhahr.core.ui.res.generated.resources.invalid_password_uppercase
import gurkhahr.core.ui.res.generated.resources.leave
import gurkhahr.core.ui.res.generated.resources.login
import gurkhahr.core.ui.res.generated.resources.my_attendance
import gurkhahr.core.ui.res.generated.resources.next
import gurkhahr.core.ui.res.generated.resources.password
import gurkhahr.core.ui.res.generated.resources.profile
import gurkhahr.core.ui.res.generated.resources.report
import gurkhahr.core.ui.res.generated.resources.request
import gurkhahr.core.ui.res.generated.resources.required
import gurkhahr.core.ui.res.generated.resources.skip
import gurkhahr.core.ui.res.generated.resources.upcoming_birthday
import gurkhahr.core.ui.res.generated.resources.username
import gurkhahr.core.ui.res.generated.resources.view_all
import gurkhahr.core.ui.res.generated.resources.welcome
import gurkhahr.core.ui.res.generated.resources.work_anniversaries

object SharedRes {

    fun getRes(path: String): String {
        return Res.getUri(path)
    }

    object Fonts {
        val poppinsMedium = Res.font.Poppins_Medium
        val poppinsBold = Res.font.Poppins_Bold
        val poppinsItalic = Res.font.Poppins_Italic
    }

    object Strings {
        val welcome = Res.string.welcome
        val required = Res.string.required
        val next = Res.string.next
        val skip = Res.string.skip
        val getStarted = Res.string.getStarted
        val home = Res.string.home
        val my_attendance = Res.string.my_attendance
        val profile = Res.string.profile
        val leave = Res.string.leave
        val report = Res.string.report
        val request = Res.string.request
        val view_all = Res.string.view_all
        val attendance = Res.string.attendance
        val upcoming_birthday = Res.string.upcoming_birthday
        val work_anniversaries = Res.string.work_anniversaries




        val invalidEmailAddress = Res.string.invalid_email_address
        val email = Res.string.email
        val enterYourEmail = Res.string.enter_your_email
        val login = Res.string.login
        val username = Res.string.username
        val enterYourUsername = Res.string.enter_your_username
        val password = Res.string.password
        val enterYourPassword = Res.string.enter_your_password
        val invalidPasswordLength = Res.string.invalid_password_length
        val invalidPasswordUppercase = Res.string.invalid_password_uppercase
        val invalidPasswordLowercase = Res.string.invalid_password_lowercase
        val invalidPasswordDigit = Res.string.invalid_password_digit
        val invalidPasswordSpecialChar = Res.string.invalid_password_special_char
    }
}