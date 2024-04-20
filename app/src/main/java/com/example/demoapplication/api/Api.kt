package com.example.demoapplication.api

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST

interface Api {
    @GET("posts")
    suspend fun getData(): Response<List<ApiResponseModel>>
}