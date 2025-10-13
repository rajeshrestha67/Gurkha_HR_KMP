package com.gurkha.hr.note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurkha.hr.domain.note.useCase.NoteUseCase
import com.gurkha.hr.model.note.NoteState
import com.gurkha.hr.networkhelper.onSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NoteViewModel(
    private val noteUseCase: NoteUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(NoteState())
    val state = _state
        .onStart {
            fetchAllNotes()
        }
        .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = NoteState()
    )

    private fun fetchAllNotes() = viewModelScope.launch {
        _state.update {
            it.copy(
                isFetchingNotes = true,
            )
        }
        noteUseCase().onSuccess {data ->
            _state.update {
                it.copy(
                    isFetchingNotes = true,
                    noteItem = data
                )
            }
        }

    }
}