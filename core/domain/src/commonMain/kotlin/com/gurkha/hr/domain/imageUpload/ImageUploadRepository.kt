package com.gurkha.hr.domain.imageUpload

import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.model.network.DataError
import com.gurkha.model.uploadImage.UploadImageResponseDto

interface ImageUploadRepository {
    suspend fun uploadImage(
        filePath: String,
        imageName: String,
        onProgress: (Int) -> Unit,
        employeeId: Int,
        type: String
    ): ERPResult<UploadImageResponseDto, DataError>
}