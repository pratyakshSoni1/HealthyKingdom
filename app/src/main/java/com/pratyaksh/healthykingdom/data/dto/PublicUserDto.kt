package com.pratyaksh.healthykingdom.data.dto

import com.google.firebase.firestore.GeoPoint
import com.pratyaksh.healthykingdom.domain.model.Users
import com.pratyaksh.healthykingdom.utils.Gender

data class PublicUserDto(
    val userName: String? = null,
    val userId: String? = null,
    val providesLocation: Boolean? = null,
    val phone: String? =null,
    val location: GeoPoint? = null,
    val password: String? = null,
    val mail: String = "",
    val gender: String = "",
    val age: Int? = null
)

fun PublicUserDto.toPublicUser(): Users.PublicUser = Users.PublicUser(
    userName,
    userId,
    providesLocation,
    phone,
    location?.toMapsGeopoint(),
    password,
    mail,
    age= age,
    gender = when(gender){
        "M" -> Gender.MALE
        "F" -> Gender.FEMALE
        else -> Gender.OTHERS
    }
)
