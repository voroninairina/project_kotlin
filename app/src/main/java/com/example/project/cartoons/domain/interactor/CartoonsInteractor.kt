package com.example.project.cartoons.domain.interactor

import com.example.project.cartoons.data.repository.CartoonsRepository


class CartoonsInteractor(
    private val repository: CartoonsRepository
) {
    suspend fun getCartoons() = repository.getCartoons()
}
