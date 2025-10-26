package com.gurkha.hr.logger

import okio.Path
import okio.Path.Companion.toPath
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUserDomainMask

actual fun getLogFilePath(): Path {
    val paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, true)
    val documentsDirectory = paths.firstOrNull() as? String ?: "/tmp"
    return "$documentsDirectory/logs.json".toPath()
}