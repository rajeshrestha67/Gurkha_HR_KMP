package com.gurkha.hr.domain.uploadImage

import com.gurkha.hr.datastore.user_data.repository.LocalUserDataRepository
import com.gurkha.hr.domain.imageUpload.ImageUploadRepository
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.map
import com.gurkha.hr.networkhelper.onSuccess
import com.gurkha.model.network.DataError
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
                    "PROFILE_IMAGE"->{
                        userDataRepository.saveUserData(userData.copy(
                            imageUrl = filePath
                        ))
                    }
                    "CITIZENSHIP_FRONT"->{
                        userDataRepository.saveUserData(userData.copy(
                            imageUrl = filePath
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