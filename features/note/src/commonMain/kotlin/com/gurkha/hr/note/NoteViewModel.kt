package com.gurkha.hr.note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurkha.hr.domain.note.allNotes.model.NoteData
import com.gurkha.hr.domain.note.allNotes.useCase.NoteUseCase
import com.gurkha.hr.domain.note.deleteNote.useCase.DeleteNoteUseCase
import com.gurkha.hr.model.note.NoteAction
import com.gurkha.hr.model.note.NoteState
import com.gurkha.hr.networkhelper.onError
import com.gurkha.hr.networkhelper.onSuccess
import com.gurkha.model.network.toErrorMessage
import com.gurkha.model.note.ui.AddedNoteDataUi
import com.gurkha.model.note.ui.NoteDataUi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class NoteViewModel(
    private val noteUseCase: NoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase
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

    private val _successChannel = Channel<String>()
    val successChannel = _successChannel.receiveAsFlow()

    private val _errorChannel = Channel<String>()
    val errorChannel = _errorChannel.receiveAsFlow()


    fun onAction(action: NoteAction) {
        when (action) {
            is NoteAction.OnUpdateNoteDataJson -> {
                val isUpdateValue = action.isUpdate

                if (isUpdateValue == "true") {
                    val data = Json.decodeFromString<NoteDataUi>(action.data)
                    _state.update { currentState ->
                        val updatedList = currentState.noteItem.map { note ->
                            if (note.id == data.id) {
                                note.copy(
                                    id = data.id,
                                    title = data.title,
                                    description = data.description,
                                    isEvent = data.isEvent,
                                    location = data.location,
                                    active = data.active,
                                    startTime = data.startTime,
                                    endTime = data.endTime,
                                    isReminder = data.isReminder,
                                    startDateAD = data.startDateAD,
                                    endDateAD = data.endDateAD,
                                    startDateBS = data.startDateBS,
                                    endDateBS = data.endDateBS
                                )
                            } else note
                        }
                        currentState.copy(
                            hasUpdatedData = true,
                            noteItem = updatedList
                        )
                    }
                } else {
                    val data = Json.decodeFromString<AddedNoteDataUi>(action.data)
                    _state.update { currentState ->
                        val newNote = NoteData(
                            id = data.id,
                            title = data.title,
                            description = data.description,
                            isEvent = data.isEvent,
                            location = data.location,
                            active = data.active,
                            startTime = data.startTime,
                            endTime = data.endTime,
                            isReminder = data.isReminder,
                            startDateAD = data.startDate ,
                            endDateAD = data.endDate ,
                            startDateBS = data.startDate ,
                            endDateBS = data.endDate ,
                        )
                        currentState.copy(
                            hasUpdatedData = true,
                            noteItem = currentState.noteItem + newNote
                        )
                    }
                }

            }

            is NoteAction.OnDeleteIdSelected -> {
                _state.update {
                    it.copy(
                        selectedId = action.id
                    )
                }
            }

            is NoteAction.OnDeleteNote -> {
                val id = state.value.selectedId
                id?.let {
                    deleteNote(it)
                }
            }

            is NoteAction.OnDeleteNoteFromState->{
                _state.update { currentState ->
                    val updatedList = currentState.noteItem.filter { it.id != action.id }
                    currentState.copy(
                        noteItem = updatedList,
                    )
                }
            }

        }
    }

    private fun fetchAllNotes() = viewModelScope.launch {
        _state.update {
            it.copy(
                isFetchingNotes = true,
            )
        }
        noteUseCase().onSuccess { data ->
            _state.update {
                it.copy(
                    isFetchingNotes = false,
                    noteItem = data
                )
            }
        }.onError {
            _state.update {
                it.copy(
                    isFetchingNotes = false,
                )
            }
        }

    }

    private fun deleteNote(id: Int) = viewModelScope.launch {
        _state.update {
            it.copy(
                isDeletingData = true
            )
        }
        deleteNoteUseCase(id = id).onSuccess {data ->
            _state.update { currentState ->
                val updatedList = currentState.noteItem.filter { it.id != id }
                currentState.copy(
                    isDeletingData = false,
                    noteItem = updatedList,
                )
            }
            _successChannel.send(data.message)
        }.onError {error ->
            _state.update {
                it.copy(
                    isDeletingData = false,
                )
            }
            _errorChannel.send(error.toErrorMessage())
        }
    }

    private fun refresh()=viewModelScope.launch {
        _state.update {
            it.copy(
                isRefreshing = true
            )
        }
        fetchAllNotes()
        _state.update {
            it.copy(
                isRefreshing = false
            )
        }
    }
}