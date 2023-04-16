package com.pratyaksh.healthykingdom.data.dto

import com.google.firebase.firestore.GeoPoint
import com.pratyaksh.healthykingdom.domain.model.Hospital
import com.pratyaksh.healthykingdom.utils.BloodGroupInterface
import com.pratyaksh.healthykingdom.utils.BloodGroupsInfo
import com.pratyaksh.healthykingdom.utils.Plasma
import com.pratyaksh.healthykingdom.utils.PlasmaGroupInfo
import com.pratyaksh.healthykingdom.utils.PlateletsGroupInfo

data class HospitalsDto (
    val name: String = "",
    val location: GeoPoint? =null,
    val id: String = "",
    val mail: String = "",
    val phone: String = "",
    val availBloods: List<String>? = null,
    val availPlasma: List<String>? = null,
    val availPlatelets: List<String>? = null,
)

fun HospitalsDto.toHospital(): Hospital {

    val bloods = availBloods?.map {
        when(it){
            "A+" -> BloodGroupsInfo.A_POSITIVE
            "A-" -> BloodGroupsInfo.A_NEGATIVE
            "B+" -> BloodGroupsInfo.B_POSITIVE
            "B-" -> BloodGroupsInfo.B_NEGATIVE
            "AB+" -> BloodGroupsInfo.AB_POSITIVE
            "AB-" -> BloodGroupsInfo.AB_NEGATIVE
            "O+" -> BloodGroupsInfo.O_POSITIVE
            "O-" -> BloodGroupsInfo.O_NEGATIVE
            else -> { BloodGroupsInfo.ERROR_TYPE }
        }
    }

    val plasma = availPlasma?.map {
        when(it){
            "A" -> PlasmaGroupInfo.PLASMA_A
            "B" -> PlasmaGroupInfo.PLASMA_B
            "AB" -> PlasmaGroupInfo.PLASMA_AB
            "O" -> PlasmaGroupInfo.PLASMA_O
            else -> { PlasmaGroupInfo.ERROR_TYPE }
        }
    }

    val platelets = availPlatelets?.map {
        when(it){
            "A+" -> PlateletsGroupInfo.A_POSITIVE
            "A-" -> PlateletsGroupInfo.A_NEGATIVE
            "B+" -> PlateletsGroupInfo.B_POSITIVE
            "B-" -> PlateletsGroupInfo.B_NEGATIVE
            "AB+" -> PlateletsGroupInfo.AB_POSITIVE
            "AB-" -> PlateletsGroupInfo.AB_NEGATIVE
            "O+" -> PlateletsGroupInfo.O_POSITIVE
            "O-" -> PlateletsGroupInfo.O_NEGATIVE
            else -> { PlateletsGroupInfo.ERROR_TYPE }
        }
    }

    return Hospital(
        name = name,
        location = location!!,
        id = id,
        mail = mail,
        availBloods = bloods!!,
        availPlasma = plasma!!,
        availPlatelets = platelets!!,
        phone = phone
    )
}

fun GeoPoint.toMapsGeopoint(): org.osmdroid.util.GeoPoint{
    return org.osmdroid.util.GeoPoint(
        latitude,
        longitude
    )
}