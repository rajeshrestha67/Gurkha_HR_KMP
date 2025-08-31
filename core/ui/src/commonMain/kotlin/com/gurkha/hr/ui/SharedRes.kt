package com.gurkha.hr.ui

import gurkhahr.core.ui.generated.resources.Poppins_Bold
import gurkhahr.core.ui.generated.resources.Poppins_Italic
import gurkhahr.core.ui.generated.resources.Poppins_Medium
import gurkhahr.core.ui.generated.resources.Res

object SharedRes {
    fun getRes(name: String): String {
        return Res.getUri(name)
    }

    object Fonts {
        val poppinsMedium = Res.font.Poppins_Medium
        val poppinsBold = Res.font.Poppins_Bold
        val poppinsItalic = Res.font.Poppins_Italic
    }
}