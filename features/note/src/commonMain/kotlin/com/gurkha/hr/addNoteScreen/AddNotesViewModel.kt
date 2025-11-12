package com.gurkha.hr.addNoteScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurkha.hr.domain.form.RequiredValidationUseCase
import com.gurkha.hr.domain.note.addNote.useCase.AddNoteUseCase
import com.gurkha.hr.domain.note.updateNote.useCase.UpdateNoteUseCase
import com.gurkha.hr.model.addNotes.AddNotesAction
import com.gurkha.hr.model.addNotes.AddNotesState
import com.gurkha.hr.networkhelper.onError
import com.gurkha.hr.networkhelper.onSuccess
import com.gurkha.model.network.toErrorMessage
import com.gurkha.model.note.ui.AddedNoteDataUi
import com.gurkha.model.note.ui.NoteDataUi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class AddNotesViewModel(
    private val requiredValidationUseCase: RequiredValidationUseCase,
    private val addNoteUseCase: AddNoteUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase

) : ViewModel() {
    private val _state = MutableStateFlow(AddNotesState())
    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AddNotesState()
    )

    private val _successChannel = Channel<String>()
    val successChannel = _successChannel.receiveAsFlow()
    private val _dataChannel = Channel<AddedNoteDataUi?>()
    val dataChannel = _dataChannel.receiveAsFlow()

    private val _updateDateChannel = Channel<NoteDataUi?>()
    val updateDateChannel = _updateDateChannel.receiveAsFlow()

    private val _errorChannel = Channel<String>()
    val errorChannel = _errorChannel.receiveAsFlow()

    fun onAction(action: AddNotesAction) {
        when (action) {
            is AddNotesAction.OnTitleChange -> {
                _state.update {
                    it.copy(
                        title = action.title,
                        titleError = null
                    )
                }
            }

            is AddNotesAction.OnDescriptionChange -> {
                _state.update {
                    it.copy(
                        description = action.description, descriptionError = null
                    )
                }
            }

            is AddNotesAction.OnHeaderTabChange -> {
                _state.update {
                    it.copy(
                        selectedHeaderTab = action.tab
                    )
                }
            }

            is AddNotesAction.OnEndDateChange -> {
                _state.update {
                    it.copy(
                        endDate = action.date
                    )
                }
            }

            is AddNotesAction.OnEndTimeChange -> {
                _state.update {
                    it.copy(
                        endTime = action.time
                    )
                }
            }

            is AddNotesAction.OnLocationChange -> {
                _state.update {
                    it.copy(
                        location = action.location
                    )
                }
            }

            is AddNotesAction.OnStartDateChange -> {
                _state.update {
                    it.copy(
                        startDate = action.date
                    )
                }

            }

            is AddNotesAction.OnStartTimeChange -> {
                _state.update {
                    it.copy(
                        startTime = action.time
                    )
                }
            }

            is AddNotesAction.OnToggleEvent -> {
                _state.update {
                    it.copy(
                        isEvent = !it.isEvent
                    )
                }
            }

            is AddNotesAction.OnSubmit -> {
//                AppLogger.i("AddNoteViewModel", "OnSubmit Clicked")
                val titleError = requiredValidationUseCase(state.value.title)
                val descriptionError = requiredValidationUseCase(state.value.description)

                when {
                    titleError != null -> {
                        _state.update {
                            it.copy(
                                titleError = titleError
                            )
                        }
                    }

                    descriptionError != null -> {
                        _state.update {
                            it.copy(
                                descriptionError = descriptionError
                            )
                        }
                    }

                    else -> {
                        val title = state.value.title
                        val description = state.value.description
                        val endTime = state.value.endTime
                        val isEvent = if (state.value.isEvent) "Y" else "N"
                        val isReminder = state.value.isReminder
                        val location = state.value.location
                        val reminderMessage = state.value.reminderMessage
                        val reminderTime = state.value.reminderTime
                        val startTime = state.value.startTime
                        addNotes(
                            active = isEvent,
                            description = description,
                            endTime = endTime,
                            title = title,
                            isReminder = isReminder,
                            location = location,
                            reminderMessage = reminderMessage,
                            reminderTime = reminderTime,
                            startTime = startTime,
                            startDate = startTime,
                            endDate = startTime,
                            isEvent = isEvent,
                        )
                    }
                }
            }

            is AddNotesAction.OnIsEditChange -> {
                _state.update {
                    it.copy(
                        isEdit = !it.isEdit
                    )
                }
            }

            is AddNotesAction.OnUpdateNoteData -> {
                val data = Json.decodeFromString<NoteDataUi>(action.item)
                _state.update {
                    it.copy(
                        noteItemData = data,
                        title = data.title,
                        description = data.description,
//                        startDate = data.startDateAD.,
//                        endDate = data.endDate,
                        startTime = data.startTime,
                        endTime = data.endTime,
                        location = data.location,
//                        isEvent = data.isEvent
                    )
                }
            }

            is AddNotesAction.UpdateNote -> {
                val titleError = requiredValidationUseCase(state.value.title)
                val descriptionError = requiredValidationUseCase(state.value.description)

                when {
                    titleError != null -> {
                        _state.update {
                            it.copy(
                                titleError = titleError
                            )
                        }
                    }

                    descriptionError != null -> {
                        _state.update {
                            it.copy(
                                descriptionError = descriptionError
                            )
                        }
                    }

                    else -> {
                        val updateData = NoteDataUi(
                            title = state.value.title,
                            description = state.value.description,
                            startTime = state.value.startTime,
                            endTime = state.value.endTime,
                            location = state.value.location,
                            isEvent = state.value.isEvent.toString(),
                            isReminder = state.value.isReminder,
                            id = state.value.noteItemData?.id ?: 0,
                            startDateAD = state.value.startDate.toString(),
                            endDateAD = state.value.endDate.toString(),
                            startDateBS = state.value.startDate.toString(),
                            endDateBS = state.value.endDate.toString(),
                            active = state.value.noteItemData?.active.toString(),
                        )


                        updateNotes(
                            active = updateData.active,
                            description = updateData.description,
                            endTime = "",
                            isEvent = "N",
                            isReminder = "N",
                            location = "",
                            reminderMessage = "",
                            reminderTime = "",
                            startTime = "",
                            title = updateData.title,
                            id = updateData.id,
                        )
                    }
                }

            }

            is AddNotesAction.OnUpdateDataForStore -> {
                val data = Json.decodeFromString<AddedNoteDataUi>(action.data)
                _state.update {
                    it.copy(
                        storeNoteItem = data
                    )
                }

            }

            is AddNotesAction.OnUpdateData -> {
                val data = Json.decodeFromString<NoteDataUi>(action.data)
                _state.update {
                    it.copy(
                        noteItemData = data
                    )
                }

            }

        }
    }

    private fun addNotes(
        active: String,
        description: String,
        endTime: String,
        isEvent: String,
        isReminder: String,
        location: String,
        reminderMessage: String,
        reminderTime: String,
        startTime: String,
        title: String,
        startDate: String,
        endDate: String,
    ) = viewModelScope.launch {

        addNoteUseCase(
            active = active,
            description = description,
            endTime = endTime,
            isEvent = isEvent,
            isReminder = isReminder,
            location = location,
            reminderMessage = reminderMessage,
            reminderTime = reminderTime,
            startTime = startTime,
            title = title,
            startDate = startDate,
            endDate = endDate
        ).onSuccess { data ->
            _state.update {
                it.copy(
                    isAdding = false,
                    noteItemData = null
                )
            }
            val dataUi = AddedNoteDataUi(
                id = data.id,
                title = data.title,
                description = data.description,
                location = data.location,
                startTime = data.startTime,
                endTime = data.endTime,
                isReminder = data.isReminder,
                isEvent = data.isEvent,
                startDate = data.startDate,
                endDate = data.endDate,
                active = data.active,
                reminderTime = data.reminderTime,
                reminderDate = data.reminderDate,
                reminderMessage = data.reminderMessage,
                createdAt = data.createdAt,
                updatedAt = data.updatedAt,
                message = data.message
            )
//            AppLogger.d("AddNoteViewModel", dataUi.toString())
            _dataChannel.send(dataUi)
            _successChannel.send(data.message)
        }.onError { error ->
            _state.update {
                it.copy(
                    isAdding = false
                )
            }
//            AppLogger.e("AddNoteViewModel",error.toErrorMessage())
            _errorChannel.send(error.toErrorMessage())

        }

    }

    private fun updateNotes(
        active: String,
        description: String,
        endTime: String,
        isEvent: String,
        isReminder: String,
        location: String,
        reminderMessage: String,
        reminderTime: String,
        startTime: String,
        title: String,
        id: Int
    ) = viewModelScope.launch {
        _state.update {
            it.copy(
                isUpdating = true,
            )
        }
        val data = NoteDataUi(
            id = id,
            title = title,
            description = description,
            isEvent = isEvent,
            startDateAD = "",
            endDateAD = "",
            startDateBS = "",
            endDateBS = "",
            location = location,
            active = "N",
            startTime = startTime,
            endTime = endTime,
            isReminder = isReminder
        )
        _updateDateChannel.send(
            data
        )
        updateNoteUseCase(
            active = active,
            description = description,
            endTime = endTime,
            isEvent = isEvent,
            isReminder = isReminder,
            location = location,
            reminderMessage = reminderMessage,
            reminderTime = reminderTime,
            startTime = startTime,
            title = title,
            id = id,
        ).onSuccess { data ->
            _state.update {
                it.copy(
                    isUpdating = false,
                )
            }
            _successChannel.send(data.message)

        }.onError { error ->
            _state.update {
                it.copy(
                    isUpdating = false
                )
            }
            _errorChannel.send(error.toErrorMessage())
        }

    }

}