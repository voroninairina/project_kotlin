package com.example.project.cartoons.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.project.cartoons.data.entity.CartoonsDbEntity

@Dao
interface CartoonsDao {
    @Query("SELECT * FROM CartoonsDbEntity")
    suspend fun getAll(): List<CartoonsDbEntity>

    @Insert
    suspend fun insert(driverDbEntity: CartoonsDbEntity)
}