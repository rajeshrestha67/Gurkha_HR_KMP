package com.gurkha.hr.home

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
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.gurkha.hr.components.shimmer.ShimmerView
import com.gurkha.hr.domain.upComingBirthday.model.UpComingBirthdayData
import com.gurkha.hr.home.Model.AttendanceItem
import com.gurkha.hr.home.Model.HomeScreenActions
import com.gurkha.hr.home.Model.HomeScreenState
import com.gurkha.hr.res.SharedRes
import com.gurkha.hr.res.theme.BorderColor
import com.gurkha.hr.res.theme.dimens
import com.gurkha.hr.res.theme.linkColor
import com.gurkha.hr.res.theme.primaryTextColor
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
) {
    val viewModel: HomeScreenViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

//    fetch the data
    LaunchedEffect(Unit) {
        viewModel.onAction(HomeScreenActions.OnFetchCurrentUser)
        viewModel.onAction(HomeScreenActions.OnFetchUpComingBirthday)
        viewModel.onAction(HomeScreenActions.OnFetchUpComingWorkAnniversary)
    }

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .padding(
                horizontal = MaterialTheme.dimens.small3, vertical = MaterialTheme.dimens.small2
            ).fillMaxSize(), containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0.dp),
                navigationIcon = {
                    state.userProfileUrl?.let {
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
                    } ?: Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = "avatar",
                        modifier = Modifier
                            .size(MaterialTheme.dimens.medium3)
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.BorderColor,
                                CircleShape
                            )
                            .padding(4.dp)
                            .clip(CircleShape)
                    )
                },
                title = {
                    Column {
                        Text(
                            style = MaterialTheme.typography.titleMedium,
                            text = state.fullName
                        )
                        Text(
                            style = MaterialTheme.typography.titleSmall.copy(
                                color = MaterialTheme.colorScheme.primaryTextColor
                            ),
                            text = state.levelName
                        )
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
            onFetchAttendance = { viewModel.onAction(HomeScreenActions.AttendanceFetch) }
        )
    }
}

@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    state: HomeScreenState,
    onFetchAttendance: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { 2 })
    val listState = rememberLazyListState()
    val activeIndex = state.calendarItem.indexOfFirst { it.active }

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

//    fetch the data
    LaunchedEffect(Unit) {
        onFetchAttendance()
    }

    Box(
        contentAlignment = Alignment.BottomCenter,
    ) {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.medium1),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(
                start = MaterialTheme.dimens.small2,
                end = MaterialTheme.dimens.small2,
                top = MaterialTheme.dimens.small2,
                bottom = MaterialTheme.dimens.medium3
            )
        ) {
            //    Notification part
            item(key = "notification") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Notification view")
                    Icon(Icons.Filled.Close, contentDescription = "close icon")
                }
            }

            //            calender part
            stickyHeader(key = "calender") {
                LazyRow(
                    state = listState,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(space = MaterialTheme.dimens.small3)
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

            //        request part
            item(key = "Request part") {
                Column {
                    TitleBar(onViewAll = {}, title = SharedRes.Strings.request)

                    Spacer(modifier = Modifier.height(MaterialTheme.dimens.small2))
// request options grid
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3)
                    ) {
                        state.requestRow1.forEach { item ->
                            AttendanceItemContent(
                                modifier = Modifier.weight(1f),
                                item = item,
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(MaterialTheme.dimens.small3))

                    //                second row
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

//            Upcoming birthday part
            item(key = "upcoming birthday part") {
                TitleBar(
                    onViewAll = {},
                    title = SharedRes.Strings.upcoming_birthday,
                    subTitle = SharedRes.Strings.view_all
                )
                Spacer(modifier = Modifier.height(MaterialTheme.dimens.small2))
//                show the shimmer if is loading else show the Upcoming birthday
                state.isLoading.let { isLoading ->
                    if (isLoading) {
                        ShimmerView(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(MaterialTheme.dimens.bottomBar)
                        )
                    } else {
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = (state.upComingBirthday?.size
                                ?: 0).let { size ->
                                if (size > 2) Arrangement.spacedBy(MaterialTheme.dimens.medium3)
                                else Arrangement.SpaceBetween
                            }

                        ) {
                            items(state.upComingBirthday ?: emptyList()) { item ->
                                EventCard(
                                    fullName = item.fullName,
                                    imageUrl = item.imageUrl,
                                    date = item.dateOfBirth,
                                    designationName = item.designationName
                                )
                            }
                        }
                    }
                }

            }

//            upcoming work anniversary part
            item(key = "upcoming anniversary part") {
                TitleBar(
                    onViewAll = {},
                    title = SharedRes.Strings.work_anniversaries,
                    subTitle = SharedRes.Strings.view_all
                )
                Spacer(modifier = Modifier.height(MaterialTheme.dimens.small2))
//                show the shimmer if is loading else show the Upcoming work anniversary
                state.isLoading.let { isLoading ->
                    if (isLoading) {
                        ShimmerView(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(MaterialTheme.dimens.bottomBar)
                        )
                    } else {
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = (state.upComingWorkAnniversary?.size
                                ?: 0).let { size ->
                                if (size > 2) Arrangement.spacedBy(MaterialTheme.dimens.medium3)
                                else Arrangement.SpaceBetween
                            }

                        ) {
                            items(state.upComingWorkAnniversary ?: emptyList()) { item ->
                                EventCard(
                                    fullName = item.fullName,
                                    imageUrl = item.imageUrl,
                                    date = item.joinedDate,
                                    designationName = item.designationName
                                )
                            }
                        }
                    }
                }

            }

            //        attendance part
            item(key = "attendance part") {
                TitleBar(
                    onViewAll = {},
                    title = SharedRes.Strings.attendance,
                    subTitle = SharedRes.Strings.view_all
                )
                Spacer(modifier = Modifier.height(MaterialTheme.dimens.small2))
//chart
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(MaterialTheme.dimens.chartHeight)
                ) {
                    HorizontalPager(state = pagerState) { item ->
                        val color =
                            if (item == 0) MaterialTheme.colorScheme.BorderColor else MaterialTheme.colorScheme.onPrimaryContainer
                        Box(
                            modifier = Modifier
                                .background(color = color)
                                .fillMaxSize(),
                        )

                    }
                }
            }
        }
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


@Composable
fun EventCard(
    imageUrl : String,
    fullName : String,
    designationName : String,
    date : String,
) {
    Column(
        modifier = Modifier
            .widthIn(min = 150.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.BorderColor,
                shape = RoundedCornerShape(MaterialTheme.dimens.small2)
            )
            .padding(
                MaterialTheme.dimens.small2
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small1)
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = fullName,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(MaterialTheme.dimens.medium2)
                    .aspectRatio(ratio = 1f)
                    .background(Color.Black)
            )
            Text(
                text = "Kartik",
                style = MaterialTheme.typography.titleSmall.copy(
                    color = MaterialTheme.colorScheme.primaryTextColor
                )
            )
        }
        Text(text = fullName, style = MaterialTheme.typography.titleMedium)
        Text(
            text = designationName,
            style = MaterialTheme.typography.titleSmall.copy(
                color = MaterialTheme.colorScheme.primaryTextColor
            )
        )
    }
}


@Composable
fun TitleBar(
    title: StringResource,
    subTitle: StringResource? = null,
    onViewAll: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(title),
            style = MaterialTheme.typography.titleLarge
        )
        subTitle?.let {
            TextButton(
                onClick = onViewAll,
                content = {
                    Text(
                        text = stringResource(subTitle),
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = MaterialTheme.colorScheme.linkColor
                        )
                    )
                }
            )
        }
    }
}