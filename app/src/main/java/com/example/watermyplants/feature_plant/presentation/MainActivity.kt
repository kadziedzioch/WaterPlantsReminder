package com.example.watermyplants.feature_plant.presentation

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.watermyplants.feature_plant.presentation.add_edit_plant.AddEditPlantEvent
import com.example.watermyplants.feature_plant.presentation.add_edit_plant.AddEditPlantScreen
import com.example.watermyplants.feature_plant.presentation.add_edit_plant.AddEditPlantViewModel
import com.example.watermyplants.feature_plant.presentation.camera.CameraScreen
import com.example.watermyplants.feature_plant.presentation.home.HomeScreen
import com.example.watermyplants.feature_plant.presentation.util.Screen
import com.example.watermyplants.ui.theme.WaterMyPlantsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WaterMyPlantsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.PlantsScreen.route
                    ){
                        composable(
                            route = Screen.PlantsScreen.route
                        ){
                            HomeScreen(
                                navController = navController
                            )
                        }
                        composable(
                            route = Screen.AddEditPlantScreen.route + "?plantId={plantId}&uri={uri}",
                            arguments = listOf(
                                navArgument(
                                    name = "plantId"
                                ){
                                    type = NavType.IntType
                                    defaultValue = -1
                                },
                                navArgument(
                                    name = "uri"
                                ){
                                    type = NavType.StringType
                                    defaultValue = ""
                                }
                            )
                        )
                        {
                            entry ->
                            val viewModel = hiltViewModel<AddEditPlantViewModel>()
                            entry.savedStateHandle.get<String>("uri")?.let {uri ->
                                if(uri.isNotBlank()){
                                    viewModel.onEvent(AddEditPlantEvent.PhotoPicked(uri))
                                }
                            }

                            AddEditPlantScreen(
                                navController = navController,
                                viewModel = viewModel
                            )
                        }

                        composable(
                            route = Screen.CameraScreen.route
                        ){
                            CameraScreen(
                                navController = navController
                            )
                        }

                    }

                }
            }
        }
    }
}

