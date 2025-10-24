package com.gurkha.hr.viewAllScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurkha.hr.model.viewAll.ViewAllScreenAction
import com.gurkha.hr.model.viewAll.ViewAllScreenState
import com.gurkha.model.upComingBirthday.ui.ViewAllUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.serialization.json.Json

class ViewAllScreenViewModel : ViewModel() {

    private val _state = MutableStateFlow(ViewAllScreenState())

    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ViewAllScreenState()
    )

    fun onAction(action: ViewAllScreenAction) {
        when (action) {
            is ViewAllScreenAction.OnJsonUpdate -> {
                val jsonData = Json.decodeFromString<List<ViewAllUi>>(action.json)
                _state.update {
                    it.copy(
                        data = jsonData
                    )
                }
            }
        }
    }
}