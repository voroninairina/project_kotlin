package com.example.project.cartoons.data.api

import com.example.project.cartoons.data.model.CartoonsListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CartoonsApi {
    @GET("documents/Cartoons")
    suspend fun getCartoons(): CartoonsListResponse
}