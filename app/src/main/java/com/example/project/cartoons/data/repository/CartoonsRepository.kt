package com.example.project.cartoons.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.example.project.cartoons.data.api.CartoonsApi
import com.example.project.cartoons.data.db.CartoonsDatabase
import com.example.project.cartoons.data.entity.CartoonsDbEntity
import com.example.project.cartoons.data.mapper.CartoonsResponseToEntityMapper
import com.example.project.cartoons.domain.model.CartoonsEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class CartoonsRepository(
    private val api: CartoonsApi,
    private val mapper: CartoonsResponseToEntityMapper,
    private val dataStore: DataStore<Preferences>,
    private val cartoonsDatabase: CartoonsDatabase,
) {
    private val aliveFirstKey = booleanPreferencesKey(ALIVE_FIRST_KEY)
    suspend fun getCartoons(aliveFirst: Boolean) = withContext(Dispatchers.IO) {
        val response = api.getCartoons(orderBy = "$STATUS_KEY ${if (aliveFirst) ORDER_ASC else ORDER_DESC}")
        mapper.mapResponse(response)
    }

    suspend fun setAliveFirstSettings(aliveFirst: Boolean) = withContext(Dispatchers.IO) {
        dataStore.edit {
            it[aliveFirstKey] = aliveFirst
        }
    }

    fun observeAliveFirstSettings(): Flow<Boolean> =
        dataStore.data.map {it[aliveFirstKey] ?: false}

    suspend fun getFavorites() =
        withContext(Dispatchers.IO) {
            cartoonsDatabase.cartoonsDao().getAll().map {
                CartoonsEntity (
                    id = it.id.toString(),
                    name = it.name.orEmpty(),
                    status = it.status.orEmpty(),
                    location = it.location.orEmpty(),
                    firstSeenIn = it.firstSeenIn.orEmpty(),
                    imageUrl = it.imageUrl.orEmpty(),
                )
            }
        }

    suspend fun saveFavorite(cartoons: CartoonsEntity) =
        withContext(Dispatchers.IO) {
            cartoonsDatabase.cartoonsDao().insert(
                CartoonsDbEntity (
                name = cartoons.name,
                status = cartoons.status,
                location = cartoons.location,
                firstSeenIn = cartoons.firstSeenIn,
                    imageUrl = cartoons.imageUrl,
                )
            )
        }

    companion object {
        private const val ALIVE_FIRST_KEY = "ALIVE_FIRST_KEY"

        const val STATUS_KEY = "status"
        const val ORDER_ASC = "asc"
        const val ORDER_DESC = "desc"
    }
}
