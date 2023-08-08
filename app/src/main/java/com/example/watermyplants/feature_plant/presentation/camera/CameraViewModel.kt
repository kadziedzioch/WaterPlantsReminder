package com.example.watermyplants.feature_plant.presentation.camera

import android.media.Image
import android.net.Uri
import androidx.camera.core.ImageProxy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.watermyplants.feature_plant.domain.use_case.SaveImageToExternalStorage
import com.example.watermyplants.feature_plant.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val saveImageToExternalStorage: SaveImageToExternalStorage
): ViewModel() {

    private val channel =  Channel<UiEvent>()
    val eventFlow = channel.receiveAsFlow()

    fun onPhotoCaptured(img: ImageProxy?) {
        viewModelScope.launch {
            img?.let {
                saveImageToExternalStorage(it).let { result ->
                    when(result){
                        is Resource.Failure -> {
                            channel.send(UiEvent.ShowSnackBar(result.message))
                        }
                        is Resource.Success -> {
                            channel.send(UiEvent.SavePicture(result.result))
                        }
                    }
                }

            } ?: channel.send(
                UiEvent.ShowSnackBar("Couldn't save image")
            )
        }

    }

    fun onError(msg: String){
        viewModelScope.launch {
            channel.send(UiEvent.ShowSnackBar(msg))
        }
    }

    sealed class UiEvent {
        data class ShowSnackBar(val message: String) : UiEvent()
        data class SavePicture(val uri: Uri): UiEvent()
    }


}