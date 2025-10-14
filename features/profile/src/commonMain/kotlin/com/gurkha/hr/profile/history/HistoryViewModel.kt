package com.gurkha.hr.profile.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurkha.hr.domain.history.useCase.HistoryUseCase
import com.gurkha.hr.networkhelper.onSuccess
import com.gurkha.hr.profile.model.history_screen.HistoryScreenViewAction
import com.gurkha.hr.profile.model.history_screen.HistoryState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val historyUseCase: HistoryUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(HistoryState())
    val state = _state


        .onStart {
            onFetchData()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HistoryState()
        )

    private fun onFetchData(
    ) = viewModelScope.launch {
        _state.update { it.copy(isLoading = true)
        }

        historyUseCase(
            bsMonth = _state.value.bsMonth,
            bsYear = _state.value.bsYear
        ).onSuccess { data ->
            _state.update {
                it.copy(
                    isLoading = false,
                    historySummaryList = data
                )
            }
        }
    }

    fun onAction(action: HistoryScreenViewAction){
        when(action){
            is HistoryScreenViewAction.fromYear -> {
                _state.update {
                    it.copy(
                        bsYear = action.year,
                        endYearError = null
                    )
                }
            }
            is HistoryScreenViewAction.fromMonth -> {
                _state.update {
                    it.copy(
                        bsMonth = action.month,
                        endMonthError = null
                    )
                }
            }
        }

    }
}
