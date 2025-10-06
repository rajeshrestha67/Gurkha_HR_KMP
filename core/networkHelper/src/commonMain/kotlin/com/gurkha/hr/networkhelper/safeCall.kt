package com.gurkha.hr.networkhelper

import com.gurkha.model.ErrorData
import com.gurkha.model.auth.login.LoginResponseDto
import com.gurkha.model.network.DataError
import com.gurkha.model.leave.leaveRequest.LeaveRequestResponseDto
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.JsonConvertException
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.SerializationException
import kotlin.coroutines.coroutineContext


suspend inline fun <reified T> safeCall(
    crossinline execute: suspend () -> HttpResponse
): ERPResult<T, DataError.NetworkError> {
    val response = try {
        execute()
    } catch (e: SocketTimeoutException) {
        return ERPResult.Error(DataError.NetworkError.RequestTimeout)
    } catch (e: UnresolvedAddressException) {
        return ERPResult.Error(DataError.NetworkError.NoInternet)
    } catch (e: SerializationException) {
        return ERPResult.Error(DataError.NetworkError.Serialization)
    } catch (e: Throwable) {
        return ERPResult.Error(e.toNetworkError())
    } catch (e: Exception) {
        coroutineContext.ensureActive()
        return ERPResult.Error(DataError.NetworkError.DataUnknown)
    }
    return responseToResult(response)
}

suspend inline fun <reified T> responseToResult(
    response: HttpResponse
): ERPResult<T, DataError.NetworkError> {
    return when (response.status.value) {
        in 200..299 -> {
            try {
                ERPResult.Success(response.body<T>())
            } catch (e: NoTransformationFoundException) {
                ERPResult.Error(DataError.NetworkError.Serialization)
            } catch (e: JsonConvertException) {
                ERPResult.Error(DataError.NetworkError.Serialization)
            } catch (e: Exception) {
                coroutineContext.ensureActive()
                ERPResult.Error(DataError.NetworkError.Serialization)
            }
        }

        401 -> ERPResult.Error(DataError.NetworkError.UnAuthorized)
        408 -> ERPResult.Error(DataError.NetworkError.RequestTimeout)
        409 -> ERPResult.Error(DataError.NetworkError.Conflict)
        429 -> ERPResult.Error(DataError.NetworkError.TooManyRequest)
        413 -> ERPResult.Error(DataError.NetworkError.PayloadTooLarge)
        in 500..599 -> ERPResult.Error(DataError.NetworkError.Server)
        else -> {
            if (T::class == LoginResponseDto::class || T::class == LeaveRequestResponseDto::class) {
                val res = try {
                    response.body<ErrorData>()
                } catch (e: Exception) {
                    null
                }
                ERPResult.Error(
                    DataError.NetworkError.Custom(
                        res?.message ?: response.status.description
                    )
                )
            } else {
                ERPResult.Error(DataError.NetworkError.DataUnknown)
            }
        }
    }
}
