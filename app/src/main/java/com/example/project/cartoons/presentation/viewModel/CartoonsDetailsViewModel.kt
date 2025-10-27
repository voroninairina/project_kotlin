package com.example.project.cartoons.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project.cartoons.domain.interactor.CartoonsInteractor
import com.example.project.cartoons.domain.model.CartoonsEntity
import com.example.project.cartoons.presentation.model.CartoonsDetailsViewState
import com.example.project.cartoons.presentation.model.CartoonsUiModel
import com.example.project.navigation.Route
import com.example.project.navigation.TopLevelBackStack
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CartoonsDetailsViewModel(
    private val topLevelBackStack: TopLevelBackStack<Route>,
    private val cartoons: CartoonsUiModel,
    private val interactor: CartoonsInteractor,

): ViewModel() {
    private val mutableState = MutableStateFlow(CartoonsDetailsViewState(cartoons))
    val state = mutableState.asStateFlow()

    fun onRatingChanged(rating: Float) {
        mutableState.update { it.copy(rating = rating) }
        if (rating > 4f){
            viewModelScope.launch {
                interactor.saveFavorite(
                    CartoonsEntity(
                        cartoons.id,
                        cartoons.name,
                        cartoons.status,
                        cartoons.location,
                        cartoons.firstSeenIn,
                        cartoons.imageUrl,
                    )
                )
            }
        }
    }

    fun onBack() {
        topLevelBackStack.removeLast()
    }
}