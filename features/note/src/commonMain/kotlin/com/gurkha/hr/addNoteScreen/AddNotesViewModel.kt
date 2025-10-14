package com.gurkha.hr.addNoteScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurkha.hr.domain.form.RequiredValidationUseCase
import com.gurkha.hr.domain.note.addNote.useCase.AddNoteUseCase
import com.gurkha.hr.model.addNotes.AddNotesAction
import com.gurkha.hr.model.addNotes.AddNotesState
import com.gurkha.hr.networkhelper.onSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddNotesViewModel(
    private val requiredValidationUseCase: RequiredValidationUseCase,
    private val addNoteUseCase: AddNoteUseCase,

    ) : ViewModel() {
    private val _state = MutableStateFlow(AddNotesState())
    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AddNotesState()
    )

    fun onAction(action: AddNotesAction) {
        when (action) {
            is AddNotesAction.OnTitleChange -> {
                _state.update {
                    it.copy(
                        title = action.title, titleError = null
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
                        val isEvent = if(state.value.isEvent) "Y" else "N"
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
        ).onSuccess {

        }
        println("data_send $active $description $endTime $isEvent $isReminder $location $reminderMessage $reminderTime $startTime $title $startDate $endDate")

    }
}