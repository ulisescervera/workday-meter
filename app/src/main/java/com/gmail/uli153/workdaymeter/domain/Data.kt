package com.gmail.uli153.workdaymeter.domain

sealed class Data<out T> {
    object Loading: Data<Nothing>()
    data class Success<T>(val data: T): Data<T>()
}