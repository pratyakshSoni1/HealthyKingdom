package com.pratyaksh.healthykingdom.utils

import android.util.Log
import com.google.firebase.firestore.auth.User
import com.pratyaksh.healthykingdom.R
import com.pratyaksh.healthykingdom.domain.model.Users

sealed class AccountTypes(
    val type: String,
    val img: Int
) {

    object AMBULANCE : AccountTypes(type = "Ambulance", img = R.drawable.ambulance)
    object HOSPITAL : AccountTypes(type = "Hospital", img = R.drawable.hospital)
    object PUBLIC_USER : AccountTypes(type = "Public User", img = R.drawable.ic_person)

}

fun identifyUserTypeFromId(userId: String): AccountTypes? {
    Log.d("IDENTIFY_USER", "Got Id: $userId")
    if( userId.split("-")[0] == AccountTypes.HOSPITAL.type )
        return AccountTypes.HOSPITAL
    else if( userId.split("-")[0] == AccountTypes.AMBULANCE.type )
        return AccountTypes.AMBULANCE
    else if( userId.split("-")[0] == AccountTypes.PUBLIC_USER.type )
        return AccountTypes.PUBLIC_USER
    else return null
}
