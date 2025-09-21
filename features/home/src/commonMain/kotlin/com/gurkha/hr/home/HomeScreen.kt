package com.gurkha.hr.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.gurkha.hr.components.graphLine.SmoothLineGraph
import com.gurkha.hr.home.Model.AttendanceItem
import com.gurkha.hr.home.Model.HomeScreenState
import com.gurkha.hr.res.SharedRes
import com.gurkha.hr.res.theme.BorderColor
import com.gurkha.hr.res.theme.dimens
import com.gurkha.hr.res.theme.linkColor
import com.gurkha.hr.res.theme.primaryTextColor
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val viewModel: HomeScreenViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                windowInsets = WindowInsets(0.dp),

                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        AsyncImage(
                            modifier = Modifier
                                .clip(shape = CircleShape)
                                .size(size = MaterialTheme.dimens.medium3)
                                .aspectRatio(ratio = 1f)
                                .background(Color.Black),
                            model = SharedRes.getRes(path = "drawable/gurkha_hr.png"),
                            contentDescription = "avatar",
                            contentScale = ContentScale.Fit,
                        )

                        Column(
                            modifier = Modifier.weight(1f)
                                .padding(horizontal = MaterialTheme.dimens.small1),
                        ) {
                            Text(
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = MaterialTheme.colorScheme.primaryTextColor
                                ),
                                text = state.userName
                            )
                            Text(
                                style = MaterialTheme.typography.titleSmall.copy(
                                    color = MaterialTheme.colorScheme.primaryTextColor
                                ),
                                text = state.position
                            )
                        }
                    }
                },
                actions = {

                    IconButton(
                        onClick = {
                        }
                    ) {
                        Icon(
                            Icons.Filled.Search, contentDescription = "notification icon"
                        )
                    }
                    IconButton(
                        onClick = {

                        }
                    ) {
                        Icon(
                            Icons.Filled.Chat, contentDescription = "notification icon"
                        )
                    }
                    IconButton(
                        onClick = {
                        }
                    ) {
                        Icon(
                            Icons.Filled.Notifications, contentDescription = "notification icon"
                        )
                    }


                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->
        HomeScreenContent(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            state = state,
        )
    }
}

@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    state: HomeScreenState,
) {
    val pagerState = rememberPagerState(pageCount = { 2 })
    val listState = rememberLazyListState()
    val activeIndex = state.calendarItem.indexOfFirst { it.active }
    var showNotification by rememberSaveable {
        mutableStateOf(true)
    }

//to show the active week date and day starting from the sunday
    LaunchedEffect(activeIndex) {
        if (activeIndex >= 0) {
            val activeItem = state.calendarItem[activeIndex]
            val dayOfWeekNumber = when (activeItem.day) {
                "SUN" -> 1
                "MON" -> 2
                "TUE" -> 3
                "WED" -> 4
                "THU" -> 5
                "FRI" -> 6
                "SAT" -> 7
                else -> 0
            }
            val sundayIndex = (activeIndex - dayOfWeekNumber + 1).coerceAtLeast(0)
            listState.scrollToItem(sundayIndex)
        }
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomCenter,
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(
                top = MaterialTheme.dimens.small2,
                bottom = MaterialTheme.dimens.medium3
            )
        ) {
            //    Notification part
            item(key = "notification") {
                AnimatedVisibility(showNotification) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = MaterialTheme.dimens.small3),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Notification view")
                        IconButton(onClick = {
                            showNotification = !showNotification
                        }) {
                            Icon(Icons.Filled.Close, contentDescription = "close icon")
                        }
                    }
                }
            }

            //            calender part
            stickyHeader(key = "calender") {
                LazyRow(
                    state = listState,
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(space = MaterialTheme.dimens.small3),
                    contentPadding = PaddingValues(horizontal = MaterialTheme.dimens.small3)
                ) {
                    items(state.calendarItem) { item ->
                        val color = if (item.active)
                            MaterialTheme.colorScheme.secondaryContainer
                        else
                            MaterialTheme.colorScheme.background
                        Column(
                            modifier = Modifier
                                .clip(MaterialTheme.shapes.medium)
                                .background(color = color)
                                .border(
                                    1.dp,
                                    color = MaterialTheme.colorScheme.BorderColor,
                                    MaterialTheme.shapes.medium
                                )
                                .size(MaterialTheme.dimens.medium3)
                                .clickable(onClick = {
//                                    send the date to find there activities for that date
                                }),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = item.day, style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = item.date, style = MaterialTheme.typography.titleSmall.copy(
                                    color = MaterialTheme.colorScheme.primaryTextColor

                                )
                            )
                        }
                    }
                }
            }

            item("request_title") {
                Text(
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = MaterialTheme.dimens.small3),
                    text = stringResource(SharedRes.Strings.request),
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = MaterialTheme.colorScheme.primaryTextColor
                    )
                )
            }

            //        request part
            item(key = "request") {
                Column(
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = MaterialTheme.dimens.small3)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3)
                    ) {
                        state.requestRow1.forEach { item ->
                            AttendanceItemContent(
                                modifier = Modifier.weight(1f),
                                item = item
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(MaterialTheme.dimens.small3))

                    //  second row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3)
                    ) {
                        state.requestRow2.forEach { item ->
                            AttendanceItemContent(
                                modifier = Modifier.weight(1f),
                                item = item
                            )
                        }
                    }
                }

            }

            //        attendance title
            item(key = "attendance_title") {
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = MaterialTheme.dimens.small3),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = stringResource(SharedRes.Strings.attendance),
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = MaterialTheme.colorScheme.primaryTextColor
                        )
                    )
                    Text(
                        text = stringResource(SharedRes.Strings.view_all),
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = MaterialTheme.colorScheme.linkColor
                        )
                    )
                }
            }


            //        attendance chart
            item(key = "Attendance Chart") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(MaterialTheme.dimens.chartHeight)
                ) {
                    HorizontalPager(state = pagerState) { item ->
                        AnimatedContent(item) { page ->
                            when (page) {
                                0 -> SmoothLineGraph()
                                1 -> Box(
                                    modifier = Modifier
                                        .background(color = MaterialTheme.colorScheme.onPrimaryContainer)
                                        .fillMaxSize(),
                                )
                            }
                        }
                    }
                }
            }
        }
//        slider
    }
}


//reusable request row
@Composable
fun AttendanceItemContent(
    item: AttendanceItem,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(MaterialTheme.dimens.small2))
            .border(
                width = 1.dp,
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.BorderColor
            )
            .clickable(onClick = {

            }),
//            .background(MaterialTheme.colorScheme.secondaryContainer),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier
                .padding(
                    horizontal = MaterialTheme.dimens.small3,
                    vertical = MaterialTheme.dimens.small2
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small2)
        ) {
            Icon(imageVector = item.icon, contentDescription = "arrow right")
            Text(text = item.title, style = MaterialTheme.typography.titleMedium)
        }

        Column(
            modifier = Modifier
                .padding(
                    horizontal = MaterialTheme.dimens.small3,
                    vertical = MaterialTheme.dimens.small2
                ),

            ) {
            Text(
                text = item.time, style = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.primaryTextColor
                )
            )
            Text(
                text = item.status, style = MaterialTheme.typography.titleSmall.copy(
                    color = MaterialTheme.colorScheme.primaryTextColor

                )
            )
        }

    }
}