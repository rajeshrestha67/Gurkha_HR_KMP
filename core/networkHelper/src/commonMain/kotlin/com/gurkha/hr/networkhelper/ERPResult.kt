package com.gurkha.hr.networkhelper

import com.gurkha.model.network.ERPError


public sealed interface ERPResult<out D, out E : ERPError> {
    data class Success<out D>(val data: D) : ERPResult<D, Nothing>
    data class Error<out E : ERPError>(val error: E) : ERPResult<Nothing, E>
}

inline fun <T, E : ERPError, R> ERPResult<T, E>.map(transform: (T) -> R): ERPResult<R, E> {
    return when (this) {
        is ERPResult.Error -> ERPResult.Error(error)
        is ERPResult.Success -> {
            ERPResult.Success(transform(data))
        }
    }
}


inline fun <T, E : ERPError> ERPResult<T, E>.onSuccess(action: (T) -> Unit): ERPResult<T, E> {
    return when (this) {
        is ERPResult.Error -> this
        is ERPResult.Success -> {
            action(data)
            this
        }
    }
}

inline fun <T, E : ERPError> ERPResult<T, E>.onError(action: (E) -> Unit): ERPResult<T, E> {
    return when (this) {
        is ERPResult.Error -> {
            action(error)
            this
        }

        is ERPResult.Success -> this
    }
}

typealias EmptyResult<E> = ERPResult<Unit, E>