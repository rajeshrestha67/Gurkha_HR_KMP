package com.gurkha.hr.home

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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gurkha.hr.components.ERPButton
import com.gurkha.hr.components.PlatformMessage
import com.gurkha.hr.components.ProfilePicture
import com.gurkha.hr.components.date.horizontalCalendar.HorizontalCalendar
import com.gurkha.hr.components.dimens
import com.gurkha.hr.components.erpColors
import com.gurkha.hr.components.extractInitials
import com.gurkha.hr.components.media.rememberCameraLauncher
import com.gurkha.hr.components.noRippleClickable
import com.gurkha.hr.components.permissions.CAMERA_PERMISSION
import com.gurkha.hr.components.permissions.navigateToSettings
import com.gurkha.hr.components.permissions.rememberRequestPermission
import com.gurkha.hr.components.shimmer.ShimmerView
import com.gurkha.hr.components.swipeToDismiss.SwipeToDismissBox
import com.gurkha.hr.date.data.CalendarDate
import com.gurkha.hr.date.data.CalendarDay
import com.gurkha.hr.domain.attendance.clockStatusEnum.ClockStatus
import com.gurkha.hr.domain.upComingBirthday.mapper.toUi
import com.gurkha.hr.domain.upComingEvent.mapper.toUi
import com.gurkha.hr.domain.upComingEvent.model.EventData
import com.gurkha.hr.domain.upComingWorkAnniversaries.mapper.toUi
import com.gurkha.hr.logger.AppLogger
import com.gurkha.hr.model.home.AttendanceHistoryItemUI
import com.gurkha.hr.model.home.HomeScreenActions
import com.gurkha.hr.model.home.HomeScreenState
import com.gurkha.hr.model.home.RequestItem
import com.gurkha.hr.res.SharedRes
import com.gurkha.model.upComingBirthday.ui.ViewAllUi
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.ExperimentalTime

const val TAG = "Home Screen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    topAppBarScrollBehavior: TopAppBarScrollBehavior,
    onChatClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onViewAllClick: (String?, String) -> Unit,
    onGoToFixProfile: () -> Unit,
    onToggleFloatingActionButton: (Boolean) -> Unit,
    onGoToAttendanceRequestScreen: (String?, String?) -> Unit,
    onBirthdayUser: (json: String) -> Unit
) {
    val viewModel: HomeScreenViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val anniversaryTitle = stringResource(SharedRes.Strings.work_anniversaries)
    val birthdayTitle = stringResource(SharedRes.Strings.upcoming_birthday)
    val eventTitle = stringResource(SharedRes.Strings.upcoming_events)

    LaunchedEffect(Unit) {
        viewModel.navigateToChatChannel.collect {
            onBirthdayUser(it)
        }
    }


    Scaffold(
        contentWindowInsets = WindowInsets(),
        modifier = Modifier
            .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
            .fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .fillMaxWidth(),
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
                            background = MaterialTheme.erpColors.imageBackgroundColor,
                            borderWidth = 0.5.dp,
                            borderColor = MaterialTheme.colorScheme.outline,
                            ratio = 1f
                        )

                        Column(
                            modifier = Modifier.weight(1f)
                                .padding(horizontal = MaterialTheme.dimens.small1),
                        ) {
                            Text(
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = MaterialTheme.erpColors.primaryTextColor
                                ), text = state.fullName
                            )
                            Text(
                                style = MaterialTheme.typography.titleSmall.copy(
                                    color = MaterialTheme.erpColors.primaryTextColor
                                ), text = state.levelName
                            )
                        }
                    }
                }, actions = {
                    IconButton(onClick = onChatClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Chat,
                            contentDescription = "chat"
                        )
                    }
                    BadgedBox(
                        modifier = Modifier
                            .noRippleClickable(onClick = onNotificationClick)
                            .padding(horizontal = MaterialTheme.dimens.small3),
                        badge = {
                            if (state.totalUnSeenNotification != 0) {
                                Badge(
                                    contentColor = MaterialTheme.colorScheme.onError
                                ) {
                                    Text(text = state.totalUnSeenNotification.toString())
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Notifications,
                            contentDescription = "notification icon"
                        )
                    }
                }, scrollBehavior = topAppBarScrollBehavior
            )
        }) { paddingValues ->
        PullToRefreshBox(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            isRefreshing = state.isRefreshing,
            onRefresh = {
                viewModel.onAction(HomeScreenActions.OnRefresh)
            },
            content = {
                HomeScreenContent(
                    modifier = Modifier.fillMaxSize(),
                    state = state,
                    onAction = viewModel::onAction,
                    onViewAllClick = onViewAllClick,
                    birthdayTitle = birthdayTitle,
                    anniversaryTitle = anniversaryTitle,
                    onGoToFixProfile = onGoToFixProfile,
                    eventTitle = eventTitle,
                    onToggleFloatingActionButton = onToggleFloatingActionButton,
                    onGoToAttendanceRequestScreen = onGoToAttendanceRequestScreen
                )
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    state: HomeScreenState,
    onAction: (HomeScreenActions) -> Unit,
    onViewAllClick: (String?, String) -> Unit,
    birthdayTitle: String,
    anniversaryTitle: String,
    onGoToFixProfile: () -> Unit,
    eventTitle: String,
    onToggleFloatingActionButton: (Boolean) -> Unit,
    onGoToAttendanceRequestScreen: (String?, String?) -> Unit
) {
    var showPermissionModal by remember { mutableStateOf(false) }

    val openCamera = rememberCameraLauncher(
        onImageCaptured = { uri ->
            onAction(HomeScreenActions.SwipeToDismiss(uri = uri))
        },
        onError = { e ->
            println("❌ Error: ${e.message}")
        }
    )

    val platformMessage: PlatformMessage = koinInject()
    val onPermission = rememberRequestPermission(
        permissions = listOf(
            CAMERA_PERMISSION
        ),
        onGranted = { permission ->
            if (permission == CAMERA_PERMISSION) {
                openCamera()
            }
            AppLogger.i(
                tag = TAG,
                message = "Permission granted: $permission"
            )
        },
        onDenied = { permission ->
            AppLogger.i(
                tag = TAG,
                message = "Permission denied: $permission"
            )
            platformMessage.showToast(message = "Permission denied: $permission")
        },
        onPermanentlyDenied = { permission ->
            AppLogger.i(
                tag = TAG,
                message = "Permission denied permanent: $permission"
            )
            showPermissionModal = true
        },
        onAllGranted = {
            AppLogger.i(
                tag = TAG,
                message = "All Permission granted"
            )
        }
    )
    val mainListState = rememberLazyListState()


    val isScrolling by remember {
        derivedStateOf {
            mainListState.isScrollInProgress
        }
    }

    val isAtTop by remember {
        derivedStateOf {
            mainListState.firstVisibleItemIndex == 0 && mainListState.firstVisibleItemScrollOffset == 0
        }
    }
    val isAtEnd by remember {
        derivedStateOf {
            val lastVisibleItem = mainListState.layoutInfo.visibleItemsInfo.lastOrNull()
            val totalItemsCount = mainListState.layoutInfo.totalItemsCount

            lastVisibleItem != null && lastVisibleItem.index == totalItemsCount - 1
        }
    }

    val shouldShowSwipeToDismiss by remember(state.showSwipeView) {
        derivedStateOf { (!isScrolling || isAtTop || isAtEnd) && state.showSwipeView }
    }


    val showSupportChatIcon by remember(state.showSupportChatIcon) {
        derivedStateOf { (!isScrolling || isAtTop || isAtEnd) && state.showSupportChatIcon }
    }

    //cause the shouldShowSwipeToDismiss is not true when there is holiday
    LaunchedEffect(showSupportChatIcon) {
        onToggleFloatingActionButton(showSupportChatIcon)
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
                bottom = MaterialTheme.dimens.swipeToDismissHeight + MaterialTheme.dimens.bottomBar
            )
        ) {
            //    Notification part
            notificationView(
                showNotification = !state.isProfileComplete,
                onGoToFixProfile = onGoToFixProfile
            )

            //            calender part
            calendarView(
                calendarItem = state.calendarData,
                today = state.todayBS,
                selectedDay = state.selectedDay,
                onAction = onAction,
                isLoading = state.isAttendanceLoading
            )

            // request section
            requestSection(state = state)

            // event section
            eventSection(
                state = state,
                eventTitle = eventTitle,
                onViewAllClick = onViewAllClick,
            )

            //birthday section
            birthDaySection(
                state = state,
                onViewAllClick = onViewAllClick,
                birthdayTitle = birthdayTitle,
                onAction = onAction
            )

            // anniversary Section
            anniversarySection(
                state = state,
                onViewAllClick = onViewAllClick,
                anniversaryTitle = anniversaryTitle
            )

            // attendance list
            attendanceSection(
                state = state,
                onGoToAttendanceRequestScreen = onGoToAttendanceRequestScreen
            )

        }
        if (showPermissionModal) {
            PermanentPermissionShow(
                onDismiss = {
                    showPermissionModal = false
                }
            )
        }
        AnimatedVisibility(
            visible = shouldShowSwipeToDismiss,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            SwipeToDismissBox(
                text = stringResource(state.swipeText),
                onDismissed = {
                    onPermission()
                }
            )
        }
    }
}


fun LazyListScope.anniversarySection(
    state: HomeScreenState,
    onViewAllClick: (String?, String) -> Unit,
    anniversaryTitle: String
) {
    val data = Json.encodeToString<List<ViewAllUi>>(state.upComingWorkAnniversary.toUi())
    val title = Json.encodeToString<String>(anniversaryTitle)
    if (state.upComingWorkAnniversary.isNotEmpty()) {
        item(key = "anniversary title") {
            TitleBar(
                modifier = Modifier.fillMaxWidth()
                    .padding(
                        start = MaterialTheme.dimens.small3,
                        end = MaterialTheme.dimens.small1
                    ),
                onViewAll = { onViewAllClick(data, title) },
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
                            MaterialTheme.dimens.small2, alignment = Alignment.Start
                        )
                    ) {
                        repeat(4) {
                            ShimmerView(
                                modifier = Modifier.size(MaterialTheme.dimens.bottomBar)
                                    .clip(MaterialTheme.shapes.small)
                            )
                        }
                    }

                }

                else -> {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = MaterialTheme.dimens.small3),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3)

                    ) {
                        items(state.upComingWorkAnniversary) { item ->
                            UpComingCard(
                                fullName = item.fullName,
                                imageUrl = item.imageUrl,
                                designationName = item.designationName,
                                onChatClicked = {},
                                backgroundColor = item.backgroundColor
                            )
                        }
                    }
                }
            }
        }
    }

}


fun LazyListScope.birthDaySection(
    state: HomeScreenState,
    onViewAllClick: (String?, String) -> Unit,
    birthdayTitle: String,
    onAction: (HomeScreenActions) -> Unit
) {
    val dataToSend = Json.encodeToString<List<ViewAllUi>>(state.upComingBirthday.toUi())
    val title = Json.encodeToString<String>(birthdayTitle)

    if (state.upComingBirthday.isNotEmpty()) {

        item(key = "birthday") {
            TitleBar(
                modifier = Modifier.fillMaxWidth()
                    .padding(
                        start = MaterialTheme.dimens.small3,
                        end = MaterialTheme.dimens.small1
                    ),
                onViewAll = { onViewAllClick(dataToSend, title) },
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
                            MaterialTheme.dimens.small2, alignment = Alignment.Start
                        )
                    ) {
                        repeat(4) {
                            ShimmerView(
                                modifier = Modifier.size(MaterialTheme.dimens.bottomBar)
                                    .clip(MaterialTheme.shapes.small)
                            )
                        }
                    }
                }

                else -> {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        contentPadding = PaddingValues(horizontal = MaterialTheme.dimens.small3),
                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3)
                    ) {
                        items(state.upComingBirthday) { item ->
                            UpComingCard(
                                fullName = item.fullName,
                                imageUrl = item.imageUrl ?: "",
                                designationName = item.designationName,
                                onChatClicked = {
                                    onAction(HomeScreenActions.OnSpecificUserClicked(item))
                                },
                                backgroundColor = item.backgroundColor
                            )
                        }
                    }
                }
            }
        }
    }

}


fun LazyListScope.attendanceSection(
    state: HomeScreenState,
    onGoToAttendanceRequestScreen: (String?, String?) -> Unit
) {
    item(key = "attendance_title") {
        TitleBar(
            modifier = Modifier.fillMaxWidth()
                .padding(start = MaterialTheme.dimens.small3, end = MaterialTheme.dimens.small1),
            title = SharedRes.Strings.attendance,
        )
    }

    if (state.isAttendanceLoading) {
        items(7) {
            ShimmerView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(MaterialTheme.dimens.leaveBoxHeight)
                    .padding(
                        horizontal = MaterialTheme.dimens.small3
                    )
                    .clip(shape = MaterialTheme.shapes.medium)

            )
        }
    } else {
        items(items = state.attendanceReportHistory, key = { it.date }) {
            AttendanceHistoryItem(
                item = it,
                onGoToAttendanceRequestScreen = onGoToAttendanceRequestScreen
            )
        }
    }
}

@Composable
private fun AttendanceHistoryItem(
    item: AttendanceHistoryItemUI,
    onGoToAttendanceRequestScreen: (String?, String?) -> Unit

) {
    var showMore by rememberSaveable { mutableStateOf(false) }
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = MaterialTheme.dimens.small3
            )
            .clip(MaterialTheme.shapes.small),
        tonalElevation = 4.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {

            if (!item.isHoliday) {
                Box(
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    IconButton(
                        onClick = {
                            showMore = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = "More Option"
                        )
                    }
                    DropdownMenu(
                        containerColor = MaterialTheme.colorScheme.background,
                        expanded = showMore,
                        onDismissRequest = {
                            showMore = false
                        }
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = stringResource(SharedRes.Strings.attendanceRequest),
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = MaterialTheme.erpColors.primaryTextColor
                                    )
                                )
                            },
                            onClick = {
                                //date also include day name so only send the date
                                val dateToSend = item.date.split(" ")[0]

                                //send the status base on the time
                                val date = Json.encodeToString(dateToSend)
                                val clockStatus = if (item.clockInTime == "--:--")
                                    Json.encodeToString(ClockStatus.CLOCK_IN)
                                else
                                    Json.encodeToString(ClockStatus.CLOCK_OUT)
                                onGoToAttendanceRequestScreen(date, clockStatus)

                                //hide the show dropDown
                                showMore = false
                            }
                        )
                    }
                }

            }


            Column(
                modifier = Modifier.fillMaxSize().padding(
                    all = MaterialTheme.dimens.small2
                )
            ) {

                Row {

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = stringResource(SharedRes.Strings.date),
                            style = MaterialTheme.typography.titleSmall.copy(
                                color = MaterialTheme.erpColors.darkPrimaryTextColor
                            )
                        )
                        Text(
                            text = item.date,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.erpColors.primaryTextColor
                            )
                        )
                    }

                    Text(
                        modifier = Modifier.padding(horizontal = MaterialTheme.dimens.small2).align(
                            Alignment.CenterVertically
                        ).padding(end = MaterialTheme.dimens.medium1),
                        text = item.status.value, style = MaterialTheme.typography.bodyLarge.copy(
                            color = item.status.color
                        )
                    )
                }


                HorizontalDivider(
                    modifier = Modifier.padding(vertical = MaterialTheme.dimens.small2)
                        .height(MaterialTheme.dimens.extraSmall)
                )


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small2),
                ) {

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = stringResource(SharedRes.Strings.clockIn),
                            style = MaterialTheme.typography.titleSmall.copy(
                                color = MaterialTheme.erpColors.darkPrimaryTextColor
                            )
                        )
                        Text(
                            text = item.clockInTime,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.erpColors.primaryTextColor
                            )
                        )
                    }

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = stringResource(SharedRes.Strings.clockOut),
                            style = MaterialTheme.typography.titleSmall.copy(
                                color = MaterialTheme.erpColors.darkPrimaryTextColor
                            )
                        )
                        Text(
                            text = item.clockOutTime,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.erpColors.primaryTextColor
                            )
                        )
                    }

                    Column(
                        modifier = Modifier.weight(2f)
                    ) {
                        Text(
                            text = stringResource(SharedRes.Strings.status),
                            style = MaterialTheme.typography.titleSmall.copy(
                                color = MaterialTheme.erpColors.darkPrimaryTextColor
                            )
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxSize().padding(vertical = MaterialTheme.dimens.small1),
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.spacedBy(
                                space = MaterialTheme.dimens.small1,
                                alignment = Alignment.Start
                            )
                        ) {

                            repeat(item.statusClips.size) {
                                Text(
                                    modifier = Modifier
                                        .border(
                                            width = 1.dp,
                                            color = MaterialTheme.colorScheme.outline,
                                            shape = MaterialTheme.shapes.small
                                        ).padding(MaterialTheme.dimens.small1),
                                    text = item.statusClips[it],
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = MaterialTheme.erpColors.primaryTextColor
                                    )
                                )

                            }

                        }
                    }

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
                .padding(start = MaterialTheme.dimens.small3, end = MaterialTheme.dimens.small1),
            title = SharedRes.Strings.request
        )
    }

//    new approach
    state.requests.chunked(2).forEach { rowItems ->
        item {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = MaterialTheme.dimens.small3),
                horizontalArrangement = Arrangement.spacedBy(
                    MaterialTheme.dimens.small3
                )
            ) {
                rowItems.forEach { leaveItem ->
                    if (state.isAttendanceCountLoading || state.isRefreshing) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(
                                MaterialTheme.dimens.small3
                            )
                        ) {
                            repeat(2) {
                                ShimmerView(
                                    modifier = Modifier.weight(1f)
                                        .height(MaterialTheme.dimens.leaveBoxHeight)
                                        .clip(MaterialTheme.shapes.medium)
                                )
                            }

                        }
                    } else {
                        AttendanceItemContent(
                            modifier = Modifier.weight(1f).fillMaxSize(),
                            item = leaveItem,
                        )
                    }
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
    today: CalendarDate,
    selectedDay: Int,
    isLoading: Boolean,
    onAction: (HomeScreenActions) -> Unit
) {
    stickyHeader(key = "calender") {
        HorizontalCalendar(
            modifier = Modifier.fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.background),
            days = calendarItem,
            today = today,
            selectedDay = selectedDay,
            isLoading = isLoading,
            onDaySelected = {
                onAction(HomeScreenActions.OnDateSelected(it))
            }
        )
    }
}

fun LazyListScope.notificationView(
    showNotification: Boolean,
    onGoToFixProfile: () -> Unit
) {
    item(key = "notification") {
        AnimatedVisibility(
            visible = showNotification, enter = slideInVertically(
                initialOffsetY = { -it }) + fadeIn(), exit = slideOutVertically(
                targetOffsetY = { -it / 2 }) + fadeOut()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(
                    end = MaterialTheme.dimens.small3, start = MaterialTheme.dimens.small3,
                ).background(
                    color = MaterialTheme.colorScheme.error, shape = MaterialTheme.shapes.medium
                ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier.padding(
                        vertical = MaterialTheme.dimens.small3,
                        horizontal = MaterialTheme.dimens.small2
                    ),
                    text = stringResource(SharedRes.Strings.profile_incomplete),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onError
                    )
                )
                TextButton(
                    onClick = onGoToFixProfile,
                    content = {
                        Text(
                            text = stringResource(SharedRes.Strings.fix_now),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onError
                            )
                        )
                    })
            }
        }
    }
}

//reusable request row
@Composable
fun AttendanceItemContent(
    item: RequestItem, modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.clip(shape = MaterialTheme.shapes.medium),
        tonalElevation = 4.dp
    ) {
        Column(
            modifier = modifier
                .clip(shape = MaterialTheme.shapes.medium),
            verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier.padding(
                    horizontal = MaterialTheme.dimens.small3, vertical = MaterialTheme.dimens.small2
                ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small2)
            ) {
                Icon(imageVector = item.type.icon, contentDescription = "arrow right")
                Text(
                    text = stringResource(item.type.title),
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Column(
                modifier = Modifier.padding(
                    horizontal = MaterialTheme.dimens.small3, vertical = MaterialTheme.dimens.small2
                )
            ) {
                Text(
                    text = item.duration, style = MaterialTheme.typography.titleLarge.copy(
                        color = MaterialTheme.erpColors.primaryTextColor
                    )
                )
                Text(
                    text = stringResource(item.type.status),
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = MaterialTheme.erpColors.primaryTextColor

                    )
                )
            }

        }
    }
}

fun LazyListScope.eventSection(
    state: HomeScreenState,
    onViewAllClick: (String?, String) -> Unit,
    eventTitle: String
) {
    val data = Json.encodeToString<List<ViewAllUi>>(state.upComingEvent.toUi())
    val title = Json.encodeToString<String>(eventTitle)
    if (state.upComingEvent.isNotEmpty()) {
        item(key = "event title") {
            TitleBar(
                modifier = Modifier.fillMaxWidth()
                    .padding(
                        start = MaterialTheme.dimens.small3,
                        end = MaterialTheme.dimens.small1
                    ),
                onViewAll = {
                    onViewAllClick(data, title)
                },
                title = SharedRes.Strings.upcoming_events,
                subTitle = SharedRes.Strings.view_all
            )
        }

        when {
            state.isEventLoading -> {
                items(3) {
                    ShimmerView(
                        modifier = Modifier.size(MaterialTheme.dimens.bottomBar)
                            .clip(MaterialTheme.shapes.small)
                    )
                }
            }

            else -> {
                items(state.upComingEvent.take(n = 3)) { item ->
                    EventCard(
                        item = item
                    )
                }
            }
        }


    }
}


@Composable
fun UpComingCard(
    imageUrl: String,
    fullName: String,
    backgroundColor: Color,
    designationName: String,
    onChatClicked: () -> Unit
) {
    Column(
        modifier = Modifier.widthIn(min = MaterialTheme.dimens.eventWidth)
            .clip(shape = MaterialTheme.shapes.small)
            .background(
                MaterialTheme.colorScheme.primary.copy(
                    alpha = 0.1f
                )
            )
            .clickable(
                onClick = onChatClicked
            )
            .padding(
                vertical = MaterialTheme.dimens.small2,
                horizontal = MaterialTheme.dimens.small3
            ),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small1),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small1)
        ) {

            ProfilePicture(
                imageUrl = imageUrl,
                employeeName = fullName,
                nameInitials = fullName.extractInitials(),
                size = MaterialTheme.dimens.medium3,
                shape = CircleShape,
                background = backgroundColor,
                borderWidth = 0.dp,
                borderColor = Color.Transparent,
                ratio = 1f
            )
        }
        Text(text = fullName, style = MaterialTheme.typography.titleMedium)
        Text(
            text = designationName, style = MaterialTheme.typography.titleSmall.copy(
                color = MaterialTheme.erpColors.primaryTextColor
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
            style = MaterialTheme.typography.titleLarge.copy(
                color = MaterialTheme.colorScheme.primary
            )
        )
        subTitle?.let {
            TextButton(
                onClick = onViewAll
            ) {
                Text(
                    text = stringResource(subTitle),
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = MaterialTheme.erpColors.linkColor
                    )
                )
            }
        }
    }
}


@Composable
fun EventCard(
    item: EventData
) {
    Column(
        modifier = Modifier
            .padding(
                horizontal = MaterialTheme.dimens.small3
            )
            .fillMaxWidth()
            .clip(shape = MaterialTheme.shapes.small)
            .background(
                MaterialTheme.colorScheme.primary.copy(
                    alpha = 0.1f
                )
            )
            .padding(
                vertical = MaterialTheme.dimens.small2,
                horizontal = MaterialTheme.dimens.small3
            ),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small1)

    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(SharedRes.Strings.date),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.erpColors.darkPrimaryTextColor
                )
            )
            Text(
                text = "${item.fromDateBs} - ${item.toDateBs}",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.erpColors.primaryTextColor
                )
            )
        }

        HorizontalDivider(
            modifier = Modifier.padding(vertical = MaterialTheme.dimens.small1)
                .height(MaterialTheme.dimens.extraSmall)
        )

        Text(
            text = item.name,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.erpColors.darkPrimaryTextColor
            )
        )

        Text(
            text = item.description,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.erpColors.primaryTextColor
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermanentPermissionShow(
    onDismiss: () -> Unit
) {
    val navigateToSettings = navigateToSettings()
    ModalBottomSheet(
        modifier = Modifier.fillMaxWidth(),
        onDismissRequest = { onDismiss() },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(MaterialTheme.dimens.chartHeight)
                    .padding(
                        horizontal = MaterialTheme.dimens.small3,
                        vertical = MaterialTheme.dimens.medium2
                    ),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(SharedRes.Strings.allow_permission),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.erpColors.darkPrimaryTextColor
                    ),
                    textAlign = TextAlign.Center
                )

                ERPButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(SharedRes.Strings.go_to_setting),
                    onClick = {
                        navigateToSettings()
                        onDismiss()
                    }
                )
            }
        }

    )
}