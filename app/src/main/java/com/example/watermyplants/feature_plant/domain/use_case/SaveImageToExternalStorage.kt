package com.example.watermyplants.feature_plant.domain.use_case

import android.net.Uri
import androidx.camera.core.ImageProxy
import com.example.watermyplants.feature_plant.domain.repository.CameraRepository
import com.example.watermyplants.feature_plant.domain.utils.Resource

class SaveImageToExternalStorage(
    private val cameraRepository: CameraRepository
) {
    suspend operator fun invoke(image: ImageProxy) : Resource<Uri> {
        return cameraRepository.saveImage(image)
    }
}