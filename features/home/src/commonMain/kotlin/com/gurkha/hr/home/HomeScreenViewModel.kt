package com.gurkha.hr.home

import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurkha.hr.components.permissions.ProgressNotification
import com.gurkha.hr.date.BSPointer
import com.gurkha.hr.date.DateConverter
import com.gurkha.hr.date.Year
import com.gurkha.hr.date.data.DateUtils
import com.gurkha.hr.date.data.model.CalendarModel
import com.gurkha.hr.date.data.model.now
import com.gurkha.hr.date.getMonthStartAndEndDate
import com.gurkha.hr.domain.attendance.attendanceCountReport.model.AttendanceCountReportData
import com.gurkha.hr.domain.attendance.attendanceCountReport.useCase.AttendanceCountReportUseCase
import com.gurkha.hr.domain.attendance.attendanceReport.model.AttendanceData
import com.gurkha.hr.domain.attendance.attendanceReport.usecase.AttendanceUseCase
import com.gurkha.hr.domain.attendance.doAttendance.useCase.DoAttendanceUseCase
import com.gurkha.hr.domain.notification.unSeenNotificationCount.useCase.UnseenNotificationUseCase
import com.gurkha.hr.domain.support.useCase.SupportListFetchUseCase
import com.gurkha.hr.domain.upComingBirthday.usecase.UpComingBirthdayUseCase
import com.gurkha.hr.domain.upComingEvent.useCase.EventUseCase
import com.gurkha.hr.domain.upComingWorkAnniversaries.useCase.UpComingWorkAnniversaryUseCase
import com.gurkha.hr.domain.uploadImage.UploadImageUseCase
import com.gurkha.hr.domain.userDetail.usecase.FetchUserDetailUseCase
import com.gurkha.hr.logger.AppLogger
import com.gurkha.hr.model.home.HomeScreenActions
import com.gurkha.hr.model.home.HomeScreenState
import com.gurkha.hr.model.home.RequestItem
import com.gurkha.hr.model.home.RequestType
import com.gurkha.hr.model.home.toUI
import com.gurkha.hr.networkhelper.onError
import com.gurkha.hr.networkhelper.onSuccess
import com.gurkha.hr.res.SharedRes
import com.gurkha.model.chat.ChatUserData
import com.gurkha.model.network.toErrorMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.Json
import kotlin.time.Clock
import kotlin.time.ExperimentalTime


class HomeScreenViewModel(
    private val attendanceUseCase: AttendanceUseCase,
    private val userDetailUseCase: FetchUserDetailUseCase,
    private val upComingBirthdayUseCase: UpComingBirthdayUseCase,
    private val upComingWorkAnniversaryUseCase: UpComingWorkAnniversaryUseCase,
    private val eventUseCase: EventUseCase,
    private val calendarModel: CalendarModel,
    private val unseenNotificationUseCase: UnseenNotificationUseCase,
    private val uploadImageUseCase: UploadImageUseCase,
    private val doAttendanceUseCase: DoAttendanceUseCase,
    private val attendanceCountReportUseCase: AttendanceCountReportUseCase,
    private val supportListFetchUseCase : SupportListFetchUseCase
) : ViewModel() {
    private val notification = ProgressNotification()

    private var isAlreadyClockIn: Boolean = false

    val datePair = calendarModel.getMonthStartAndEndDate()

    private val _navigateToChatChannel = Channel<String>()
    val navigateToChatChannel = _navigateToChatChannel.receiveAsFlow()

    private val _state = MutableStateFlow(HomeScreenState())
    val state = _state
        .onStart {
            fetchCurrentUser(isRefreshing = false)
            fetchUpComingBirthday()
            fetchUpComingWorkAnniversary()
            fetchAttendance(isRefreshing = false)
            fetchCalendarValue()
            fetchUpComingEvents()
            getUnseenNotificationCount()
            getAttendanceTotalCountReport()
            fetchSupportList()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeScreenState()
        )

    fun onAction(action: HomeScreenActions) {
        when (action) {
            is HomeScreenActions.OnRequestLeave -> {
                TODO()
            }

            is HomeScreenActions.OnCheckInClicked -> {
                TODO()
            }

            is HomeScreenActions.OnRequestAttendance -> {
                TODO()
            }

            is HomeScreenActions.OnSpecificDayClicked -> {
                TODO()
            }

            is HomeScreenActions.OnCheckOutClicked -> TODO()
            is HomeScreenActions.OnNotificationClicked -> {

            }

            is HomeScreenActions.OnSearchedClicked -> TODO()
            is HomeScreenActions.OnDateSelected -> {
                val attendanceData = state.value.attendanceReport.find { data ->
                    val day = getDayFromDate(date = data.date)?.toInt()
                    day == action.day
                }
                _state.update {
                    it.copy(
                        selectedDay = action.day,
                        requests = state.value.requests.updateDuration(
                            attendanceData = attendanceData
                        )
                    )
                }
            }

            is HomeScreenActions.SwipeToDismiss -> {
                uploadImage(uri = action.uri)
            }

            is HomeScreenActions.OnRefresh -> {
                fetchCurrentUser(isRefreshing = true)
                fetchAttendance(isRefreshing = true)

            }

            is HomeScreenActions.OnSpecificUserClicked -> {
                val chatUserData = ChatUserData(
                    employeeId = action.user.employeeId,
                    chatId = action.user.chatId,
                    branchName = action.user.branchName,
                    employeeName = action.user.employeeName,
                    profileImageUrl = action.user.imageUrl,
                    nameInitials = action.user.initials,
                    backgroundColor = action.user.backgroundColor.value,
                    phoneNumber = action.user.phoneNumber
                )
                viewModelScope.launch {
                    _navigateToChatChannel.send(Json.encodeToString(chatUserData))
                }
            }
        }
    }

    private fun List<RequestItem>.updateDuration(attendanceData: AttendanceData?): List<RequestItem> {
        return map { item ->
            attendanceData?.let {
                when (item.type) {
                    RequestType.CheckIn -> item.copy(
                        duration = attendanceData.clockInTime ?: "--:--"
                    )

                    RequestType.CheckOut -> item.copy(
                        duration = attendanceData.clockOutTime ?: "--:--"
                    )

                    else -> item
                }
            } ?: item
        }
    }

    private fun List<RequestItem>.updateAttendance(attendanceCountReportData: AttendanceCountReportData?): List<RequestItem> {
        return map { item ->
            attendanceCountReportData?.let {
                when (item.type) {
                    RequestType.Attendance -> item.copy(
                        duration = attendanceCountReportData.present.toString()
                    )

                    RequestType.Leave -> item.copy(
                        duration = attendanceCountReportData.absent.toString()
                    )

                    else -> item
                }
            } ?: item
        }
    }


    private fun fetchCalendarValue() {
        _state.update {
            it.copy(
                calendarData = calendarModel.numberOfDaysInMonth(),
                todayBS = calendarModel.today,
                selectedDay = calendarModel.today.dayOfMonth
            )
        }
    }

    //    fetch the attendance report
    private fun fetchAttendance(isRefreshing: Boolean) = viewModelScope.launch {
        _state.update {
            it.copy(
                isRefreshing = isRefreshing,
                isAttendanceLoading = true,
                showSwipeView = false,
            )
        }

        val todayDays =
            BSPointer.getNumOfDaysInMonth(
                Year.ofValue(calendarModel.today.year),
                calendarModel.today.month
            )
        val fromDate = DateConverter.bsToAd(
            year = calendarModel.today.year,
            month = calendarModel.today.month,
            day = 1
        ).run {
            "$year-$month-$day"
        }
        val toDate = DateConverter.bsToAd(
            year = calendarModel.today.year,
            month = calendarModel.today.month,
            day = todayDays
        ).run {
            "$year-$month-$day"
        }

        attendanceUseCase(
            fromDate = fromDate,
            toDate = toDate
        ).onSuccess { data ->
            AppLogger.d(tag = TAG, "Attendance Fetch  success")
            val attendanceData = data.find { data ->
                val day = getDayFromDate(date = data.date)?.toInt()
                day == calendarModel.today.dayOfMonth
            }
            _state.update {
                it.copy(
                    isRefreshing = false,
                    isAttendanceLoading = false,
                    attendanceReport = data,
                    todayAttendance = attendanceData,
                    showSwipeView = !(attendanceData?.isHoliday ?: false),

                    attendanceReportHistory = data.filter { mData ->
                        try {
                            val day = getDayFromDate(date = mData.date)?.toInt()
                            day?.let { mDay ->
                                val today = calendarModel.today.dayOfMonth
                                mDay in (today - 7..today)
                            } ?: false
                        } catch (_: Exception) {
                            AppLogger.e(
                                tag = TAG,
                                "fetchAttendance date filter: ${mData.date}"
                            )
                            false
                        }
                    }.map { data ->
                        data.toUI()
                    },
                    requests = _state.value.requests.updateDuration(
                        attendanceData = attendanceData
                    )
                )
            }
            updateSwipeText()


        }.onError { error ->
            AppLogger.e(
                TAG,
                "fetchAttendance date filter: ${error.toErrorMessage()}"
            )
            _state.update {
                it.copy(isAttendanceLoading = false, showSwipeView = true)
            }
        }
    }

    private fun getDayFromDate(date: String): String? {
        if (date.isEmpty()) return null
        val split = date.split("-")
        if (split.size < 3) return null
        return split[2]
    }

    //    fetch the current user details
    private fun fetchCurrentUser(isRefreshing: Boolean) = viewModelScope.launch {
        _state.update {
            it.copy(
                isProfileLoading = true,
                isRefreshing = isRefreshing
            )
        }
        userDetailUseCase(true).onSuccess { data ->
            AppLogger.d(tag = TAG, "CurrentUser Fetch  success")

            _state.update {
                it.copy(
                    isProfileLoading = false,
                    fullName = data.fullName,
                    initials = data.initials,
                    levelName = data.levelName,
                    email = data.email,
                    userProfileUrl = data.userProfileUrl,
                    employeeId = data.employeeId,
                    isProfileComplete = data.isCompleteProfile,
                    isRefreshing = false
                )
            }
            //only fetch the total count after the current userdata fetch cause we need the employee id
        }.onError { error ->
            AppLogger.e(
                tag = TAG,
                "Fetching Current User failed: ${error.toErrorMessage()}"
            )
            _state.update {
                it.copy(
                    isProfileLoading = false
                )
            }
        }
    }

    //    fetch the upcoming birthday
    private fun fetchUpComingBirthday() = viewModelScope.launch {
        _state.update {
            it.copy(isBirthDayLoading = true)
        }
        upComingBirthdayUseCase().onSuccess { data ->
            AppLogger.d(tag = TAG, "Upcoming Birthday Fetch  success")

            _state.update {
                it.copy(
                    isBirthDayLoading = false,
                    upComingBirthday = data
                )
            }
        }.onError { error ->
            AppLogger.e(
                tag = TAG,
                "Fetching Upcoming Birthday failed: ${error.toErrorMessage()}"
            )
            _state.update {
                it.copy(isBirthDayLoading = false)
            }
        }
    }

    //    fetch the upcoming work anniversary
    private fun fetchUpComingWorkAnniversary() = viewModelScope.launch {
        _state.update {
            it.copy(
                isAnniversaryLoading = true
            )
        }
        upComingWorkAnniversaryUseCase().onSuccess { data ->
            AppLogger.d(tag = TAG, "Upcoming Anniversary Fetch  success")

            _state.update {
                it.copy(
                    isAnniversaryLoading = false,
                    upComingWorkAnniversary = data
                )
            }
        }.onError { error ->
            AppLogger.e(
                tag = TAG,
                "Fetching Upcoming Anniversary  failed: ${error.toErrorMessage()}"
            )
            _state.update {
                it.copy(
                    isAnniversaryLoading = false
                )
            }
        }
    }

    private fun fetchUpComingEvents() = viewModelScope.launch {
        _state.update {
            it.copy(
                isEventLoading = true
            )
        }

        eventUseCase().onSuccess { data ->
            AppLogger.d(tag = TAG, "Upcoming Events Fetch  success")

            _state.update {
                it.copy(
                    isEventLoading = false,
                    upComingEvent = data
                )
            }
        }.onError { error ->
            AppLogger.e(
                tag = TAG,
                "Fetching Upcoming Events failed: ${error.toErrorMessage()}"
            )
            _state.update {
                it.copy(
                    isEventLoading = false
                )
            }
        }

    }

    private fun getUnseenNotificationCount() = viewModelScope.launch {
        _state.update {
            it.copy(
                isNotificationCountLoading = true
            )
        }
        unseenNotificationUseCase().onSuccess { data ->
            AppLogger.d(tag = TAG, "Unseen Notification fetch  success")

            _state.update {
                it.copy(
                    isNotificationCountLoading = false,
                    totalUnSeenNotification = data.count
                )
            }
        }.onError { error ->
            AppLogger.e(
                tag = TAG,
                "Unseen Notification fetch failed: ${error.toErrorMessage()}"
            )
        }
    }

    private fun updateSwipeText() = viewModelScope.launch {
        _state.value.todayAttendance?.clockInTime?.let {
            _state.update {
                isAlreadyClockIn = true
                it.copy(
                    swipeText = SharedRes.Strings.swipeToCheckOut,
                )
            }
        } ?: _state.update {
            isAlreadyClockIn = false
            it.copy(
                swipeText = SharedRes.Strings.swipeToCheckIn,
            )
        }
    }

    @OptIn(ExperimentalTime::class)
    private fun uploadImage(
        uri: String,
    ) = viewModelScope.launch {
        _state.update {
            it.copy(
                showSwipeView = false
            )
        }
        val imageName = if (isAlreadyClockIn) "CLOCK_OUT_IMAGE" else "CLOCK_IN_IMAGE"

        uploadImageUseCase(
            imageName = "$imageName${Clock.System.now().toEpochMilliseconds()}",
            filePath = uri,
            type = imageName,
            onProgress = { progress ->
                viewModelScope.launch {
                    withContext(Dispatchers.Main.immediate) {
                        notification.showNotification(
                            progress = progress
                        )
                    }
                }
            }
        ).onSuccess { data ->
            AppLogger.d(tag = TAG, "Image Upload success")
            _state.update {
                it.copy(
                    showSwipeView = true
                )
            }

            doAttendance(
                forDate = LocalDate.now().toString(),
                imageName = data.imageName.toString()
            )
        }.onError { error ->
            AppLogger.e(
                tag = TAG,
                "Image Upload failed: ${error.toErrorMessage()}"
            )
            _state.update {
                it.copy(
                    showSwipeView = true
                )
            }
        }
    }

    private fun doAttendance(
        forDate: String,
        imageName: String
    ) = viewModelScope.launch {
//        get the time when the the attendance use case was triggered
        val currentTime = DateUtils.getCurrentTime()
        doAttendanceUseCase(
            imageName = imageName,
            forDate = forDate
        ).onSuccess {
            AppLogger.d(tag = TAG, "Attendance update success")

            //after the success of the one attendance immediately update the text
            updateSwipeText()

            _state.update {
                if (isAlreadyClockIn) {
                    it.copy(
                        showSwipeView = true,
                        requests = it.requests.updateDuration(
                            attendanceData = it.todayAttendance?.copy(
                                clockOutTime = currentTime
                            )
                        )
                    )
                } else {
                    it.copy(
                        showSwipeView = true,
                        requests = it.requests.updateDuration(
                            attendanceData = it.todayAttendance?.copy(
                                clockInTime = currentTime
                            )
                        )
                    )
                }
            }
        }.onError { error ->
            AppLogger.e(
                tag = TAG,
                "Attendance update failed: ${error.toErrorMessage()}"
            )
            _state.update {
                it.copy(
                    showSwipeView = true
                )
            }
        }
    }

    companion object {
        private const val TAG = "HomeScreenViewModel"
    }

    private fun getAttendanceTotalCountReport() = viewModelScope.launch {
        _state.update {
            it.copy(
                isAttendanceCountLoading = true
            )
        }
        attendanceCountReportUseCase(
            toDate = datePair.first,
            fromDate = datePair.second
        ).onSuccess { data ->
            AppLogger.d(tag = TAG, "Attendance update success")

            _state.update {
                it.copy(
                    isAttendanceCountLoading = false,
                    requests = it.requests.updateAttendance(
                        attendanceCountReportData = data
                    )
                )
            }
        }.onError { error ->
            AppLogger.e(
                tag = TAG,
                "Attendance Count Report Fetch failed: ${error.toErrorMessage()}"
            )
        }
    }

    private fun fetchSupportList()=viewModelScope.launch {
        _state.update {
            it.copy(
                isFetchingSupportList = true
            )
        }
        supportListFetchUseCase().onSuccess {
            _state.update {
                it.copy(
                    isFetchingSupportList = false
                )
            }
            AppLogger.d(tag = TAG, "Support List Fetch success")
        }.onError { error ->
            _state.update {
                it.copy(
                    isFetchingSupportList = false
                )
            }
            AppLogger.e(tag = TAG, "Support List Fetch error",error)
        }
    }
}