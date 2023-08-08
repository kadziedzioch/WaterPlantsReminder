package com.example.watermyplants.feature_plant.domain.model

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.util.UUID
import java.util.concurrent.TimeUnit

@Entity
data class Plant(
    @PrimaryKey val id: Int? = null,
    val name: String,
    val description: String,
    val duration: Long,
    val unit: TimeUnit,
    var workId: UUID? = null,
    val imageUri: Uri
)

class InvalidPlantException(override val message: String): Exception(message)