package com.gurkha.hr.logger

import com.gurkha.hr.networkhelper.DataError

interface Logger {
    fun d(tag: String, message: String)
    fun i(tag: String, message: String)
    fun w(tag: String, message: String, error: DataError? = null)
    fun e(tag: String, message: String, error: DataError? = null)
}