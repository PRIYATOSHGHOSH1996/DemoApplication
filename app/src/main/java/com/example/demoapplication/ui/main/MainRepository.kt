package com.example.demoapplication.ui.main

import com.example.demoapplication.api.Api
import com.example.demoapplication.api.ApiResponseModel
import com.example.demoapplication.api.BaseRepository
import com.example.demoapplication.api.Service
import kotlinx.coroutines.Dispatchers

class MainRepository:BaseRepository() {
    suspend fun getData():List<ApiResponseModel>? {
        return apiCall(dispatcher = Dispatchers.IO){
            Service.createService(Api::class.java).getData()
        }
    }
}