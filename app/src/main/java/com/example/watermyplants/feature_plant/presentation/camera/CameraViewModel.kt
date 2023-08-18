package com.example.watermyplants.feature_plant.presentation.camera

import android.media.Image
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.watermyplants.feature_plant.domain.use_case.SaveImageToExternalStorage
import com.example.watermyplants.feature_plant.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val saveImageToExternalStorage: SaveImageToExternalStorage
): ViewModel() {

    private val channel =  Channel<UiEvent>()
    val eventFlow = channel.receiveAsFlow()

    private val _isSaving = mutableStateOf(false)
    val isSaving : State<Boolean> = _isSaving

    fun onPhotoCaptured(img: Image?, rotation: Float) {
        _isSaving.value = true
        viewModelScope.launch(Dispatchers.IO) {
            img?.let {
                saveImageToExternalStorage(it, rotation).let { result ->
                    when(result){
                        is Resource.Failure -> {
                            channel.send(UiEvent.ShowSnackBar(result.message))
                            _isSaving.value = false
                        }
                        is Resource.Success -> {
                            channel.send(UiEvent.SavePicture(result.result))
                            _isSaving.value = false
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