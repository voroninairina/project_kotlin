package com.example.project.cartoons.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.project.cartoons.data.dao.CartoonsDao
import com.example.project.cartoons.data.entity.CartoonsDbEntity

@Database(entities = [CartoonsDbEntity::class], version = 1)
abstract class CartoonsDatabase : RoomDatabase() {
    abstract fun cartoonsDao(): CartoonsDao
}