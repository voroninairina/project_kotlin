package com.example.project.cartoons.presentation.viewModel

import androidx.lifecycle.ViewModel
import com.example.project.cartoons.presentation.model.CartoonsDetailsViewState
import com.example.project.cartoons.presentation.model.CartoonsUiModel
import com.example.project.navigation.Route
import com.example.project.navigation.TopLevelBackStack
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CartoonsDetailsViewModel(
    private val topLevelBackStack: TopLevelBackStack<Route>,
    private val cartoons: CartoonsUiModel,
): ViewModel() {
    private val mutableState = MutableStateFlow(CartoonsDetailsViewState(cartoons))
    val state = mutableState.asStateFlow()

    fun onRatingChanged(rating: Float) {
        mutableState.update { it.copy(rating = rating) }
    }

    fun onBack() {
        topLevelBackStack.removeLast()
    }
}