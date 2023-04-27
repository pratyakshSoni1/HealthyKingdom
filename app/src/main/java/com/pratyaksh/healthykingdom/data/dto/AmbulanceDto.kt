package com.pratyaksh.healthykingdom.data.dto

import com.google.firebase.firestore.GeoPoint
import com.pratyaksh.healthykingdom.domain.model.Ambulance

data class AmbulanceDto(
    val driverName: String = "",
    val vehicleNumber: String = "",
    val vehicleLocation: GeoPoint? = null,
    val driverAge: Int = 0,
    val driverGender: String = "",
    val isVacant: Boolean = false,
    val isOnline: Boolean = false,
    val password: String? = null
)

fun AmbulanceDto.toAmbulance(): Ambulance{

    val gender = when(driverGender){

        "F" -> "Female"
        "M" -> "Male"
        else -> "Other"

    }

    return Ambulance(
        driverName = driverName,
        driverGender = gender,
        driverAge = driverAge,
        vehicleLocation = vehicleLocation!!.toMapsGeopoint(),
        vehicleNumber = vehicleNumber,
        isOnline = isOnline,
        isVacant = isVacant
    )

}
