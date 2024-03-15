package com.samkt.weathersavy.utils

sealed class Result<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T?, message: String? = null) : Result<T>(data, message)

    class Error<T>(message: String?) : Result<T>(message = message)
}
