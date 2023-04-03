package com.pratyaksh.healthykingdom.domain.model

import org.osmdroid.util.GeoPoint

data class Hospital(
    val name: String,
    val location: GeoPoint
)
