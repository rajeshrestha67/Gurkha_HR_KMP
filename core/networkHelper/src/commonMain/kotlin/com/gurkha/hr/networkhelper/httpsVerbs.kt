package com.gurkha.hr.networkhelper

import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse


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

suspend inline fun HttpRequestBuilder.appendLocalAttributes(
    baseUrl: BaseUrl,
    endPoint: String,
    block: HttpRequestBuilder.() -> Unit = {}
) {

    url(path = endPoint, host = baseUrl.url, scheme = "https")
    block()
}

sealed class BaseUrl(open val url: String) {
    data object Generic : BaseUrl(BASE_URL)
}