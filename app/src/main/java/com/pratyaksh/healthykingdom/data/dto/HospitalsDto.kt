package com.pratyaksh.healthykingdom.data.dto

import com.google.firebase.firestore.GeoPoint
import com.pratyaksh.healthykingdom.domain.model.Hospital

data class HospitalsDto (
    val name: String = "",
    val location: GeoPoint? =null,
    val id: String = ""
)

fun HospitalsDto.toHospital(): Hospital {
    return Hospital(
        name = name,
        location = location!!,
        id = id
    )
}

fun GeoPoint.toMapsGeopoint(): org.osmdroid.util.GeoPoint{
    return org.osmdroid.util.GeoPoint(
        latitude,
        longitude
    )
}