package com.gurkha.hr.networkhelper

import io.ktor.client.network.sockets.SocketTimeoutException
import kotlinx.io.IOException

actual fun Throwable.toNetworkError(): DataError.NetworkError {
    return when (this) {
        is SocketTimeoutException -> DataError.NetworkError.RequestTimeout
        is NotImplementedError -> DataError.NetworkError.Serialization
        is IOException -> DataError.NetworkError.NoInternet
        else -> DataError.NetworkError.DataUnknown
    }
}