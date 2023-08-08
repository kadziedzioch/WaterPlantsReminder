package com.example.watermyplants.di

import android.app.Application
import android.content.Context
import androidx.camera.core.*
import androidx.room.Room
import com.example.watermyplants.feature_plant.data.data_source.PlantDatabase
import com.example.watermyplants.feature_plant.data.repository.CameraRepositoryImpl
import com.example.watermyplants.feature_plant.data.repository.PlantRepositoryImpl
import com.example.watermyplants.feature_plant.domain.repository.CameraRepository
import com.example.watermyplants.feature_plant.domain.repository.PlantRepository
import com.example.watermyplants.feature_plant.domain.use_case.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePlantDatabase(app: Application): PlantDatabase{
        return Room.databaseBuilder(
            app,
            PlantDatabase::class.java,
            PlantDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideExpenseRepository(db: PlantDatabase,
                                 @ApplicationContext appContext: Context
    ): PlantRepository{
        return PlantRepositoryImpl(db.plantDao, appContext)
    }


    @Provides
    @Singleton
    fun providePlantUseCases(plantRepository: PlantRepository) : PlantUseCases{
        return PlantUseCases(
            deletePlant = DeletePlant(plantRepository),
            insertPlant = InsertPlant(plantRepository),
            getPlantById = GetPlantById(plantRepository),
            getPlants = GetPlants(plantRepository)
        )

    }

    @Provides
    @Singleton
    fun provideCameraRepository(@ApplicationContext appContext: Context
    ): CameraRepository{
        return CameraRepositoryImpl(appContext)
    }

    @Provides
    @Singleton
    fun provideCameraUseCase(cameraRepository: CameraRepository) : SaveImageToExternalStorage{
        return SaveImageToExternalStorage(cameraRepository)
    }



}