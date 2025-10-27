package com.example.project.cartoons.presentation.model

import com.example.project.cartoons.presentation.screen.CartoonsListItem

data class CartoonsListViewState(
    val listState: State = State.Loading,
    val filters: List<CartoonsListFilter> = CartoonsListFilter.entries,
    val currentFilter: CartoonsListFilter = CartoonsListFilter.ALL
) {
    sealed interface State {
        object Loading : State
        data class Error(val error: String) : State
        data class Success(val data: List<CartoonsUiModel>) : State
    }
}

enum class CartoonsListFilter(val text: String) {
    ALL("Все"),
    FAVORITES("Избранные персонажи")
}