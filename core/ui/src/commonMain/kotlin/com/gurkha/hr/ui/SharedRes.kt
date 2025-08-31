package com.gurkha.hr.ui

import gurkhahr.core.ui.generated.resources.Res

object SharedRes {
    fun getRes(name: String): String {
        return Res.getUri(name)
    }
}