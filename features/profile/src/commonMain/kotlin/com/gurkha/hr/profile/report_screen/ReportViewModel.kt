package com.gurkha.hr.profile.report_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurkha.hr.domain.form.RequiredValidationUseCase
import com.gurkha.hr.domain.history.useCase.HistoryUseCase
import com.gurkha.hr.domain.reportScreen.useCase.ReportUseCase
import com.gurkha.hr.networkhelper.onSuccess
import com.gurkha.hr.profile.model.report_Screen.ReportScreenState
import com.gurkha.hr.profile.model.report_Screen.ReportScreenViewAction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ReportViewModel(
    private val requiredValidationUseCase: RequiredValidationUseCase,
    private val reportUseCase: ReportUseCase,
    private val historyUseCase: HistoryUseCase

): ViewModel() {

    private val _state = MutableStateFlow(ReportScreenState())
    val state = _state
        .onStart {
            onFetchData()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ReportScreenState()
        )

    private fun onFetchData (
    ) = viewModelScope.launch {
        _state.update { it.copy(isLoading = true)
        }

        reportUseCase(
            bsMonth = _state.value.monthValue,
            bsYear = _state.value.year

        ).onSuccess { data ->
            println("report_data $data")
            _state.update {
                it.copy(
                    isLoading = false,
                    reportListItems = _state.value.reportListItems.mapIndexed { index, reportItems ->
                        when(
                            index
                        ){
                            0 -> reportItems.copy(
                                days = data[0].totalDays.toString()
                            )
                            1 -> reportItems.copy(
                                days = data[0].holidays.toString()
                            )
                            2 -> reportItems.copy(
                                days = data[0].totalWorkingDays.toString()
                            )
                            3 -> reportItems.copy(
                                days = data[0].totalWorkedDays.toString()
                            )
                            4 -> reportItems.copy(
                                days = data[0].totalLeaveTaken.toString()
                            )
                            5 -> reportItems.copy(
                                days = data[0].totalPresentDays.toString()
                            )
                            6 -> reportItems.copy(
                                days = data[0].totalAbsentDays.toString()
                            )
                            else -> reportItems
                        }
                    }
                )
            }
        }
        historyUseCase(
            bsMonth = _state.value.monthValue,
            bsYear = _state.value.year
        ).onSuccess {
            data ->
            println("history_data $data")
            _state.update {
                it.copy(
                    historySummaryList = data
            ) }

        }
    }

    fun onAction(action: ReportScreenViewAction){
        when(action){
            is ReportScreenViewAction.OnItemSelected -> {
                _state.update {
                    it.copy(
                        selectedTab = action.index
                    )
                }
            }
            is ReportScreenViewAction.YearField ->{
                _state.update {
                    it.copy(
                        year = action.year,
                        endYearError = null
                    )
                }
            }
            is ReportScreenViewAction.MonthField ->{
                _state.update {
                    it.copy(
                        monthDisplay = action.showMonth,
                        monthValue = action.month
                    )
                }
            }
            is ReportScreenViewAction.YearFieldError ->{
                _state.update {
                    it.copy(
                        endYearError = action.error)
                }
            }
            is ReportScreenViewAction.MonthFieldError ->{
                _state.update {
                    it.copy(
                        endMonthError = action.error
                    )
                }
            }
            is ReportScreenViewAction .Submit ->{
                _state.update {
                    it.copy(
                        employeeId = action.employeeId
                    )
                }
                submit()
            }
        }
    }
    private fun submit(

    )= viewModelScope.launch {
        val monthPickerError = requiredValidationUseCase(state.value.monthDisplay)

        when{
            monthPickerError != null -> {
                _state.update {
                    it.copy(
                        endMonthError = monthPickerError
                    )
                }
            }
            else ->{
                _state.update {
                    it.copy(
                        endYearError = null
                    )
                }
            }
        }
        onFetchData()
    }
}