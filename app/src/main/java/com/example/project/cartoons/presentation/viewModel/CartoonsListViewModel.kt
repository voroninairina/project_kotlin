package com.example.project.cartoons.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project.CartoonsDetails
import com.example.project.cartoons.domain.interactor.CartoonsInteractor
import com.example.project.cartoons.domain.model.CartoonsEntity
import com.example.project.cartoons.presentation.model.CartoonsListViewState
import com.example.project.cartoons.presentation.model.CartoonsUiModel
import com.example.project.core.launchLoadingAndError
import com.example.project.navigation.Route
import com.example.project.navigation.TopLevelBackStack
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CartoonsListViewModel(
    private val topLevelBackStack: TopLevelBackStack<Route>,
    private val interactor: CartoonsInteractor,
): ViewModel() {
    private val mutableState = MutableStateFlow(CartoonsListViewState())
    val viewState = mutableState.asStateFlow()

    init {
        loadCartoons()
    }

    fun onCartoonsClick(cartoons: CartoonsUiModel) {
        topLevelBackStack.add(CartoonsDetails(cartoons))
    }

    fun onRetryClick() = loadCartoons()

    private fun loadCartoons() {
        viewModelScope.launchLoadingAndError(
            handleError = { e ->
                updateState(CartoonsListViewState.State.Error(e.localizedMessage.orEmpty()))
            }
        ) {
            updateState(CartoonsListViewState.State.Loading)

            val cartoons = interactor.getCartoons()
            updateState(CartoonsListViewState.State.Success(mapToUi(cartoons)))
        }
    }

    private fun updateState(state: CartoonsListViewState.State) =
        mutableState.update { it.copy(state = state) }

    private fun mapToUi(cartoons: List<CartoonsEntity>): List<CartoonsUiModel> = cartoons.map { cartoons ->
        CartoonsUiModel(
            id = cartoons.id,
            name = cartoons.name,
            status = cartoons.status,
            location = cartoons.location,
            firstSeenIn = cartoons.firstSeenIn,
            imageUrl = cartoons.imageUrl,
        )
    }
}