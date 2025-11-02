package com.example.project.di

import android.content.Context
import android.preference.PreferenceDataStore
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.example.project.Cartoons
import com.example.project.cartoons.presentation.viewModel.CartoonsDetailsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import com.example.project.navigation.Route
import com.example.project.navigation.TopLevelBackStack
import org.koin.android.ext.koin.androidContext

val mainModule = module {
    single { TopLevelBackStack<Route>(Cartoons) }
    single { getDataStore(androidContext()) }
}

fun getDataStore (androidContext : Context): DataStore<Preferences> =
    PreferenceDataStoreFactory.create {
        androidContext.preferencesDataStoreFile("default")
    }