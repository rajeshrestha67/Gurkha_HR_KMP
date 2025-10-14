package com.gurkha.hr.note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurkha.hr.domain.note.allNotes.model.NoteData
import com.gurkha.hr.domain.note.allNotes.useCase.NoteUseCase
import com.gurkha.hr.domain.note.deleteNote.useCase.DeleteNoteUseCase
import com.gurkha.hr.model.note.NoteAction
import com.gurkha.hr.model.note.NoteState
import com.gurkha.hr.networkhelper.onSuccess
import com.gurkha.model.note.ui.NoteDataUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class NoteViewModel(
    private val noteUseCase: NoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(NoteState())
    val state = _state
        .onStart {
            if(_state.value.noteItem.isEmpty()){
                fetchAllNotes()
            }
        }
        .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = NoteState()
    )

    fun onAction(action: NoteAction){
        when(action){
            is NoteAction.OnUpdateNoteDataJson -> {
                val data = Json.decodeFromString<NoteDataUi>(action.data)
                _state.update { currentState ->
                    val updatedList = currentState.noteItem + NoteData(
                        id = data.id,
                        title = data.title,
                        description = data.description,
                        isEvent = data.isEvent,
                        startDateAD = data.startDateAD,
                        endDateAD = data.endDateAD,
                        startDateBS = data.startDateBS,
                        endDateBS = data.endDateBS,
                        location = data.location,
                        active = data.active,
                        startTime = data.startTime,
                        endTime = data.endTime,
                        isReminder = data.isReminder
                    )

                    currentState.copy(
                        hasUpdatedData = true,
                        noteItem = updatedList,
                    )
                }
            }
            is NoteAction.OnDeleteIdSelected-> {
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

        }
    }

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

    private fun deleteNote(id: Int)=viewModelScope.launch{
        _state.update {
            it.copy(

            )
        }
        deleteNoteUseCase(id = id).onSuccess {
            _state.update { currentState ->
                val updatedList = currentState.noteItem.filter { it.id != id }
                currentState.copy(
                    noteItem = updatedList,
                )
            }
        }
    }
}