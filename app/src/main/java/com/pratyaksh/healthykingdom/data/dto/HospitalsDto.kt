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
    val id: String = "",
    val mail: String = "",
    val phone: String = "",
    val availBloods: List<String>? = null,
    val availPlasma: List<String>? = null,
    val availPlatelets: List<String>? = null,
    val password: String? = null
)

fun HospitalsDto.toHospital(): Users.Hospital {

    val bloodStock = mutableListOf<BloodGroupsInfo>()
    val plasmaStock = mutableListOf<PlasmaGroupInfo>()
    val plateletsStock = mutableListOf<PlateletsGroupInfo>()

    availBloods?.forEach {
        when(it){
            "A+" -> bloodStock.add(BloodGroupsInfo.A_POSITIVE)
            "A-" -> bloodStock.add(BloodGroupsInfo.A_NEGATIVE)
            "B+" -> bloodStock.add(BloodGroupsInfo.B_POSITIVE)
            "B-" -> bloodStock.add(BloodGroupsInfo.B_NEGATIVE)
            "AB+" -> bloodStock.add(BloodGroupsInfo.AB_POSITIVE)
            "AB-" -> bloodStock.add(BloodGroupsInfo.AB_NEGATIVE)
            "O+" -> bloodStock.add(BloodGroupsInfo.O_POSITIVE)
            "O-" -> bloodStock.add(BloodGroupsInfo.O_NEGATIVE)

        }
    }

    availPlasma?.forEach {
        when(it){
            "A" -> plasmaStock.add(PlasmaGroupInfo.Plasma_A)
            "B" -> plasmaStock.add(PlasmaGroupInfo.Plasma_B)
            "AB" -> plasmaStock.add(PlasmaGroupInfo.Plasma_AB)
            "O" -> plasmaStock.add(PlasmaGroupInfo.Plasma_O)

        }
    }

    availPlatelets?.forEach {
        when(it){
            "A+" -> plateletsStock.add(PlateletsGroupInfo.A_POSITIVE)
            "A-" -> plateletsStock.add(PlateletsGroupInfo.A_NEGATIVE)
            "B+" -> plateletsStock.add(PlateletsGroupInfo.B_POSITIVE)
            "B-" -> plateletsStock.add(PlateletsGroupInfo.B_NEGATIVE)
            "AB+" -> plateletsStock.add(PlateletsGroupInfo.AB_POSITIVE)
            "AB-" -> plateletsStock.add(PlateletsGroupInfo.AB_NEGATIVE)
            "O+" -> plateletsStock.add(PlateletsGroupInfo.O_POSITIVE)
            "O-" -> plateletsStock.add(PlateletsGroupInfo.O_NEGATIVE)

        }
    }

    return Users.Hospital(
        name = name,
        location = location!!.toMapsGeopoint(),
        id = id,
        mail = mail,
        availBloods = bloodStock,
        availPlasma = plasmaStock,
        availPlatelets = plateletsStock,
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