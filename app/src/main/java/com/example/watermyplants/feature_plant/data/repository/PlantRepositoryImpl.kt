package com.example.watermyplants.feature_plant.data.repository

import android.content.Context
import androidx.work.*
import com.example.watermyplants.feature_plant.data.data_source.PlantDao
import com.example.watermyplants.feature_plant.domain.model.Plant
import com.example.watermyplants.feature_plant.domain.repository.PlantRepository
import com.example.watermyplants.feature_plant.worker.PlantReminderWorker
import kotlinx.coroutines.flow.Flow

class PlantRepositoryImpl(
    private val plantDao: PlantDao,
    private val context: Context
) : PlantRepository {

    private val workManager = WorkManager.getInstance(context)

    override fun getPlants(): Flow<List<Plant>> {
        return plantDao.getPlants()
    }

    override suspend fun getPlantById(id: Int): Plant? {
        return plantDao.getPlantById(id)
    }

    override suspend fun scheduleReminder(plant: Plant) {
        val workId = plant.workId
        if(workId != null){
            workManager.cancelWorkById(workId)
        }
        val data = Data.Builder()
        data.putString(PlantReminderWorker.nameKey, plant.name)
        data.putString(PlantReminderWorker.descriptionKey, plant.description)

        val waterBuilder = PeriodicWorkRequestBuilder<PlantReminderWorker>(plant.duration, plant.unit)
            .setInitialDelay(plant.duration, plant.unit)
            .setInputData(data.build())
            .build()

        workManager.enqueueUniquePeriodicWork(
            java.util.UUID.randomUUID().toString(),
            ExistingPeriodicWorkPolicy.UPDATE,
            waterBuilder
        )
        plant.workId = waterBuilder.id
        plantDao.insertPlant(plant)
    }

    override suspend  fun deleteReminder(plant: Plant) {
        workManager.cancelWorkById(plant.workId!!)
        plantDao.deletePlant(plant)
    }


}