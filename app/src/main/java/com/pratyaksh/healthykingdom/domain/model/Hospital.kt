package com.pratyaksh.healthykingdom.domain.model

import com.google.firebase.firestore.GeoPoint


data class Hospital(
    val name: String,
    val location: GeoPoint
)
