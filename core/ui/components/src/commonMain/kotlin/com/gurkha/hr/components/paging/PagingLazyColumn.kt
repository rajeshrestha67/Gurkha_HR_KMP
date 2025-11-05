package com.gurkha.hr.components.paging

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.gurkha.hr.components.ERPButton
import com.gurkha.hr.components.dimens
import com.gurkha.hr.components.paging.model.PagingListState
import com.gurkha.hr.components.paging.model.isEndOfData
import com.gurkha.hr.components.paging.model.isError
import com.gurkha.hr.components.paging.model.isInitial
import com.gurkha.hr.components.paging.model.isLoading
import com.gurkha.hr.components.paging.model.isPaging
import com.gurkha.hr.res.SharedRes
import org.jetbrains.compose.resources.stringResource


@Composable
fun PagingLazyColumn(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    reverseLayout: Boolean = false,
    verticalArrangement: Arrangement.Vertical =
        if (!reverseLayout) Arrangement.Top else Arrangement.Bottom,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    userScrollEnabled: Boolean = true,
    includeInsets: Boolean = false,
    onBottomReached: () -> Unit,
    pagingListState: PagingListState,
    onRetry: () -> Unit,
    content: LazyListScope.() -> Unit
) {
    val reachedBottom by remember {
        derivedStateOf {
            state.reachedBottom()
        }
    }

    LaunchedEffect(reachedBottom, pagingListState) {
        val shouldNotCall =
            pagingListState.isLoading() || pagingListState.isPaging() || pagingListState.isError() != null || pagingListState.isInitial() || pagingListState.isEndOfData()
        if (reachedBottom && !shouldNotCall) {
            onBottomReached()
        }
    }
    LazyColumn(
        modifier = modifier,
        state = state,
        contentPadding = contentPadding,
        reverseLayout = reverseLayout,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
        flingBehavior = flingBehavior,
        userScrollEnabled = userScrollEnabled,
        content = {
            content()
            pagingStatusItemColumn(
                pagingState = pagingListState,
                onRetry = onRetry,
                key = this.toString(),
                includeInsets = includeInsets
            )
        }
    )
}

@Composable
fun PagingStatusViewColumn(
    pagingState: PagingListState,
    modifier: Modifier = Modifier,
    onRetry: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(MaterialTheme.dimens.small3)
            .heightIn(min = MaterialTheme.dimens.medium3),
        contentAlignment = Alignment.Center
    ) {
        when {
            pagingState.isPaging() -> CircularProgressIndicator()
            pagingState.isError() != null -> ErrorRetryViewColumn(
                error = pagingState.isError()!!,
                onRetry = onRetry
            )

            else -> Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))
        }
    }
}

// 2. Optional: LazyList extension for convenience
fun LazyListScope.pagingStatusItemColumn(
    includeInsets: Boolean,
    pagingState: PagingListState,
    onRetry: () -> Unit = {},
    key: Any? = null
) {
    item(key = key) {
        PagingStatusViewColumn(
            modifier = if (includeInsets) Modifier.windowInsetsPadding(WindowInsets.navigationBars) else Modifier,
            pagingState = pagingState,
            onRetry = onRetry
        )
    }
}

// 3. Extracted error view component
@Composable
private fun ErrorRetryViewColumn(
    error: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.clickable(onClick = onRetry),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.padding(horizontal = MaterialTheme.dimens.small2),
            text = error
        )
        ERPButton(
            modifier = Modifier
                .clip(MaterialTheme.shapes.extraSmall)
                .padding(
                    horizontal = MaterialTheme.dimens.small2,
                    vertical = MaterialTheme.dimens.small1
                ),
            onClick = onRetry,
            text = stringResource(SharedRes.Strings.request),
        )
    }
}
