package com.gurkha.hr.components.paging

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState


fun LazyListState.reachedBottom(threshold: Float = 0.9f): Boolean {
    return layoutInfo.visibleItemsInfo.lastOrNull()?.let { lastItem ->
        (lastItem.index + 1) / layoutInfo.totalItemsCount.toFloat() >= threshold
    } ?: false
}

fun LazyGridState.reachedBottom(threshold: Float = 0.9f): Boolean {
    return layoutInfo.visibleItemsInfo.lastOrNull()?.let { lastItem ->
        (lastItem.index + 1) / layoutInfo.totalItemsCount.toFloat() >= threshold
    } ?: false
}