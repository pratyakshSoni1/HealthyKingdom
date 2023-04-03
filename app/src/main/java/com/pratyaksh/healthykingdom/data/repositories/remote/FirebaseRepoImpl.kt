package com.pratyaksh.healthykingdom.data.repositories.remote

import com.pratyaksh.healthykingdom.domain.model.Hospital
import com.pratyaksh.healthykingdom.domain.repository.RemoteFirebaseRepo
import org.osmdroid.util.GeoPoint

class FirebaseRepoImpl :RemoteFirebaseRepo {
    override suspend fun getAllHospitals(): List<Hospital> {
        TODO("Not yet implemented")
    }

    override suspend fun getHospitalByLocation(geoPoint: GeoPoint): Hospital {
        TODO("Not yet implemented")
    }

    override suspend fun getHospitalsNearby(geoPoint: GeoPoint): List<Hospital> {
        TODO("Not yet implemented")
    }
}