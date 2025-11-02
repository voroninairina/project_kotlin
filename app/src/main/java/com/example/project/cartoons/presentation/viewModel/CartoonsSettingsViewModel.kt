package com.example.project.cartoons.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.example.project.cartoons.domain.interactor.CartoonsInteractor
import com.example.project.cartoons.presentation.model.CartoonsSettingsState
import com.example.project.navigation.Route
import com.example.project.navigation.TopLevelBackStack
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CartoonsSettingsViewModel(
    private val topLevelBackStack: TopLevelBackStack<Route>,
    private val interactor: CartoonsInteractor,
): ViewModel() {
    private val mutableState = MutableStateFlow(CartoonsSettingsState())
    val viewState = mutableState.asStateFlow()

    init {
        viewModelScope.launch{
            interactor.observeAliveFirstSettings().collect { aliveFirst ->
                mutableState.update { it.copy(aliveFirst = aliveFirst) }
            }
        }
    }

    fun onAliveFirstCheckedChange(isChecked: Boolean) {
        mutableState.update { it.copy(aliveFirst = isChecked) }
    }

    fun onBack(){
        topLevelBackStack.removeLast()
    }
    fun onSaveClicked(){
        viewModelScope.launch {
            interactor.setAliveFirstSettings(viewState.value.aliveFirst)
            onBack()
        }
    }
}