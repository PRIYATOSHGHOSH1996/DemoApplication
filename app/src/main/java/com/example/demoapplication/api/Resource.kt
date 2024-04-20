package com.example.demoapplication.api

open class Resource<out T>(val status: Status, val message: String?) {
    class Error<out T>(message: String?, val throwable: Throwable? = null) :
        Resource<T>(Status.ERROR, message)

    class Success<out T>(val data: T) : Resource<T>(Status.SUCCESS, null)
    class Loading<out T> : Resource<T>(Status.LOADING, null)
    companion object {
        fun <T> success(data: T): Resource<T> = Success(data)
        fun <T> error(message: String?, throwable: Throwable? = null): Resource<T> =
            Error(message, throwable)

        fun <T> loading(): Resource<T> = Loading()
    }
}

enum class Status {
    SUCCESS, LOADING, ERROR
}

