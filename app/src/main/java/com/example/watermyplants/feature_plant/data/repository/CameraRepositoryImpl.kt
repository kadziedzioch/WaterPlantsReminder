package com.example.watermyplants.feature_plant.data.repository

import android.content.ContentValues
import android.content.Context
import android.graphics.*
import android.media.Image
import android.net.Uri
import android.provider.MediaStore
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import com.example.watermyplants.feature_plant.data.util.sdk29AndUp
import com.example.watermyplants.feature_plant.domain.repository.CameraRepository
import com.example.watermyplants.feature_plant.domain.utils.Resource
import java.io.IOException
import java.util.*

@ExperimentalGetImage class CameraRepositoryImpl(
    val context: Context
) : CameraRepository{

    override suspend fun saveImage(img: ImageProxy): Resource<Uri> {

        val imageCollection = sdk29AndUp {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } ?: MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val name = UUID.randomUUID().toString()
        val bitmap = img.image?.toBitmap()?.rotate(img.imageInfo.rotationDegrees.toFloat())

        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "$name.png")
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(MediaStore.Images.Media.WIDTH, bitmap!!.width)
            put(MediaStore.Images.Media.HEIGHT, bitmap.height)
        }

        return try {
            val resolver = context.contentResolver
            val uri = resolver.insert(imageCollection, contentValues)?.also {uri ->
                resolver.openOutputStream(uri).use { outputStream->
                    if(!bitmap!!.compress(Bitmap.CompressFormat.PNG, 100, outputStream)){
                        throw IOException("Couldn't save bitmap")
                    }
                }
            } ?: throw IOException("Couldn't create MediaStore entry")
            Resource.Success(uri)
        } catch (exception: IOException) {
            Resource.Failure(exception.message!!)
        }

    }

    fun Image.toBitmap(): Bitmap {
        val buffer = planes[0].buffer
        buffer.rewind()
        val bytes = ByteArray(buffer.capacity())
        buffer.get(bytes)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    fun Bitmap.rotate(degrees: Float): Bitmap =
        Bitmap.createBitmap(this, 0, 0, width, height, Matrix().apply { postRotate(degrees) }, true)


}