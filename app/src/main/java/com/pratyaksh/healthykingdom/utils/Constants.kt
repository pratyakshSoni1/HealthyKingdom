package com.pratyaksh.healthykingdom.utils

object Constants{

    object Collections{
        val HOSPITALS_COLLECTION = "hospitals"
        val AMBLANCE_DRIVERS = "ambulanceDrivers"
    }

}

enum class LoginSignupStatus{

    LOGIN_SUCCESS,
    INVALID_PASSWORD,
    INVALID_PHONE,
    INVALID_NAME_OR_LOCATION,

}