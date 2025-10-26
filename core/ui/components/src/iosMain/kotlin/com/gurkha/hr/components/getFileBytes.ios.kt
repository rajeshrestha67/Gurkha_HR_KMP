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
        ?: NSData()

    val length = data.length.toInt()
    val bytes = ByteArray(length)

    bytes.usePinned {
        data.getBytes(it.addressOf(0), data.length)
    }

    return bytes
}