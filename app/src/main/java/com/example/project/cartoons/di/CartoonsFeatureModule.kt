package com.example.project.cartoons.di

import androidx.datastore.core.DataStore
import com.example.project.cartoons.data.api.CartoonsApi
import com.example.project.cartoons.data.dataSource.DataSourceProvider
import com.example.project.cartoons.data.mapper.CartoonsResponseToEntityMapper
import com.example.project.cartoons.data.repository.CartoonsRepository
import com.example.project.cartoons.data.repository.IProfileRepository
import com.example.project.cartoons.data.repository.ProfileRepository
import com.example.project.cartoons.domain.interactor.CartoonsInteractor
import com.example.project.cartoons.domain.model.ProfileEntity
import com.example.project.cartoons.presentation.profile.viewModel.EditProfileViewModel
import com.example.project.cartoons.presentation.profile.viewModel.ProfileViewModel
import com.example.project.cartoons.presentation.viewModel.CartoonsDetailsViewModel
import com.example.project.cartoons.presentation.viewModel.CartoonsListViewModel
import com.example.project.cartoons.presentation.viewModel.CartoonsSettingsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit


val cartoonsFeatureModule = module {
    single { get<Retrofit>().create(CartoonsApi::class.java) }

    factory { CartoonsResponseToEntityMapper() }
    single { CartoonsRepository(get(), get(), get(), get()) }

    single { CartoonsInteractor(get()) }

    factory<DataStore<ProfileEntity>>(named("profile")) { DataSourceProvider(get()).provide() }
    single<IProfileRepository> { ProfileRepository() }

    viewModel { CartoonsListViewModel(get(), get()) }
    viewModel { CartoonsDetailsViewModel(get(), get(), get()) }
    viewModel { CartoonsSettingsViewModel(get(), get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { EditProfileViewModel (get()) }
}