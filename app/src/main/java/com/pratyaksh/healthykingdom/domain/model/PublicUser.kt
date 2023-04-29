package com.pratyaksh.healthykingdom.domain.model

import com.pratyaksh.healthykingdom.data.dto.PublicUserDto
import com.pratyaksh.healthykingdom.data.dto.toFBGeopoint
import org.osmdroid.util.GeoPoint

data class PublicUser(
    val userName: String?,
    val userId: String?,
    val providesLocation: Boolean?,
    val phone: String?,
    val location: GeoPoint? = null,
    val password: String?
)

fun PublicUser.toPublicUserDto(): PublicUserDto{
    return PublicUserDto(
        userName, userId,
        providesLocation, phone,
        location?.toFBGeopoint(), password
    )
}
