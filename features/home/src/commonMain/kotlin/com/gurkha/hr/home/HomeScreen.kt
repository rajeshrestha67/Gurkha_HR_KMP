package com.gurkha.hr.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gurkha.hr.components.ProfilePicture
import com.gurkha.hr.components.date.horizontalCalendar.HorizontalCalendar
import com.gurkha.hr.components.extractInitials
import com.gurkha.hr.components.graphLine.SmoothLineGraph
import com.gurkha.hr.components.shimmer.ShimmerView
import com.gurkha.hr.components.swipeToDismiss.SwipeToDismissBox
import com.gurkha.hr.date.data.CalendarDate
import com.gurkha.hr.date.data.CalendarDay
import com.gurkha.hr.home.model.AttendanceItem
import com.gurkha.hr.home.model.HomeScreenState
import com.gurkha.hr.res.SharedRes
import com.gurkha.hr.res.theme.borderColor
import com.gurkha.hr.res.theme.dimens
import com.gurkha.hr.res.theme.imageBackgroundColor
import com.gurkha.hr.res.theme.linkColor
import com.gurkha.hr.res.theme.primaryTextColor
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    topAppBarScrollBehavior: TopAppBarScrollBehavior,
    onChatClick: () -> Unit,
    onNotificationClick: () -> Unit
) {
    val viewModel: HomeScreenViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()


    Scaffold(
        contentWindowInsets = WindowInsets(),
        modifier = Modifier
            .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
            .fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                windowInsets = WindowInsets(),
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        ProfilePicture(
                            imageUrl = state.userProfileUrl,
                            employeeName = state.fullName,
                            nameInitials = state.initials,
                            size = MaterialTheme.dimens.medium3,
                            shape = CircleShape,
                            background = MaterialTheme.colorScheme.imageBackgroundColor,
                            borderWidth = 0.5.dp,
                            borderColor = MaterialTheme.colorScheme.borderColor,
                            ratio = 1f
                        )

                        Column(
                            modifier = Modifier.weight(1f)
                                .padding(horizontal = MaterialTheme.dimens.small1),
                        ) {
                            Text(
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = MaterialTheme.colorScheme.primaryTextColor
                                ),
                                text = state.fullName
                            )
                            Text(
                                style = MaterialTheme.typography.titleSmall.copy(
                                    color = MaterialTheme.colorScheme.primaryTextColor
                                ),
                                text = state.levelName
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = onChatClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Chat,
                            contentDescription = "chat"
                        )
                    }
                    IconButton(onClick = onNotificationClick) {
                        Icon(
                            imageVector = Icons.Filled.Notifications,
                            contentDescription = "notification icon"
                        )
                    }
                },
                scrollBehavior = topAppBarScrollBehavior
            )
        }
    ) { paddingValues ->
        HomeScreenContent(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            state = state
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    state: HomeScreenState
) {
    val pagerState = rememberPagerState(pageCount = { 2 })
    //val calendarListState = rememberLazyListState()
    //val activeIndex = state.calendarItem.indexOfFirst { it.active }
    val (showNotification, onChangeNotification) = rememberSaveable {
        mutableStateOf(true)
    }
    val mainListState = rememberLazyListState()


    val isScrolling by remember {
        derivedStateOf {
            mainListState.isScrollInProgress
        }
    }

    val isAtTop by remember {
        derivedStateOf {
            mainListState.firstVisibleItemIndex == 0 &&
                    mainListState.firstVisibleItemScrollOffset == 0
        }
    }
    val isAtEnd by remember {
        derivedStateOf {
            val lastVisibleItem = mainListState.layoutInfo.visibleItemsInfo.lastOrNull()
            val totalItemsCount = mainListState.layoutInfo.totalItemsCount

            // Check if the last visible item is the last item in the list
            lastVisibleItem != null && lastVisibleItem.index == totalItemsCount - 1
        }
    }

    val shouldShowSwipeToDismiss by remember {
        derivedStateOf { !isScrolling || isAtTop || isAtEnd }
    }


//to show the active week date and day starting from the sunday
//    LaunchedEffect(activeIndex) {
//        if (activeIndex >= 0) {
//            val activeItem = state.calendarItem[activeIndex]
//            val dayOfWeekNumber = when (activeItem.day) {
//                "SUN" -> 1
//                "MON" -> 2
//                "TUE" -> 3
//                "WED" -> 4
//                "THU" -> 5
//                "FRI" -> 6
//                "SAT" -> 7
//                else -> 0
//            }
//            val sundayIndex = (activeIndex - dayOfWeekNumber + 1).coerceAtLeast(0)
//            calendarListState.scrollToItem(sundayIndex)
//        }
//    }

//    fetch the data
    LaunchedEffect(Unit) {
        // onFetchAttendance()
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomCenter,
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = mainListState,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(
                top = MaterialTheme.dimens.small2,
                bottom = MaterialTheme.dimens.swipeToDismissHeight
            )
        ) {
            //    Notification part
            notificationView(
                showNotification = showNotification,
                onChangeNotification = onChangeNotification
            )

            //            calender part
            calendarView(
                calendarItem = state.calendarData,
                today = state.todayBS
            )

            // request section
            requestSection(state = state)

            //birthday section
            birthDaySection(
                state = state
            )

            // anniversary Section
            anniversarySection(
                state = state
            )

            // attendance title
            attendanceSection(
                pagerState = pagerState
            )

        }
        AnimatedVisibility(
            visible = shouldShowSwipeToDismiss,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
        ) {
            SwipeToDismissBox(
                text = "Swipe to Check In",
                onDismissed = {

                }
            )
        }
    }
}


fun LazyListScope.anniversarySection(
    state: HomeScreenState
) {
    item(key = "anniversary title") {
        TitleBar(
            modifier = Modifier.fillMaxWidth().padding(horizontal = MaterialTheme.dimens.small3),
            onViewAll = {},
            title = SharedRes.Strings.work_anniversaries,
            subTitle = SharedRes.Strings.view_all
        )
    }

    item(key = "anniversary list") {
        when {
            state.isAnniversaryLoading -> {

                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = MaterialTheme.dimens.small3),
                    horizontalArrangement = Arrangement.spacedBy(
                        MaterialTheme.dimens.small2,
                        alignment = Alignment.Start
                    )
                ) {
                    repeat(4) {
                        ShimmerView(
                            modifier = Modifier
                                .size(MaterialTheme.dimens.bottomBar)
                                .clip(MaterialTheme.shapes.small)
                        )
                    }
                }

            }

            else -> {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = MaterialTheme.dimens.small3),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = state.upComingWorkAnniversary.size.let { size ->
                        if (size > 2) Arrangement.spacedBy(MaterialTheme.dimens.medium3)
                        else Arrangement.SpaceBetween
                    }

                ) {
                    items(state.upComingWorkAnniversary) { item ->
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
}


fun LazyListScope.birthDaySection(
    state: HomeScreenState
) {
    item(key = "birthday") {
        TitleBar(
            modifier = Modifier.fillMaxWidth().padding(horizontal = MaterialTheme.dimens.small3),
            onViewAll = {},
            title = SharedRes.Strings.upcoming_birthday,
            subTitle = SharedRes.Strings.view_all
        )
    }

    item(key = "birthday list") {
        when {
            state.isBirthDayLoading -> {
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = MaterialTheme.dimens.small3),
                    horizontalArrangement = Arrangement.spacedBy(
                        MaterialTheme.dimens.small2,
                        alignment = Alignment.Start
                    )
                ) {
                    repeat(4) {
                        ShimmerView(
                            modifier = Modifier
                                .size(MaterialTheme.dimens.bottomBar)
                                .clip(MaterialTheme.shapes.small)
                        )
                    }
                }
            }

            else -> {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    contentPadding = PaddingValues(horizontal = MaterialTheme.dimens.small3),
                    horizontalArrangement = state.upComingBirthday.size.let { size ->
                        if (size > 2) Arrangement.spacedBy(MaterialTheme.dimens.medium3)
                        else Arrangement.SpaceBetween
                    }

                ) {
                    items(state.upComingBirthday) { item ->
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
}


fun LazyListScope.attendanceSection(
    pagerState: PagerState
) {
    item(key = "attendance_title") {
        TitleBar(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = MaterialTheme.dimens.small3),
            title = SharedRes.Strings.attendance,
            subTitle = SharedRes.Strings.view_all
        )
    }
    //        attendance chart
    item(key = "Attendance Chart") {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = MaterialTheme.dimens.small3)
        ) { item ->
            AnimatedContent(item) { page ->
                when (page) {
                    0 -> SmoothLineGraph()
                    1 -> Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = MaterialTheme.colorScheme.onPrimaryContainer)
                    )
                }
            }
        }
    }
}

fun LazyListScope.requestSection(
    state: HomeScreenState
) {
    item("request_title") {

        TitleBar(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = MaterialTheme.dimens.small3),
            title = SharedRes.Strings.request
        )
    }

    // request part
//    item(key = "request") {
//        Column(
//            modifier = Modifier.fillMaxWidth()
//                .padding(horizontal = MaterialTheme.dimens.small3)
//        ) {
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth(),
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3)
//            ) {
//                state.requestRow1.forEach { item ->
//                    AttendanceItemContent(
//                        modifier = Modifier.weight(1f),
//                        item = item
//                    )
//                }
//            }
//
//            Spacer(modifier = Modifier.height(MaterialTheme.dimens.small3))
//
//            //  second row
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth(),
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3)
//            ) {
//                state.requestRow2.forEach { item ->
//                    AttendanceItemContent(
//                        modifier = Modifier.weight(1f),
//                        item = item
//                    )
//                }
//            }
//        }
//    }

//    new approach
    state.homeGridItemsToShow.chunked(2).forEach { rowItems ->
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.dimens.small3),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3)
            ) {
                rowItems.forEach { leaveItem ->
                    AttendanceItemContent(
                        modifier = Modifier.weight(1f).fillMaxSize(),
                        item = leaveItem
                    )
                }
                // Fill remaining spaces in row if needed
                repeat(2 - rowItems.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

fun LazyListScope.calendarView(
    calendarItem: List<CalendarDay>,
    today: CalendarDate
) {
    stickyHeader(key = "calender") {
        HorizontalCalendar(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.background),
            date = calendarItem,
            today = today
        )
    }
}

fun LazyListScope.notificationView(
    showNotification: Boolean,
    onChangeNotification: (Boolean) -> Unit
) {
    item(key = "notification") {
        AnimatedVisibility(
            visible = showNotification,
            enter = slideInVertically(
                initialOffsetY = { -it }
            ) + fadeIn(),
            exit = slideOutVertically(
                targetOffsetY = { -it / 2 }
            ) + fadeOut()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        end = MaterialTheme.dimens.small3,
                        start = MaterialTheme.dimens.small3
                    ).background(
                        color = MaterialTheme.colorScheme.error,
                        shape = MaterialTheme.shapes.medium
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier.padding(
                        all = MaterialTheme.dimens.small2
                    ),
                    text = "Notification view",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onError
                    )
                )
                IconButton(onClick = {
                    onChangeNotification(!showNotification)
                }) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "close icon",
                        tint = MaterialTheme.colorScheme.onError
                    )
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
                color = MaterialTheme.colorScheme.borderColor
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
                )
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
    imageUrl: String,
    fullName: String,
    designationName: String,
    date: String,
) {
    Column(
        modifier = Modifier
            .widthIn(min = 150.dp)
//            .border(
//                width = 1.dp,
//                color = MaterialTheme.colorScheme.borderColor,
//                shape = RoundedCornerShape(MaterialTheme.dimens.small2)
//            )
            .padding(
                MaterialTheme.dimens.small2
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small1)
        ) {

            ProfilePicture(
                imageUrl = imageUrl,
                employeeName = fullName,
                nameInitials = fullName.extractInitials(),
                size = MaterialTheme.dimens.medium2,
                shape = CircleShape,
                background = MaterialTheme.colorScheme.imageBackgroundColor,
                borderWidth = 0.dp,
                borderColor = Color.Transparent,
                ratio = 1f
            )

            Text(
                text = "",
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
    modifier: Modifier,
    title: StringResource,
    subTitle: StringResource? = null,
    onViewAll: () -> Unit = {}
) {
    Row(
        modifier = modifier,
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