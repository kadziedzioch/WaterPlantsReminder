package com.example.watermyplants.feature_plant.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.watermyplants.feature_plant.presentation.home.components.PlantItem
import com.example.watermyplants.feature_plant.presentation.util.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(Screen.AddEditPlantScreen.route)
            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(10.dp)
        ) {
            Text(
                text = "My plants",
                style = MaterialTheme.typography.headlineLarge
            )
            Spacer(modifier = Modifier.height(15.dp))
            when(val state = uiState){
                is HomeUiState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ){
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                }
                is HomeUiState.NothingFound ->{
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ){
                        Text(
                            text = "Click button below \n to add plants!",
                            modifier = Modifier.align(Alignment.Center),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
                is HomeUiState.Success -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ){
                        items(state.plants){ plant->
                            PlantItem(
                                onPlantClicked = {
                                    navController.navigate(
                                        Screen.AddEditPlantScreen.route + "?plantId=" + plant.id
                                    )
                                },
                                onPlantDeleted = {
                                    viewModel.deletePlant(plant)
                                },
                                plant = plant
                            )
                        }
                    }

                }
            }
        }

    }


}