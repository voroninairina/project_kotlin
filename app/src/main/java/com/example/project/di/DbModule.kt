package com.example.project.di

import android.content.Context
import androidx.room.Room
import com.example.project.cartoons.data.db.CartoonsDatabase
import org.koin.dsl.module

val DbModule = module {
    single { DatabaseBuilder.getInstance(get()) }
}

object DatabaseBuilder {
    private var INSTANCE: CartoonsDatabase? = null

    fun getInstance(context: Context): CartoonsDatabase {
        if (INSTANCE == null) {
            synchronized(CartoonsDatabase::class) {
                INSTANCE = buildRoomDB(context)
            }
        }
        return INSTANCE!!
    }

    private fun buildRoomDB(context: Context) =
        Room.databaseBuilder(
            context.applicationContext,
            CartoonsDatabase::class.java,
            "cartoons"
        ).build()
}