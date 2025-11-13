package com.gurkha.hr.components.tabbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape


@Composable
fun <T> ERPTabView(
    modifier: Modifier = Modifier,
    items: List<T>,
    selectedTab: T,
    onItemSelected: (T) -> Unit,
    onItemReSelected: (T) -> Unit = {},
    shape: Shape = MaterialTheme.shapes.large,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    text: @Composable ((T, Boolean) -> Unit),
) {
    var selectedTabIndex by rememberSaveable { mutableStateOf(0) }
    var previousSelectedTabIndex by rememberSaveable { mutableStateOf(0) }

    LaunchedEffect(selectedTab) {
        val newIndex = items.indexOf(selectedTab)
        if (newIndex != -1 && newIndex != selectedTabIndex) {
            selectedTabIndex = newIndex
            previousSelectedTabIndex = newIndex
        }
    }
    Box(
        modifier = modifier.fillMaxWidth().background(backgroundColor)
    ) {

        SecondaryTabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape),
            containerColor = MaterialTheme.colorScheme.primary.copy(0.1f),
            contentColor = TabRowDefaults.primaryContentColor,
            divider = {},
            indicator = {},
        ) {
            items.forEachIndexed { index, item ->
                val isSelected = selectedTab == item
                val tabBackgroundColor = if (isSelected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    Color.Transparent
                }
                Tab(
                    modifier = Modifier.background(
                        color = tabBackgroundColor,
                        shape = shape
                    ),
                    selected = isSelected,
                    onClick = {
                        if (index == previousSelectedTabIndex) {
                            onItemReSelected(item)
                            return@Tab
                        }
                        selectedTabIndex = index
                        onItemSelected(item)
                        previousSelectedTabIndex = selectedTabIndex
                    },
                    text = {
                        text(item, isSelected)
                    }
                )
            }
        }
    }
}
