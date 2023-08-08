package com.example.watermyplants.feature_plant.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class PlantReminderWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val plantName = inputData.getString(nameKey)
        val description = inputData.getString(descriptionKey)

        val message = String.format( "It's time to water %s \n %s", plantName, description)
        makePlantReminderNotification(
            message ,
            applicationContext
        )

        return Result.success()
    }

    companion object {
        const val nameKey = "NAME"
        const val descriptionKey = "DESCRIPTION"
    }

}