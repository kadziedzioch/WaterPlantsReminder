package com.example.watermyplants.feature_plant.data.data_source

import androidx.room.*
import com.example.watermyplants.feature_plant.domain.model.Plant
import kotlinx.coroutines.flow.Flow

@Dao
interface PlantDao {

    @Query("SELECT * FROM plant")
    fun getPlants() : Flow<List<Plant>>

    @Query("SELECT * FROM plant WHERE id = :id")
    suspend fun getPlantById(id: Int) : Plant

    @Delete
    suspend fun deletePlant(plant: Plant)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlant(plant: Plant)

}