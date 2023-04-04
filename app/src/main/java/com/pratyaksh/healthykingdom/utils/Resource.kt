package com.pratyaksh.healthykingdom.utils

sealed class Resource<T>(data: T?, msg: String?){

    data class Success<T>(val data:T, val msg: String?)
    data class Loading<T>(val data:T, val msg: String?)
    data class Error<T>(val msg: String?)


}
