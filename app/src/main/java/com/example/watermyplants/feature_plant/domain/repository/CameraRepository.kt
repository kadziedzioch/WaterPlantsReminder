package com.example.watermyplants.feature_plant.domain.repository

import android.media.Image
import android.net.Uri
import androidx.camera.core.ImageProxy
import com.example.watermyplants.feature_plant.domain.utils.Resource
import kotlinx.coroutines.flow.Flow

interface CameraRepository {
    suspend fun saveImage(img: Image, rotation: Float) : Resource<Uri>
}