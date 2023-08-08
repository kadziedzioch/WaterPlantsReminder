package com.example.watermyplants.feature_plant.presentation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
        when(val state = uiState){
            is HomeUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(paddingValues)
                        .padding(15.dp)
                ){
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }
            is HomeUiState.NothingFound ->{
                Text(
                    text = "No plants found",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(top = 20.dp),
                    textAlign = TextAlign.Center
                )
            }
            is HomeUiState.Success -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ){
                    items(state.plants){ plant->
                        PlantItem(
                            onPlantClicked = {
                                navController.navigate(
                                    Screen.AddEditPlantScreen.route + "?plantId=" + plant.id
                                )
                            },
                            onPlantDeleted = { /*TODO*/ },
                            plant = plant
                        )
                    }
                }

            }
        }
    }


}