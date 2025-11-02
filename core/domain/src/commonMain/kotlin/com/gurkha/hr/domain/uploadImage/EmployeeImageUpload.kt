package com.gurkha.hr.domain.uploadImage

import com.gurkha.hr.datastore.user_data.repository.UserDataRepository
import com.gurkha.hr.domain.imageUpload.ImageUploadRepository
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.map
import com.gurkha.model.network.DataError
import kotlinx.coroutines.flow.firstOrNull

class EmployeeImageUpload (
    private val imageUploadRepository: ImageUploadRepository,
    private val userDataRepository: UserDataRepository
){
    suspend operator fun invoke(
        filePath: String,
        imageName: String,
        onProgress: (Int) -> Unit
    ): ERPResult<UploadImageData, DataError> {
        val id = userDataRepository.userDataFlow.firstOrNull()?.employeeId ?: 0
        return imageUploadRepository.employeeImageUpload(
            filePath = filePath,
            imageName = imageName,
            onProgress = onProgress,
            employeeId = id
        ).map {
            UploadImageData(
                a = it.message,
                imageName = it.data?.fileNames?.firstOrNull()
            )
        }
    }
}