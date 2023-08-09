package com.example.watermyplants.feature_plant.presentation.camera

import android.Manifest
import android.graphics.Color
import android.os.Build
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.permissions.*
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen(
    navController: NavController
) {
    val permissions = if (Build.VERSION.SDK_INT <= 28){
        listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }else listOf(Manifest.permission.CAMERA)

    val permissionState = rememberMultiplePermissionsState(
        permissions = permissions)

    if (!permissionState.allPermissionsGranted){
        SideEffect {
            permissionState.launchMultiplePermissionRequest()
        }
    }

    if (permissionState.allPermissionsGranted) {
        CameraScreenContent(
            navigateBack = {navController.navigateUp()},
            navigateBackWithUri = { uri ->
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set("uri", uri)
                navController.popBackStack()
            }
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraScreenContent(
    navigateBack: ()->Unit,
    navigateBackWithUri: (String) -> Unit
) {
    val context = LocalContext.current
    val viewModel = hiltViewModel<CameraViewModel>()
    val lifecycleOwner = LocalLifecycleOwner.current
    val isSaving by viewModel.isSaving
    val cameraController = remember { LifecycleCameraController(context) }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true){
        viewModel.eventFlow.collectLatest { event->
            when(event){
                is CameraViewModel.UiEvent.SavePicture -> {
                    navigateBackWithUri(event.uri.toString())
                }
                is CameraViewModel.UiEvent.ShowSnackBar ->
                {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) {innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ){
            AndroidView(
                modifier = Modifier
                    .fillMaxSize(),
                factory = { context ->
                    PreviewView(context).apply {
                        layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                        setBackgroundColor(Color.BLACK)
                        implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                        scaleType = PreviewView.ScaleType.FILL_START
                    }.also { previewView ->
                        previewView.controller = cameraController
                        cameraController.bindToLifecycle(lifecycleOwner)
                    }
                }
            )
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
            )
            {
                if(isSaving){
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = androidx.compose.ui.graphics.Color.White
                    )
                }

                IconButton(
                    onClick = navigateBack,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        tint = androidx.compose.ui.graphics.Color.White,
                        contentDescription = "Go back"
                    )
                }
                IconButton(
                    onClick = {
                        val mainExecutor = ContextCompat.getMainExecutor(context)
                        cameraController.takePicture(mainExecutor, @ExperimentalGetImage object : ImageCapture.OnImageCapturedCallback() {
                            override fun onCaptureSuccess(image: ImageProxy) {
                                val rotation = image.imageInfo.rotationDegrees.toFloat()
                                val img = image.image
                                viewModel.onPhotoCaptured(img, rotation)
                            }
                            override fun onError(exception: ImageCaptureException) {
                                viewModel.onError(exception.message ?: "Couldn't save image")
                            }
                        })
                    },
                    modifier = Modifier
                        .align(Alignment.BottomCenter),
                    enabled = !isSaving
                ) {
                    Icon(
                        imageVector = Icons.Default.PhotoCamera,
                        tint = androidx.compose.ui.graphics.Color.White,
                        contentDescription = "Take photo",
                        modifier = Modifier.size(60.dp)
                    )
                }

            }

        }
    }




}

