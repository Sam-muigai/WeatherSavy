package com.samkt.weathersavy.utils

sealed class Result<T>(val data: T? = null, val message: String? = null) {}
