package com.example.project.cartoons.data.repository

import com.example.project.cartoons.data.api.CartoonsApi
import com.example.project.cartoons.data.mapper.CartoonsResponseToEntityMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CartoonsRepository(
    private val api: CartoonsApi,
    private val mapper: CartoonsResponseToEntityMapper,
) {
    suspend fun getCartoons() = withContext(Dispatchers.IO) {
        val response = api.getCartoons()
        mapper.mapResponse(response)
    }
}
