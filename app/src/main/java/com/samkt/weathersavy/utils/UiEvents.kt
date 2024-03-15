package com.samkt.weathersavy.utils

sealed class UiEvents {
    data class ShowSnackBar(val message: String) : UiEvents()
}
