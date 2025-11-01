package com.gurkha.hr.components

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.Foundation.NSURL
import platform.Foundation.dataWithContentsOfURL
import platform.Foundation.getBytes


@OptIn(ExperimentalForeignApi::class)
actual suspend fun getFileBytes(uri: String): ByteArray {
    val url = NSURL.fileURLWithPath(uri)
    val data = NSData.dataWithContentsOfURL(url)
        ?: throw Exception("Failed to load data from $uri")

    val length = data.length.toInt()
    if (length == 0) return ByteArray(0)

    return ByteArray(length).apply {
        usePinned {
            data.getBytes(it.addressOf(0), length.toULong())
        }
    }
}