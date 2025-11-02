package com.gurkha.hr.profile.document

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurkha.hr.profile.model.documentScreen.DocumentScreenAction
import com.gurkha.hr.profile.model.documentScreen.DocumentScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class DocumentScreenViewModel: ViewModel() {
    private val _state = MutableStateFlow(DocumentScreenState())
    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DocumentScreenState()
    )

    fun onAction(action: DocumentScreenAction){
        when(action){
            is DocumentScreenAction.OnProfileSelected -> {

            }
        }
    }
}