package com.gurkha.hr.domain.uploadImage

import com.gurkha.hr.datastore.user_data.repository.LocalUserDataRepository
import com.gurkha.hr.domain.imageUpload.ImageUploadRepository
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.map
import com.gurkha.hr.networkhelper.onSuccess
import com.gurkha.model.network.DataError
import com.gurkha.model.uploadImage.ImageUpdateDocumentType
import com.gurkha.model.user_data.UserData
import kotlinx.coroutines.flow.firstOrNull

class UploadImageUseCase(
    private val imageUploadRepository: ImageUploadRepository,
    private val userDataRepository: LocalUserDataRepository

) {

    suspend operator fun invoke(
        filePath: String,
        imageName: String,
        onProgress: (Int) -> Unit,
        type: String
    ): ERPResult<UploadImageData, DataError> {
        val employeeId = userDataRepository.userDataFlow.firstOrNull()?.employeeId ?: 0
        return imageUploadRepository.uploadImage(
            filePath = filePath,
            imageName = imageName,
            onProgress = onProgress,
            employeeId = employeeId,
            type = type
        ).onSuccess {
            //this path is stored in db so we need the same path to store in the local
            val filePath = it.data?.fileNames?.firstOrNull()

            //local userData
            val userData = userDataRepository.userDataFlow.firstOrNull() ?: UserData()

            //only update if file path is not null
            filePath?.let {
                when(type){
                    ImageUpdateDocumentType.PROFILE_IMAGE.key ->{
                        userDataRepository.saveUserData(userData.copy(
                            imageUrl = "https://mbank.gurkhahr.com/erp-images/${filePath}"
                        ))
                    }
                    ImageUpdateDocumentType.CITIZENSHIP_FRONT.key ->{
                        userDataRepository.saveUserData(userData.copy(
                            citizenshipFrontImage = "https://mbank.gurkhahr.com/erp-images/${filePath}"
                        ))
                    }
                    ImageUpdateDocumentType.CITIZENSHIP_BACK.key ->{
                        userDataRepository.saveUserData(userData.copy(
                            citizenshipBackImage = "https://mbank.gurkhahr.com/erp-images/${filePath}"
                        ))
                    }
                    ImageUpdateDocumentType.NATIONAL_ID.key ->{
                        userDataRepository.saveUserData(userData.copy(
                            nationalId = "https://mbank.gurkhahr.com/erp-images/${filePath}"
                        ))
                    }
                    ImageUpdateDocumentType.SLC_MARKSHEET.key ->{
                        userDataRepository.saveUserData(userData.copy(
                            slcDocument = "https://mbank.gurkhahr.com/erp-images/${filePath}"
                        ))
                    }
                    ImageUpdateDocumentType.PLUS_TWO_IMAGE.key ->{
                        userDataRepository.saveUserData(userData.copy(
                            plusTwoImage = "https://mbank.gurkhahr.com/erp-images/${filePath}"
                        ))
                    }
                    ImageUpdateDocumentType.BACHELOR.key ->{
                        userDataRepository.saveUserData(userData.copy(
                            bachelorImage = "https://mbank.gurkhahr.com/erp-images/${filePath}"
                        ))
                    }
                    ImageUpdateDocumentType.MASTER.key ->{
                        userDataRepository.saveUserData(userData.copy(
                            masterImage = "https://mbank.gurkhahr.com/erp-images/${filePath}"
                        ))
                    }
                    ImageUpdateDocumentType.EXPERIENCE_DOCUMENT.key ->{
                        userDataRepository.saveUserData(userData.copy(
                            masterImage = "https://mbank.gurkhahr.com/erp-images/${filePath}"
                        ))
                    }
                }
            }
        }
            .map {
            UploadImageData(
                a = it.message,
                imageName = it.data?.fileNames?.firstOrNull()
            )
        }
    }
}