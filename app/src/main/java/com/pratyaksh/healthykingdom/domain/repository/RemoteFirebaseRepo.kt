package com.pratyaksh.healthykingdom.domain.repository

import com.pratyaksh.healthykingdom.domain.model.Hospital
import com.google.firebase.firestore.GeoPoint

interface RemoteFirebaseRepo {

    suspend fun getAllHospitals(): List<Hospital>
    suspend fun getHospitalByLocation(geoPoint: GeoPoint): Hospital
    suspend fun getHospitalsNearby(geoPoint: GeoPoint): List<Hospital>

}