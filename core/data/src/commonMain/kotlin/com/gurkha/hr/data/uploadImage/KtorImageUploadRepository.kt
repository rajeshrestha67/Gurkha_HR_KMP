package com.gurkha.hr.data.uploadImage

import com.gurkha.hr.components.getFileBytes
import com.gurkha.hr.domain.imageUpload.ImageUploadRepository
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.EndPoint
import com.gurkha.hr.networkhelper.safeCall
import com.gurkha.hr.networkhelper.uploadImage
import com.gurkha.model.network.DataError
import com.gurkha.model.uploadImage.UploadImageResponseDto
import io.ktor.client.HttpClient

class KtorImageUploadRepository(
    private val httpClient: HttpClient
) : ImageUploadRepository {
    override suspend fun uploadImage(
        filePath: String,
        imageName: String,
        onProgress: (Int) -> Unit
    ): ERPResult<UploadImageResponseDto, DataError> {
        val bytes = getFileBytes(filePath)
        return safeCall {
            httpClient.uploadImage(
                endPoint = EndPoint.IMAGE_UPLOAD_END_POINT,
                fileName = imageName,
                fileBytes = bytes,
                uri = filePath,
                onProgress = onProgress
            )
        }
    }
}