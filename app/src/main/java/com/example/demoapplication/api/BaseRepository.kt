package com.example.demoapplication.api

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.lang.Exception

open class BaseRepository {
    protected suspend fun <T> apiCall(
        dispatcher: CoroutineDispatcher,
        apiCall: suspend () -> Response<out T>
    ): T? {
        return withContext(dispatcher) {
            val response = apiCall.invoke()
            return@withContext if (response.isSuccessful) {
                response.body()
            } else {
                response.errorBody()?.string()
                    ?.takeIf { it.isNotBlank()}.let { throw Exception(it) }
            }
        }
    }
}