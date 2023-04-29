package com.pratyaksh.healthykingdom.domain.model

import com.google.firebase.Timestamp
import com.pratyaksh.healthykingdom.data.dto.AmbulanceDto
import com.pratyaksh.healthykingdom.data.dto.toFBGeopoint
import org.osmdroid.util.GeoPoint


data class Ambulance(
    val driverName: String,
    val vehicleNumber: String,
    val vehicleLocation: GeoPoint,
    val driverAge: Int,
    val driverGender: String,
    val isVacant: Boolean,
    val isOnline: Boolean,
    val password: String? = null,
    val phone: String? = null,
    val userId: String? = null,
    val lastLocUpdated: Timestamp? = null

)

fun Ambulance.toAmbulanceDto(): AmbulanceDto{
    return AmbulanceDto(
        driverName, vehicleNumber,
        driverAge, driverName, isVacant,
        isOnline, password, phone,
        vehicleLocation.toFBGeopoint(), userId
    )
}
