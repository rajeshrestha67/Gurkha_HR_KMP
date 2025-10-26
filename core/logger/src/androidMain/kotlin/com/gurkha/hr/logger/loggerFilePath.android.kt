package com.gurkha.hr.logger

import okio.Path
import okio.Path.Companion.toPath
import java.io.File

actual fun getLogFilePath(): Path {
    val dir = File(
        System.getProperty("java.io.tmpdir") ?: "/data/data"
    )
    return File(dir, "logs.json").absolutePath.toPath()
}