package com.example.project.cartoons.data.repository
import com.example.project.cartoons.domain.model.ProfileEntity
import kotlinx.coroutines.flow.Flow
import org.threeten.bp.LocalTime

interface IProfileRepository {
    suspend fun getProfile(): ProfileEntity?

    suspend fun setProfile(
        photoUri: String,
        name: String,
        url: String,
        time: LocalTime,
    ): ProfileEntity
    suspend fun observeProfile(): Flow<ProfileEntity>

}
