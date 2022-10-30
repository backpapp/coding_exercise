package com.backpapp.gvttest.data.api

import com.backpapp.gvttest.data.api.model.CardApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CardApi {
    @GET("card/list")
    suspend fun getCards(): List<CardApiResponse>

    @GET("card/")
    suspend fun getCard(@Query("id") id: String): CardApiResponse

}