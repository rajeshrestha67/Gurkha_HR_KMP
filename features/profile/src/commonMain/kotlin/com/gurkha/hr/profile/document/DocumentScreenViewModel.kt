package com.gurkha.hr.profile.document

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurkha.hr.components.permissions.ProgressNotification
import com.gurkha.hr.domain.uploadImage.EmployeeImageUpload
import com.gurkha.hr.domain.uploadImage.UploadImageUseCase
import com.gurkha.hr.networkhelper.onSuccess
import com.gurkha.hr.profile.model.document_screen.DocumentScreenAction
import com.gurkha.hr.profile.model.document_screen.DocumentScreenState
import com.gurkha.hr.profile.model.document_screen.DocumentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.ExperimentalTime

class DocumentScreenViewModel(
    private val uploadImageUseCase: UploadImageUseCase,
    private val employeeImageUpload: EmployeeImageUpload
) : ViewModel() {
    private val _state = MutableStateFlow(DocumentScreenState())

    val notification = ProgressNotification()


    //    private val notification = ProgressNotification()
    val state = _state.stateIn(
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
        employeeImageUpload(
            imageName = _state.value.selectedDocumentType.toString(),
            filePath = uri,
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
            println("successImage $data")
        }
    }
}