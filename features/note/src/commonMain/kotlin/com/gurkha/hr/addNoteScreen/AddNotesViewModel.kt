package com.gurkha.hr.addNoteScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurkha.hr.domain.form.RequiredValidationUseCase
import com.gurkha.hr.model.addNotes.AddNotesAction
import com.gurkha.hr.model.addNotes.AddNotesState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddNotesViewModel(
    private val requiredValidationUseCase: RequiredValidationUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(AddNotesState())
    val state = _state.asStateFlow()

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

            is AddNotesAction.OnSubmit -> {
                if (state.value.isEvent) {
                    val titleError = requiredValidationUseCase(state.value.title)
                    val descriptionError = requiredValidationUseCase(state.value.description)
                    val startDateError =
                        requiredValidationUseCase(state.value.startDate?.displayValueBS)
                    val endDateError =
                        requiredValidationUseCase(state.value.endDate?.displayValueBS)
                    val startTimeError = requiredValidationUseCase(state.value.startTime)
                    val endTimeError = requiredValidationUseCase(state.value.endTime)
                    val locationError = requiredValidationUseCase(state.value.location)

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

                        startDateError != null -> {
                            _state.update {
                                it.copy(
                                    startDateError = startDateError
                                )
                            }
                        }

                        endDateError != null -> {
                            _state.update {
                                it.copy(
                                    endDateError = endDateError
                                )
                            }
                        }

                        startTimeError != null -> {
                            _state.update {
                                it.copy(
                                    startTimeError = startTimeError
                                )
                            }
                        }

                        endTimeError != null -> {
                            _state.update {
                                it.copy(
                                    endTimeError = endTimeError
                                )
                            }
                        }

                        locationError != null -> {
                            _state.update {
                                it.copy(
                                    locationError = locationError
                                )
                            }
                        }

                        else -> {
                        }
                    }

                } else {
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

                        }
                    }
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
                        endDate = action.date, endDateError = null
                    )
                }
            }

            is AddNotesAction.OnEndTimeChange -> {
                _state.update {
                    it.copy(
                        endTime = action.time, endTimeError = null
                    )
                }
            }

            is AddNotesAction.OnLocationChange -> {
                _state.update {
                    it.copy(
                        location = action.location, locationError = null
                    )
                }
            }

            is AddNotesAction.OnStartDateChange -> {
                _state.update {
                    it.copy(
                        startDate = action.date, startDateError = null
                    )
                }

            }

            is AddNotesAction.OnStartTimeChange -> {
                _state.update {
                    it.copy(
                        startTime = action.time, startTimeError = null
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
        }
    }

    private fun addNotes()=viewModelScope.launch {

    }
}