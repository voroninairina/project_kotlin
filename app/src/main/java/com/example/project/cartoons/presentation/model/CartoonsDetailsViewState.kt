package com.example.project.cartoons.presentation.model

data class CartoonsDetailsViewState(
    val cartoons: CartoonsUiModel,
    val rating: Float = 0f,
) {
    val userVoteVisible get() = rating != 0f
}