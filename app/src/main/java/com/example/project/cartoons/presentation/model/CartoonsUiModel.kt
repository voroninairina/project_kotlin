package com.example.project.cartoons.presentation.model

import kotlinx.serialization.Serializable

@Serializable
data class CartoonsUiModel(
    val id: String,
    val name: String,
    val status: String,
    val location: String?,
    val firstSeenIn: String?,
    val imageUrl: String?,
)