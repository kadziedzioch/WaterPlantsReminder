package com.example.watermyplants.feature_plant.presentation.add_edit_plant

import android.Manifest
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.watermyplants.feature_plant.domain.utils.TimePeriod
import com.example.watermyplants.feature_plant.presentation.add_edit_plant.components.TimePicker
import com.example.watermyplants.feature_plant.presentation.util.Screen
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun AddEditPlantScreen(
    modifier: Modifier = Modifier,
    viewModel: AddEditPlantViewModel,
    navController : NavController
) {

    val permission = if (Build.VERSION.SDK_INT >= 33){
        Manifest.permission.POST_NOTIFICATIONS
    }else ""

    val permissionState = rememberPermissionState(permission = permission)


    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(key1 = true){
        viewModel.eventFlow.collectLatest { event->
            when(event){
                is AddEditPlantViewModel.UiEvent.SavePlant ->
                {
                    //navController.navigateUp()
                }
                is AddEditPlantViewModel.UiEvent.ShowSnackBar -> {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            if(viewModel.imgUri.value.isNotBlank()){
                AsyncImage(
                    model = ImageRequest.Builder(context = LocalContext.current)
                        .data(Uri.parse(viewModel.imgUri.value))
                        .crossfade(true)
                        .build(),
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .clickable {
                            navController.navigate(Screen.CameraScreen.route)
                        },
                    contentScale = ContentScale.FillBounds,
                    contentDescription = null
                )
            }
            else{
                IconButton(onClick = {
                    navController.navigate(Screen.CameraScreen.route)
                }) {
                    Icon(
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape),
                        imageVector = Icons.Default.AddAPhoto,
                        contentDescription = null
                    )
                }
            }

            OutlinedTextField(
                value = viewModel.plantName.value.text,
                onValueChange = {viewModel.onEvent(AddEditPlantEvent.NameEntered(it))},
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Name")}
            )
            OutlinedTextField(
                value = viewModel.plantDescription.value.text,
                onValueChange = {viewModel.onEvent(AddEditPlantEvent.DescriptionEntered(it))},
                maxLines = 10,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                label = { Text(text = "Description")}
            )
            TimePicker(
                duration = viewModel.duration.value.text,
                dropDownMenuState = viewModel.dropDownMenuState.value,
                changeTimePeriod = {viewModel.onEvent(AddEditPlantEvent.TimeUnitEntered(TimePeriod.valueOf(it)))},
                changeExpandedState = {viewModel.onEvent(AddEditPlantEvent.DropDownMenuExpandStateChanged)},
                onTextChange = {viewModel.onEvent(AddEditPlantEvent.DurationEntered(it))}
            )
            Button(
                onClick = {
                    if(Build.VERSION.SDK_INT >= 33){
                        if(permissionState.status.isGranted){
                            viewModel.onEvent(AddEditPlantEvent.PlantSaved)
                        }
                        else{
                            permissionState.launchPermissionRequest()
                        }
                    }
                    else{
                        viewModel.onEvent(AddEditPlantEvent.PlantSaved)
                    }
                }
            ) {
                Text(text="Save")
            }

        }
    }


}