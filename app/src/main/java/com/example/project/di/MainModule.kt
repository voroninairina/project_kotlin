package com.example.project.di

import com.example.project.Cartoons
import com.example.project.cartoons.presentation.viewModel.CartoonsDetailsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import com.example.project.navigation.Route
import com.example.project.navigation.TopLevelBackStack

val mainModule = module {
    single { TopLevelBackStack<Route>(Cartoons) }
}