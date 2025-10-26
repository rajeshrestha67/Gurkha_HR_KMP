package com.gurkha.hr.networkhelper

import com.gurkha.model.network.DataError

expect fun Throwable.toNetworkError(): DataError.NetworkError