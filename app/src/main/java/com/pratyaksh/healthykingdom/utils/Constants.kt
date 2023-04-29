package com.pratyaksh.healthykingdom.utils

object Constants{

    object Collections{
        val HOSPITALS_COLLECTION = "hospitals"
        val AMBLANCE_DRIVERS = "ambulance"
        val PUBLIC_USERS = "public"
    }

    val USER_LOGGED_DS = "healthykingdom.user_logged_datastore"
    val USER_LOGGED_KEY = "healthykingdom.user_logged_key"

}

enum class LoginSignupStatus{

    LOGIN_SUCCESS,
    INVALID_PASSWORD,
    INVALID_PHONE,
    INVALID_NAME_OR_LOCATION,

}