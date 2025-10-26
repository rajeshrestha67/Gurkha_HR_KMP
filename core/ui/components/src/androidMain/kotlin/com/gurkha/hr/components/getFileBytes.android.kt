package com.gurkha.hr.components

import android.content.Context
import androidx.core.net.toUri
import org.koin.mp.KoinPlatform.getKoin
import java.io.FileNotFoundException

actual suspend fun getFileBytes(uri: String): ByteArray {
    val context: Context = getKoin().get()
    val contentUri = uri.toUri()
    return context.contentResolver.openInputStream(contentUri)?.use { input ->
        input.readBytes()
    } ?: throw FileNotFoundException("Cannot open input stream for $uri")
}