package com.example.project.cartoons.presentation.profile.viewModel

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project.cartoons.data.repository.IProfileRepository
import com.example.project.cartoons.presentation.profile.model.state.EditProfileState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch


class EditProfileViewModel(
    private val repository: IProfileRepository
): ViewModel() {

    private val mutableState = MutableEditProfileState()
    val viewState = mutableState as EditProfileState

    private val _navigationEvent = MutableSharedFlow<String>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            repository.getProfile()?.let {
                mutableState.name = it.name
                mutableState.url = it.url
                mutableState.photoUri = Uri.parse(it.photoUri)
            }
        }
        mutableState.isNeedToShowPermission = true
    }

    fun onNameChanged(name: String) {
        mutableState.name = name
    }

    fun onUrlChanged(url: String) {
        mutableState.url = url
    }

    fun onBackClicked() {
        viewModelScope.launch {
            _navigationEvent.emit("back")
        }
    }

    fun onDoneClicked() {
        viewModelScope.launch {
            repository.setProfile(mutableState.photoUri.toString(), viewState.name, viewState.url)
            _navigationEvent.emit("back")
        }
    }

    fun onImageSelected(uri: Uri?) {
        uri?.let { mutableState.photoUri = it }
    }

    fun onPermissionClosed() {
        mutableState.isNeedToShowPermission = false
    }

    fun onAvatarClicked() {
        mutableState.isNeedToShowSelect = true
    }

    fun onSelectDismiss() {
        mutableState.isNeedToShowSelect = false
    }

    private class MutableEditProfileState : EditProfileState {
        override var photoUri: Uri by mutableStateOf(Uri.EMPTY)
        override var name by mutableStateOf("")
        override var url by mutableStateOf("")
        override var isNeedToShowPermission by mutableStateOf(false)
        override var isNeedToShowSelect: Boolean by mutableStateOf(false)
    }
}