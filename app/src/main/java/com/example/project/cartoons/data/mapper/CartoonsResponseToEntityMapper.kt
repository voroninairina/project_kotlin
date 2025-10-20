package com.example.project.cartoons.data.mapper

import com.example.project.cartoons.data.model.CartoonsListResponse
import com.example.project.cartoons.domain.model.CartoonsEntity

class CartoonsResponseToEntityMapper {
    fun mapResponse(response: CartoonsListResponse): List<CartoonsEntity> {
        return response.documents?.map { doc ->
            CartoonsEntity(
                id = doc.name.orEmpty(),
                name = doc.fields?.name?.stringValue.orEmpty(),
                status = doc.fields?.status?.stringValue,
                location = doc.fields?.location?.stringValue,
                firstSeenIn = doc.fields?.firstSeenIn?.stringValue,
                imageUrl = doc.fields?.imageUrl?.stringValue,
            )
        }.orEmpty()
    }
}
