package com.practicum.playlistmaker.search.domain.consumer

sealed interface ConsumerData<T>{
    data class Data<T>(val value: T): ConsumerData<T>
    data class Error<T>(val msg: String): ConsumerData<T>
}