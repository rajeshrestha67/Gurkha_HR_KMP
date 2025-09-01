package com.gurkha.hr.res

import gurkhahr.core.ui.res.generated.resources.Poppins_Bold
import gurkhahr.core.ui.res.generated.resources.Poppins_Italic
import gurkhahr.core.ui.res.generated.resources.Poppins_Medium
import gurkhahr.core.ui.res.generated.resources.Res
import gurkhahr.core.ui.res.generated.resources.email
import gurkhahr.core.ui.res.generated.resources.enter_your_email
import gurkhahr.core.ui.res.generated.resources.enter_your_password
import gurkhahr.core.ui.res.generated.resources.enter_your_username
import gurkhahr.core.ui.res.generated.resources.invalid_email_address
import gurkhahr.core.ui.res.generated.resources.invalid_password_digit
import gurkhahr.core.ui.res.generated.resources.invalid_password_length
import gurkhahr.core.ui.res.generated.resources.invalid_password_lowercase
import gurkhahr.core.ui.res.generated.resources.invalid_password_special_char
import gurkhahr.core.ui.res.generated.resources.invalid_password_uppercase
import gurkhahr.core.ui.res.generated.resources.login
import gurkhahr.core.ui.res.generated.resources.password
import gurkhahr.core.ui.res.generated.resources.required
import gurkhahr.core.ui.res.generated.resources.username
import gurkhahr.core.ui.res.generated.resources.welcome

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