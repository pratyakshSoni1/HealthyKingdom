package com.pratyaksh.healthykingdom.domain.model

import com.google.firebase.firestore.GeoPoint
import com.pratyaksh.healthykingdom.utils.BloodGroupInterface
import com.pratyaksh.healthykingdom.utils.Plasma
import com.pratyaksh.healthykingdom.utils.Platelets
import java.io.Serializable


data class Hospital(
    val name: String,
    val mail: String,
    val phone: String,
    val location: GeoPoint,
    val id: String,
    val availBloods: List<BloodGroupInterface>,
    val availPlasma: List<Plasma>,
    val availPlatelets: List<Platelets>,
): Serializable{

}
