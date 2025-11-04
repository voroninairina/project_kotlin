package com.example.project.di

import androidx.datastore.core.DataStore
import com.example.project.cartoons.data.dataSource.DataSourceProvider
import com.example.project.cartoons.data.repository.IProfileRepository
import com.example.project.cartoons.data.repository.ProfileRepository
import com.example.project.cartoons.domain.model.ProfileEntity
import com.example.project.cartoons.presentation.profile.viewModel.EditProfileViewModel
import com.example.project.cartoons.presentation.profile.viewModel.ProfileViewModel
import org.koin.dsl.module
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named

val ProfileModule = module {
    factory<DataStore<ProfileEntity>>(named("profile")) { DataSourceProvider(get()).provide() }
    single<IProfileRepository> { ProfileRepository() }

    viewModel { ProfileViewModel(get()) }
    viewModel { EditProfileViewModel(get()) }
}