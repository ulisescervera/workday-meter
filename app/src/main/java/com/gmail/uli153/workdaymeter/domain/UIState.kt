package com.gmail.uli153.workdaymeter.domain

sealed class UIState<out T> {
    object Loading: UIState<Nothing>()
    data class Success<T>(val data: T): UIState<T>()
}