package com.gurkha.model.network


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
        data class Custom(val message: String) : NetworkError
    }

    sealed interface LocalError : DataError {

        data object NoData : LocalError
        data object DiskFull : LocalError
        data object UnKnown : LocalError
        data class Custom(val throwable: Throwable) : LocalError {
            constructor(exception: Exception) : this(exception as Throwable)
        }
    }
}

fun DataError.toErrorMessage(): String {
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
            this.message
        }

        is DataError.LocalError.Custom -> {
            this.throwable.message ?: "Something went wrong!"
        }
    }
}

fun DataError.toException(): Exception {
    return when (this) {
        DataError.NetworkError.RequestTimeout -> Exception("Request timed out")
        DataError.NetworkError.NoInternet -> Exception("No internet connection")
        DataError.NetworkError.Serialization -> Exception("Serialization error")
        DataError.NetworkError.UnAuthorized -> Exception("Unauthorized")
        DataError.NetworkError.Conflict -> Exception("Conflict occurred")
        DataError.NetworkError.TooManyRequest -> Exception("Too many requests")
        DataError.NetworkError.PayloadTooLarge -> Exception("Payload too large")
        DataError.NetworkError.Server -> Exception("Server error")
        DataError.NetworkError.DataUnknown -> Exception("Unknown network error")
        is DataError.NetworkError.Custom -> Exception(this.message)
        DataError.LocalError.DiskFull -> Exception("Disk full")
        DataError.LocalError.NoData -> Exception("No data")
        DataError.LocalError.UnKnown -> Exception("Unknown local error")
        is DataError.LocalError.Custom -> this.throwable as Exception
    }
}
