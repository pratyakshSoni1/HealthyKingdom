package com.pratyaksh.healthykingdom.data.dto

import com.google.firebase.firestore.GeoPoint
import com.pratyaksh.healthykingdom.domain.model.Users

data class PublicUserDto(
    val userName: String? = null,
    val userId: String? = null,
    val providesLocation: Boolean? = null,
    val phone: String? =null,
    val location: GeoPoint? = null,
    val password: String? = null
)

fun PublicUserDto.toPublicUser(): Users.PublicUser = Users.PublicUser(
    userName,
    userId,
    providesLocation,
    phone,
    location?.toMapsGeopoint(),
    password
)
