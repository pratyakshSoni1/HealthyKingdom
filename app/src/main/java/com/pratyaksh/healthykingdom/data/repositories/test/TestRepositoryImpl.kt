package com.pratyaksh.healthykingdom.data.repositories.test

import com.google.firebase.firestore.GeoPoint
import com.pratyaksh.healthykingdom.data.dto.HospitalsDto
import com.pratyaksh.healthykingdom.domain.model.Hospital
import com.pratyaksh.healthykingdom.domain.repository.RemoteFirebaseRepo
import kotlinx.coroutines.delay

class TestRepositoryImpl: RemoteFirebaseRepo {
    override suspend fun getAllHospitals(): List<HospitalsDto> {
        delay(700L)
        return listOf(
            HospitalsDto(
                name = "First Hospital",
                location = GeoPoint(26.9279849, 81.1967074),
                id = "firatHospital"
            ),

            HospitalsDto(
                name = "Third Hospital",
                location = GeoPoint(26.9329849, 81.1967074),
                id = "thirdHospital"
            ),

            HospitalsDto(
                name = "Second Hospital",
                location = GeoPoint(26.9279849, 81.1907074),
                id = "secondHospital"
            )
        )
    }

    override suspend fun getHospitalByLocation(geoPoint: GeoPoint): Hospital {
        TODO("Not yet implemented")
    }

    override suspend fun getHospitalsNearby(geoPoint: GeoPoint): List<Hospital> {
        TODO("Not yet implemented")
    }
}