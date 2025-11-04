package com.gurkha.hr.profile.document

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurkha.hr.components.permissions.ProgressNotification
import com.gurkha.hr.domain.uploadImage.UploadImageUseCase
import com.gurkha.hr.domain.userDetail.usecase.FetchUserDetailUseCase
import com.gurkha.hr.logger.AppLogger
import com.gurkha.hr.networkhelper.onError
import com.gurkha.hr.networkhelper.onSuccess
import com.gurkha.hr.profile.model.document_screen.DocumentScreenAction
import com.gurkha.hr.profile.model.document_screen.DocumentScreenState
import com.gurkha.model.network.toErrorMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.ExperimentalTime

class DocumentScreenViewModel(
    private val uploadImageUseCase: UploadImageUseCase,
    private val fetchUserDetailUseCase: FetchUserDetailUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(DocumentScreenState())

    val notification = ProgressNotification()

    private val _successChannel = Channel<String>()
    val successChannel = _successChannel.receiveAsFlow()

    private val _errorChannel = Channel<String>()
    val errorChannel = _errorChannel.receiveAsFlow()


    //    private val notification = ProgressNotification()
    val state = _state
        .onStart {
            fetchUserData()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DocumentScreenState()
        )

    fun onAction(action: DocumentScreenAction) {
        when (action) {
            is DocumentScreenAction.OnSelectedDocument -> {
                println("selected ${action.type}")
                _state.update {
                    it.copy(
                        selectedDocumentType = action.type
                    )
                }
            }

            is DocumentScreenAction.OnReceivedDocumentUri -> {
                uploadImage(uri = action.uri)
            }

            is DocumentScreenAction.OnLongPressed -> {
                _state.update {
                    it.copy(
                        longPressedImage = action.image,
                        isLongImagePressed = true
                    )
                }
            }

            is DocumentScreenAction.OnLongPressedDismiss->{
                _state.update {
                    it.copy(
                        isLongImagePressed = false
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    private fun uploadImage(
        uri: String
    ) = viewModelScope.launch {
        _state.update {
            it.copy(
                isUploading = true
            )
        }
        uploadImageUseCase(
            imageName = _state.value.selectedDocumentType.toString(),
            filePath = uri,
            type = _state.value.selectedDocumentType ?: "",
            onProgress = { progress ->
                viewModelScope.launch {
                    withContext(Dispatchers.Main.immediate) {
                        notification.showNotification(
                            progress = progress
                        )
                    }
                }
            }
        ).onSuccess { data ->
            AppLogger.d(tag = TAG, message = "Document Image Upload Success")
            _state.update {
                it.copy(
                    isUploading = false
                )
            }
            _successChannel.send(
                data.a ?: "${_state.value.selectedDocumentType} uploaded successfully"
            )

        }.onError { error ->
            AppLogger.e(tag = TAG, message = "Document Image Upload Failed", error)
            _state.update {
                it.copy(
                    isUploading = false
                )
            }
            _errorChannel.send(error.toErrorMessage())
        }
    }


    private fun fetchUserData() = viewModelScope.launch {
        fetchUserDetailUseCase().onSuccess { data ->
            println("data_document $data")
            _state.update {
                it.copy(
                    documentList = _state.value.documentList.mapIndexed { index, item ->
                        when (index) {
                            0 -> item.copy(

                                uploadedImage = data.citizenshipFrontImage
                            )

                            1 -> item.copy(
                                uploadedImage = data.citizenshipBackImage
                            )

                            2 -> item.copy(
                                uploadedImage = data.nationalId
                            )

                            3 -> item.copy(
                                uploadedImage =  data.slcDocument
                            )

                            4 -> item.copy(
                                uploadedImage =  data.plusTwoImage
                            )

                            5 -> item.copy(
                                uploadedImage =  data.bachelorImage
                            )

                            6 -> item.copy(
                                uploadedImage =  data.masterImage
                            )

                            7 -> item.copy(
                                uploadedImage =  data.experienceDocuments
                            )

                            else -> item
                        }
                    }
                )
            }
        }
    }

    companion object {
        const val TAG = "DocumentScreenViewModel"
    }
}