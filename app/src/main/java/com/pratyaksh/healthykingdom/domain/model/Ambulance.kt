package com.pratyaksh.healthykingdom.domain.model

import org.osmdroid.util.GeoPoint


data class Ambulance(
    val driverName: String,
    val vehicleNumber: String,
    val vehicleLocation: GeoPoint,
    val driverAge: Int,
    val driverGender: String,
    val isVacant: Boolean ,
    val isOnline: Boolean
)
