package com.example.watermyplants.feature_plant.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.watermyplants.feature_plant.data.util.UUIDConverter
import com.example.watermyplants.feature_plant.data.util.UriConverter
import com.example.watermyplants.feature_plant.domain.model.Plant

@Database(
    entities = [Plant::class],
    version = 1
)
@TypeConverters(UUIDConverter::class, UriConverter::class)
abstract class PlantDatabase : RoomDatabase() {

    abstract val plantDao: PlantDao

    companion object{
        const val DATABASE_NAME = "plant_db"
    }
}