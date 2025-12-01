package com.example.project

import android.app.Application
import com.example.project.cartoons.di.cartoonsFeatureModule
import com.example.project.di.DbModule
import com.example.project.di.ProfileModule
//import com.example.project.di.contextModule
import com.example.project.di.mainModule
import com.example.project.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module
import com.jakewharton.threetenabp.AndroidThreeTen

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(//contextModule,
                mainModule,
                networkModule,
                cartoonsFeatureModule,
                DbModule,
                ProfileModule,
                //contextModule,
            )
        }
    }
}