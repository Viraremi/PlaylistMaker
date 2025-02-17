package com.practicum.playlistmaker.search.domain.model

sealed interface Resource<T> {
    data class Success<T>(val data: T): Resource<T>
    data class Error<T>(val msg: String): Resource<T>
}