package com.gurkha.hr.networkhelper

import com.gurkha.hr.datastore.token.repository.TokenRepository
import io.ktor.client.HttpClient
import io.ktor.client.plugins.onUpload
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMessageBuilder
import kotlinx.coroutines.flow.firstOrNull
import org.koin.mp.KoinPlatform.getKoin


suspend inline fun HttpClient.post(
    baseUrl: BaseUrl = BaseUrl.Generic,
    endPoint: String, block: HttpRequestBuilder.() -> Unit = {}
): HttpResponse = post {
    appendLocalAttributes(
        baseUrl = baseUrl,
        endPoint = endPoint, block = block
    )
}

suspend inline fun HttpClient.get(
    baseUrl: BaseUrl = BaseUrl.Generic,
    endPoint: String, block: HttpRequestBuilder.() -> Unit = {}
): HttpResponse = get {
    appendLocalAttributes(
        baseUrl = baseUrl,
        endPoint = endPoint, block = block
    )
}

suspend inline fun HttpClient.delete(
    baseUrl: BaseUrl = BaseUrl.Generic,
    endPoint: String, block: HttpRequestBuilder.() -> Unit = {}
): HttpResponse = delete {
    appendLocalAttributes(
        baseUrl = baseUrl,
        endPoint = endPoint, block = block
    )
}

suspend inline fun HttpClient.uploadImage(
    baseUrl: BaseUrl = BaseUrl.Generic,
    endPoint: String,
    fileName: String,
    fileBytes: ByteArray,
    contentType: ContentType = ContentType.Image.JPEG,
    crossinline block: HttpRequestBuilder.() -> Unit = {},
    crossinline onProgress: (Int) -> Unit
): HttpResponse {
    return post(
        baseUrl = baseUrl,
        endPoint = endPoint
    ) {
        setBody(
            MultiPartFormDataContent(
                formData {
                    append("files", fileBytes, Headers.build {
                        append(HttpHeaders.ContentType, contentType.toString())
                        append(HttpHeaders.ContentDisposition, "filename=\"$fileName\"")
                    })
                }
            )
        )
        onUpload { written, total ->
            val progress = (written * 100) / (total ?: 0L)
            onProgress(progress.toInt())
        }
        block()
    }
}

suspend inline fun HttpRequestBuilder.appendLocalAttributes(
    baseUrl: BaseUrl,
    endPoint: String,
    block: HttpRequestBuilder.() -> Unit = {}
) {
    val tokenRepository: TokenRepository = getKoin().get()
    tokenRepository.token.firstOrNull()?.jwtToken?.let { token ->
        accessToken(token)
    }

    url(path = endPoint, host = baseUrl.url, scheme = "https")
    block()
}

public fun HttpMessageBuilder.accessToken(token: String): Unit =
    header(HttpHeaders.Authorization, token)
