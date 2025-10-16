package com.gurkha.hr.detailNoteScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurkha.hr.domain.note.deleteNote.useCase.DeleteNoteUseCase
import com.gurkha.hr.model.detail.DetailNoteScreenAction
import com.gurkha.hr.model.detail.DetailNoteScreenState
import com.gurkha.hr.networkhelper.onError
import com.gurkha.hr.networkhelper.onSuccess
import com.gurkha.model.network.toErrorMessage
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailNoteScreenViewModel(
    private val deleteNoteUseCase: DeleteNoteUseCase,
): ViewModel() {
    private val _state = MutableStateFlow(DetailNoteScreenState())
    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DetailNoteScreenState()
    )

    private val _successChannel = Channel<String>()
    val successChannel = _successChannel.receiveAsFlow()

    private val _errorChannel = Channel<String>()
    val errorChannel = _errorChannel.receiveAsFlow()



    fun onAction(action: DetailNoteScreenAction) {
        when(action){
            is DetailNoteScreenAction.OnDeleteNote -> {
                val id = action.id
                id?.let {
                    deleteNote(action.id)
                }
            }
        }
    }

    private fun deleteNote(id: Int)=viewModelScope.launch {
        _state.update {
            it.copy(
                isDeleting = true
            )
        }
        deleteNoteUseCase(id = id).onSuccess {data ->
            _state.update {
                it.copy(
                    isDeleting = false,
                )
            }
            _successChannel.send(data.message)
        }.onError { error ->
            _state.update {
                it.copy(
                    isDeleting = false,
                )
            }
            _errorChannel.send(error.toErrorMessage())
        }
    }
}