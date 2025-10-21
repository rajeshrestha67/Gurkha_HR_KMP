package com.gurkha.hr.domain.uploadImage

import com.gurkha.hr.domain.imageUpload.ImageUploadRepository
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.map
import com.gurkha.model.network.DataError

class UploadImageUseCase(
    private val imageUploadRepository: ImageUploadRepository
) {

    suspend operator fun invoke(
        filePath: String,
        imageName: String
    ): ERPResult<UploadImageData, DataError> {
        return imageUploadRepository.uploadImage(filePath = filePath, imageName = imageName).map {
            UploadImageData(
                a = it.message
            )
        }
    }
}