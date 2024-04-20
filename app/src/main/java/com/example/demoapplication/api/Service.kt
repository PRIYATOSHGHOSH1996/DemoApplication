package com.example.demoapplication.api

import android.util.Log
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class Service {
    companion object {

        fun <S> createService(serviceClass: Class<S>?): S {
            val gson = GsonBuilder().setLenient().create()
            val builder: Retrofit.Builder =
                Retrofit.Builder().addConverterFactory(GsonConverterFactory.create(gson))
                    .baseUrl("https://jsonplaceholder.typicode.com/")
            val httpClient = OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS).writeTimeout(60, TimeUnit.SECONDS)
                .addInterceptor { chain ->
                    val request = chain.request()
                    val response = chain.proceed(request)
                    val responseBodyCopy = response.peekBody(Long.MAX_VALUE)
                    Log.e("server response", responseBodyCopy.string())
                    response
                }.cache(null)
            builder.client(httpClient.build())
            val retrofit = builder.build()
            return retrofit.create(serviceClass)
        }
    }
}