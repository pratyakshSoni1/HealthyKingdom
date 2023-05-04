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

sealed class Users {

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
        val id: String,
        val availBloods: List<BloodGroupsInfo>,
        val availPlasma: List<PlasmaGroupInfo>,
        val availPlatelets: List<PlateletsGroupInfo>,
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

    val bloodStock: List<String> = availBloods.map {
        when(it){
            BloodGroupsInfo.A_POSITIVE -> "A+"
            BloodGroupsInfo.A_NEGATIVE -> "A-"
            BloodGroupsInfo.AB_POSITIVE -> "AB+"
            BloodGroupsInfo.AB_NEGATIVE -> "AB-"
            BloodGroupsInfo.B_POSITIVE -> "B+"
            BloodGroupsInfo.B_NEGATIVE -> "B-"
            BloodGroupsInfo.O_POSITIVE -> "O+"
            BloodGroupsInfo.O_NEGATIVE -> "O-"
        }
    }
        val plateletsStock: List<String> = availPlatelets.map {
        when(it){
            PlateletsGroupInfo.A_POSITIVE -> "A+"
            PlateletsGroupInfo.A_NEGATIVE -> "A-"
            PlateletsGroupInfo.AB_POSITIVE -> "AB+"
            PlateletsGroupInfo.AB_NEGATIVE -> "AB-"
            PlateletsGroupInfo.B_POSITIVE -> "B+"
            PlateletsGroupInfo.B_NEGATIVE -> "B-"
            PlateletsGroupInfo.O_POSITIVE -> "O+"
            PlateletsGroupInfo.O_NEGATIVE -> "O-"
            else -> ""
        }
    }
        val plasmaStock: List<String> = availPlasma.map {
        when(it){
            PlasmaGroupInfo.Plasma_A -> "A"
            PlasmaGroupInfo.Plasma_AB -> "AB"
            PlasmaGroupInfo.Plasma_B -> "B"
            PlasmaGroupInfo.Plasma_O -> "O"
        }
    }

    return HospitalsDto(
        name, location.toFBGeopoint(),
        id, mail, phone,
        availBloods = bloodStock,
        plasmaStock, plateletsStock,
        password
    )
}