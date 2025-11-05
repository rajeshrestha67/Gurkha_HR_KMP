package com.gurkha.hr.components.paging.model

sealed interface PagingListState {
    data object Initial : PagingListState
    data object Loaded : PagingListState
    data object Loading : PagingListState
    data object Paging : PagingListState
    data object Refresh : PagingListState
    data object EndOfData : PagingListState
    data class Error(val error: String) : PagingListState
}

fun PagingListState.isLoading(): Boolean {
    return this == PagingListState.Loading
}

fun PagingListState.isRefreshing(): Boolean {
    return this == PagingListState.Refresh
}

fun PagingListState.isPaging(): Boolean {
    return this == PagingListState.Paging
}


fun PagingListState.isInitial(): Boolean {
    return this == PagingListState.Initial
}

fun PagingListState.isEndOfData(): Boolean {
    return this == PagingListState.EndOfData
}

fun PagingListState.isError(): String? {
    return when (this) {
        is PagingListState.Error -> return error
        else -> null
    }
}

fun PagingListState.isLoaded(): Boolean {
    return this == PagingListState.Loaded
}

