package com.gurkha.hr.profile.document

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurkha.hr.components.permissions.ProgressNotification
import com.gurkha.hr.domain.uploadImage.UploadImageUseCase
import com.gurkha.hr.domain.userDetail.usecase.FetchUserDetailUseCase
import com.gurkha.hr.networkhelper.onSuccess
import com.gurkha.hr.profile.model.document_screen.DocumentScreenAction
import com.gurkha.hr.profile.model.document_screen.DocumentScreenState
import com.gurkha.hr.profile.model.document_screen.DocumentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
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
                _state.update {
                    it.copy(
                        selectedDocumentType = action.type
                    )
                }
            }

            is DocumentScreenAction.OnReceivedDocumentUri -> {
                uploadImage(uri = action.uri)
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    private fun uploadImage(
        uri: String
    ) = viewModelScope.launch {
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
        ).onSuccess {
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

                                uploadedImage = if (data.citizenshipFrontImage.isNullOrBlank()) null else data.citizenshipFrontImage
                            )

                            1 -> item.copy(
                                uploadedImage = if (data.citizenshipBackImage.isNullOrBlank()) null else data.citizenshipFrontImage
                            )

                            2 -> item.copy(
                                uploadedImage = if (data.nationalId.isNullOrBlank()) null else data.nationalId
                            )

                            3 -> item.copy(
                                uploadedImage = if (data.slcDocument.isNullOrBlank()) null else data.slcDocument
                            )

                            4 -> item.copy(
                                uploadedImage = if (data.plusTwoImage.isNullOrBlank()) null else data.plusTwoImage
                            )

                            5 -> item.copy(
                                uploadedImage = if (data.bachelorImage.isNullOrBlank()) null else data.bachelorImage
                            )

                            6 -> item.copy(
                                uploadedImage = if (data.masterImage.isNullOrBlank()) null else data.masterImage
                            )

                            7 -> item.copy(
                                uploadedImage = if (data.experienceDocuments.isNullOrBlank()) null else data.experienceDocuments
                            )

                            else -> item
                        }
                    }
                )
            }
        }
    }
}