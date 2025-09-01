package com.gurkha.hr.res

import gurkhahr.core.ui.res.generated.resources.Poppins_Bold
import gurkhahr.core.ui.res.generated.resources.Poppins_Italic
import gurkhahr.core.ui.res.generated.resources.Poppins_Medium
import gurkhahr.core.ui.res.generated.resources.Res
import gurkhahr.core.ui.res.generated.resources.email
import gurkhahr.core.ui.res.generated.resources.enter_your_email
import gurkhahr.core.ui.res.generated.resources.invalid_email_address
import gurkhahr.core.ui.res.generated.resources.required
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
    }
}