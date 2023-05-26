package com.pratyaksh.healthykingdom.data.dto

import com.google.firebase.firestore.GeoPoint
import com.pratyaksh.healthykingdom.domain.model.Users
import com.pratyaksh.healthykingdom.utils.BloodGroupsInfo
import com.pratyaksh.healthykingdom.utils.Plasma
import com.pratyaksh.healthykingdom.utils.PlasmaGroupInfo
import com.pratyaksh.healthykingdom.utils.PlateletsGroupInfo

data class HospitalsDto (
    val name: String = "",
    val location: GeoPoint? =null,
    val userId: String = "",
    val mail: String = "",
    val phone: String = "",
    val password: String? = null
)

fun HospitalsDto.toHospital(): Users.Hospital {

    return Users.Hospital(
        name = name,
        location = location!!.toMapsGeopoint(),
        userId = userId,
        mail = mail,
        phone = phone,
        password = password!!
    )
}

fun GeoPoint.toMapsGeopoint(): org.osmdroid.util.GeoPoint{
    return org.osmdroid.util.GeoPoint(
        latitude,
        longitude
    )
}
fun org.osmdroid.util.GeoPoint.toFBGeopoint(): GeoPoint{
    return GeoPoint(
        latitude,
        longitude
    )
}