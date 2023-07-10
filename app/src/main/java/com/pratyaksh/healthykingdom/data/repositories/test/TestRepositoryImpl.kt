package com.pratyaksh.healthykingdom.data.repositories.test

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.GeoPoint
import com.pratyaksh.healthykingdom.data.dto.HospitalsDto
import com.pratyaksh.healthykingdom.domain.model.Users
import com.pratyaksh.healthykingdom.domain.repository.RemoteHospitalFbRepo
import com.pratyaksh.healthykingdom.utils.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow

class TestRepositoryImpl: RemoteHospitalFbRepo {
    override suspend fun getAllHospitals(): List<HospitalsDto> {
        delay(700L)
        return listOf(
            HospitalsDto(
                name = "First Hospital",
                location = GeoPoint(26.9279849, 81.1967074),
                userId = "firatHospital",
                mail = "firstHosp@gmail.com",
                phone="+91 0000000000"
            ),

            HospitalsDto(
                name = "Third Hospital",
                location = GeoPoint(26.9329849, 81.1967074),
                userId = "thirdHospital",
                mail = "firstHosp@gmail.com",
                phone="+91 0000000000"
            ),

            HospitalsDto(
                name = "Second Hospital",
                location = GeoPoint(26.9279849, 81.1907074),
                userId = "secondHospital",
                mail = "firstHosp@gmail.com",
                phone="+91 0000000000"
            )
        )
    }

    override suspend fun getHospitalById(id: String): HospitalsDto? {
        return HospitalsDto(
            name = "Second Hospital",
            location = GeoPoint(26.9279849, 81.1907074),
            userId = "secondHospital",
            mail = "firstHosp@gmail.com",
            phone="+91 0000000000"
        )
    }

    override suspend fun getHospitalByPhone(phone: String): HospitalsDto? {
        TODO("Not yet implemented")
    }

    override suspend fun addHospital(hospital: HospitalsDto): Task<Void> {
        TODO("Not yet implemented")
    }

    override suspend fun getHospitalByLocation(geoPoint: GeoPoint): Users.Hospital {
        TODO("Not yet implemented")
    }

    override suspend fun getHospitalsNearby(geoPoint: GeoPoint): List<Users.Hospital> {
        TODO("Not yet implemented")
    }
}