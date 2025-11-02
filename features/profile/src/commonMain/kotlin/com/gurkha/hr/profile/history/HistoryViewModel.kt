package com.gurkha.hr.profile.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurkha.hr.date.data.model.CalendarModel
import com.gurkha.hr.date.getMonthStartAndEndDate
import com.gurkha.hr.domain.form.RequiredValidationUseCase
import com.gurkha.hr.domain.history.useCase.HistoryUseCase
import com.gurkha.hr.networkhelper.onError
import com.gurkha.hr.networkhelper.onSuccess
import com.gurkha.hr.profile.model.history_screen.HistoryScreenViewAction
import com.gurkha.hr.profile.model.history_screen.HistoryState
import com.gurkha.hr.profile.model.history_screen.toUI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val requiredValidationUseCase: RequiredValidationUseCase,
    private val historyUseCase: HistoryUseCase,
    private val calendarModel: CalendarModel

) : ViewModel() {
    private val _state = MutableStateFlow(HistoryState())
    val state = _state
        .onStart {
            onFetchData(isRefreshing = false)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HistoryState()
        )

    private fun onFetchData(
        isRefreshing: Boolean ,
    ) = viewModelScope.launch {
        _state.update {
            it.copy(
                isLoading = true,
                isRefreshing = isRefreshing
            )
        }

        historyUseCase(
            bsMonth = calendarModel.today.month,
            bsYear = calendarModel.today.year
        ).onSuccess { data ->
//            AppLogger.d("HistoryViewModel", "history fetch success ${Json.encodeToString(data)}")
            _state.update {
                it.copy(
                    isLoading = false,
                    isRefreshing= false,
                    historySummaryList = data.map { mData -> mData.toUI() }
                )
            }
        }.onError { error ->
//            AppLogger.e("HistoryViewModel", "history fetch error", error)
            _state.update {
                it.copy(
                    isLoading = false
                )
            }
        }
    }

    fun onAction(action: HistoryScreenViewAction) {
        when (action) {
            is HistoryScreenViewAction.FromYear -> {
                _state.update {
                    it.copy(
                        year = action.year,
                        endYearError = null
                    )
                }
            }

            is HistoryScreenViewAction.FromMonth -> {
                _state.update {
                    it.copy(
                        monthDisplay = action.showMonth,
                        monthValue = action.month,
                        endMonthError = null
                    )
                }
            }

            is HistoryScreenViewAction.YearPickerError -> {
                _state.update {
                    it.copy(
                        endYearError = action.error
                    )
                }
            }

            is HistoryScreenViewAction.MonthPickerError -> {
                _state.update {
                    it.copy(
                        endMonthError = action.error
                    )
                }
            }


            is HistoryScreenViewAction.Submit -> {
                _state.update {
                    it.copy(
                        employeeId = action.employeeId
                    )
                }
                submit()
            }

            is HistoryScreenViewAction.OnRefresh -> {
                onFetchData(isRefreshing = true)
            }

        }
    }

    private fun submit(

    ) = viewModelScope.launch {

        val monthPickerError = requiredValidationUseCase(state.value.monthDisplay)

        when {
            monthPickerError != null -> {
                _state.update {
                    it.copy(
                        endMonthError = monthPickerError
                    )
                }
            }

            else -> {
                _state.update {
                    it.copy(
                        endMonthError = null
                    )
                }
            }
        }
        onFetchData(isRefreshing = false)
    }

}
