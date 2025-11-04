package com.example.project.cartoons.data.dataSource

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.example.project.cartoons.data.serializer.ProfileSerializer
import com.example.project.cartoons.domain.model.ProfileEntity

class DataSourceProvider(val context: Context) {
    private val Context.profileDataStore: DataStore<ProfileEntity> by dataStore(
        fileName = "profile.pb",
        serializer = ProfileSerializer
    )

    fun provide() = context.profileDataStore
}
