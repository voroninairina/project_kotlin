package com.example.project.cartoons.domain.interactor

import com.example.project.cartoons.data.repository.CartoonsRepository
import com.example.project.cartoons.domain.model.CartoonsEntity


class CartoonsInteractor(
    private val repository: CartoonsRepository
) {
    suspend fun getCartoons(aliveFirst: Boolean) = repository.getCartoons(aliveFirst)

    fun observeAliveFirstSettings() = repository.observeAliveFirstSettings()

    suspend fun setAliveFirstSettings(aliveFirst: Boolean) = repository.setAliveFirstSettings(aliveFirst)

    suspend fun saveFavorite(cartoons: CartoonsEntity) = repository.saveFavorite(cartoons)

    suspend fun getFavorites() = repository.getFavorites()
}
