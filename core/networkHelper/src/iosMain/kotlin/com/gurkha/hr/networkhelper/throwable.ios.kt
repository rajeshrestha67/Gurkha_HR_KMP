package com.gurkha.hr.networkhelper

actual fun Throwable.toNetworkError(): DataError.NetworkError {
    return DataError.NetworkError.DataUnknown
}