package com.example.project.cartoons.di

import com.example.project.cartoons.data.api.CartoonsApi
import com.example.project.cartoons.data.mapper.CartoonsResponseToEntityMapper
import com.example.project.cartoons.data.repository.CartoonsRepository
import com.example.project.cartoons.domain.interactor.CartoonsInteractor
import com.example.project.cartoons.presentation.viewModel.CartoonsDetailsViewModel
import com.example.project.cartoons.presentation.viewModel.CartoonsListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit


val cartoonsFeatureModule = module {
    single { get<Retrofit>().create(CartoonsApi::class.java) }

    factory { CartoonsResponseToEntityMapper() }
    single { CartoonsRepository(get(), get()) }

    single { CartoonsInteractor(get()) }

    viewModel { CartoonsListViewModel(get(), get()) }
    viewModel { CartoonsDetailsViewModel(get(), get()) }
}