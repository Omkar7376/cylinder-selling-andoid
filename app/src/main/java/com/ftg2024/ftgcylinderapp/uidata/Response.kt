package com.ftg2024.ftgcylinderapp.uidata

import java.lang.Exception

sealed class Response<out T> {
    data class Success<out T>(val data: T) : Response<T>()
    data class Error(val exception: Exception, val code : Int? = null) : Response<Nothing>()
    data object Loading : Response<Nothing>()
}
