package com.pratyaksh.healthykingdom.utils

sealed class Resource<T>(val data: T?, val msg: String?){

    class Success<T>(data:T): Resource<T>(data, null)
    class Loading<T>(data:T, msg: String?): Resource<T>(data, msg)
    class Error<T>(msg: String?): Resource<T>(null, msg)


}
