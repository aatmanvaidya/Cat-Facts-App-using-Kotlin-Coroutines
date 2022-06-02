package com.example.catapp


import com.example.catapp.api.CatJson
import retrofit2.Response
import retrofit2.http.GET


interface ApiRequests {
    @GET("/facts/random")
    suspend fun getCatFacts(): Response<CatJson>
}