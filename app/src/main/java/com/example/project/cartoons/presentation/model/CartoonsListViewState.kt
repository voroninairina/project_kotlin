package com.example.project.cartoons.presentation.model

data class CartoonsListViewState(
    val state: State = State.Loading,
) {
    sealed interface State {
        object Loading : State
        data class Error(val error: String) : State
        data class Success(val data: List<CartoonsUiModel>) : State
    }
}