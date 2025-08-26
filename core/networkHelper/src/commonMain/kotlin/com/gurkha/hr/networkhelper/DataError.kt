package com.gurkha.hr.networkhelper


sealed interface DataError : ERPError {

    sealed interface NetworkError : DataError {
        data object RequestTimeout : NetworkError
        data object UnAuthorized : NetworkError
        data object Conflict : NetworkError
        data object TooManyRequest : NetworkError
        data object NoInternet : NetworkError
        data object PayloadTooLarge : NetworkError
        data object Server : NetworkError
        data object Serialization : NetworkError
        data object DataUnknown : NetworkError
        data class Custom(val errors: HashMap<String, List<String>>?) : NetworkError
    }

    sealed interface LocalError : DataError {

        data object NoData : LocalError
        data object DiskFull : LocalError
        data object UnKnown : LocalError
    }
}

fun DataError.toErrorMessage(key: String = ""): String {
    return when (this) {
        DataError.NetworkError.RequestTimeout -> "Request time out"
        DataError.NetworkError.UnAuthorized -> "Unauthorized Access"
        DataError.NetworkError.Conflict -> "Conflict Occurred"
        DataError.NetworkError.TooManyRequest -> "Too Many Request"
        DataError.NetworkError.NoInternet -> "No Internet Connection"
        DataError.NetworkError.PayloadTooLarge -> "Payout Invalid"
        DataError.NetworkError.Server -> "Server Error"
        DataError.NetworkError.Serialization -> "Serialization Error"
        DataError.NetworkError.DataUnknown -> "Something went wrong!"
        DataError.LocalError.DiskFull -> "Disk full error"
        DataError.LocalError.NoData -> "No Data"
        DataError.LocalError.UnKnown -> "Something went wrong!"
        is DataError.NetworkError.Custom -> {
            toErrorMessage(key) ?: "Something went wrong!"
        }
    }
}

fun DataError.NetworkError.Custom.toErrorMessage(key: String): String? {
    return when (val error = errors) {
        null -> null
        else -> {
            if (error.isEmpty()) {
                return null
            } else {
                return error[key]?.firstOrNull()
            }
        }
    }
}