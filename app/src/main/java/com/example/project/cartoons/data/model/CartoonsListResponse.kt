package com.example.project.cartoons.data.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
class CartoonsListResponse(
    val documents: List<CartoonsListDocument>?,
)

@Keep
@Serializable
class CartoonsListDocument(
    val name: String? = null,
    val fields: CartoonsFirestoreModel?,
)

@Keep
@Serializable
class CartoonsFirestoreModel(
    val id: StringFirestoreModel?,
    val name: StringFirestoreModel?,
    val status: StringFirestoreModel?,
    val location: StringFirestoreModel?,
    val firstSeenIn: StringFirestoreModel?,
    val imageUrl: StringFirestoreModel?,
)
