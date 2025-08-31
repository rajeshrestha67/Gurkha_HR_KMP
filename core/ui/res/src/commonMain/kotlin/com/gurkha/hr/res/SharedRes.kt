package com.gurkha.hr.res

import gurkhahr.core.ui.res.generated.resources.Poppins_Bold
import gurkhahr.core.ui.res.generated.resources.Poppins_Italic
import gurkhahr.core.ui.res.generated.resources.Poppins_Medium
import gurkhahr.core.ui.res.generated.resources.Res
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
    }
}