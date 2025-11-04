package com.gurkha.hr.domain.uploadImage

import com.gurkha.hr.datastore.user_data.repository.LocalUserDataRepository
import com.gurkha.hr.domain.imageUpload.ImageUploadRepository
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.map
import com.gurkha.model.network.DataError
import kotlinx.coroutines.flow.firstOrNull

class UploadImageUseCase(
    private val imageUploadRepository: ImageUploadRepository,
    private val userDataRepository: LocalUserDataRepository

) {

    suspend operator fun invoke(
        filePath: String,
        imageName: String,
        onProgress: (Int) -> Unit
    ): ERPResult<UploadImageData, DataError> {
        val employeeId = userDataRepository.userDataFlow.firstOrNull()?.employeeId ?: 0
        return imageUploadRepository.uploadImage(
            filePath = filePath,
            imageName = imageName,
            onProgress = onProgress,
            employeeId =employeeId
        ).map {
            UploadImageData(
                a = it.message,
                imageName = it.data?.fileNames?.firstOrNull()
            )
        }
    }
}