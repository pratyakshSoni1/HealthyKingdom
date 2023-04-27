package com.pratyaksh.healthykingdom.domain.repository

import com.pratyaksh.healthykingdom.domain.model.Hospital
import com.google.firebase.firestore.GeoPoint
import com.pratyaksh.healthykingdom.data.dto.HospitalsDto
import com.pratyaksh.healthykingdom.utils.Resource
import kotlinx.coroutines.flow.Flow

interface RemoteFirebaseRepo {

    suspend fun getAllHospitals(): List<HospitalsDto>
    suspend fun getHospitalByLocation(geoPoint: GeoPoint): Hospital
    suspend fun getHospitalsNearby(geoPoint: GeoPoint): List<Hospital>
    suspend fun getHospitalById(id: String): HospitalsDto?
    suspend fun getHospitalByPhone(phone: String, password: String): HospitalsDto?
    suspend fun addHospital(hospital: HospitalsDto): Flow<Resource<Boolean>>

}