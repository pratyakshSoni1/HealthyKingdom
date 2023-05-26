package com.pratyaksh.healthykingdom.domain.model

import com.google.firebase.Timestamp
import com.pratyaksh.healthykingdom.data.dto.AmbulanceDto
import com.pratyaksh.healthykingdom.data.dto.HospitalsDto
import com.pratyaksh.healthykingdom.data.dto.PublicUserDto
import com.pratyaksh.healthykingdom.data.dto.toFBGeopoint
import com.pratyaksh.healthykingdom.utils.BloodGroupsInfo
import com.pratyaksh.healthykingdom.utils.PlasmaGroupInfo
import com.pratyaksh.healthykingdom.utils.PlateletsGroupInfo
import org.osmdroid.util.GeoPoint


sealed class Users(){

    data class Ambulance(
        val driverName: String,
        val vehicleNumber: String,
        val vehicleLocation: org.osmdroid.util.GeoPoint,
        val driverAge: Int,
        val driverGender: String,
        val isVacant: Boolean,
        val isOnline: Boolean,
        val password: String? = null,
        val phone: String? = null,
        val userId: String? = null,
        val lastLocUpdated: Timestamp? = null,
        val mail: String?

    ): Users()

    data class Hospital(
        val name: String,
        val mail: String,
        val phone: String,
        val location: GeoPoint,
        val userId: String,
        val password: String
    ): Users()

    data class PublicUser(
        val userName: String?,
        val userId: String?,
        val providesLocation: Boolean?,
        val phone: String?,
        val location: org.osmdroid.util.GeoPoint? = null,
        val password: String?,
        val mail: String?
    ): Users()

}

fun Users.Ambulance.toAmbulanceDto(): AmbulanceDto {
    return AmbulanceDto(
        driverName, vehicleNumber,
        driverAge, driverName, isVacant,
        isOnline, password, phone,
        vehicleLocation.toFBGeopoint(), userId
    )
}

fun Users.PublicUser.toPublicUserDto(): PublicUserDto {
    return PublicUserDto(
        userName, userId,
        providesLocation, phone,
        location?.toFBGeopoint(), password
    )
}

fun Users.Hospital.toHospitalDto(): HospitalsDto {

    return HospitalsDto(
        name, location.toFBGeopoint(),
        userId, mail, phone,
        password = password
    )
}