package com.pratyaksh.healthykingdom.domain.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.GeoPoint
import com.pratyaksh.healthykingdom.data.dto.HospitalsDto
import com.pratyaksh.healthykingdom.domain.model.Users
import com.pratyaksh.healthykingdom.utils.Resource
import kotlinx.coroutines.flow.Flow

interface RemoteHospitalFbRepo {

    suspend fun getAllHospitals(): List<HospitalsDto>
    suspend fun getHospitalByLocation(geoPoint: GeoPoint): Users.Hospital
    suspend fun getHospitalsNearby(geoPoint: GeoPoint): List<Users.Hospital>
    suspend fun getHospitalById(id: String): HospitalsDto?
    suspend fun getHospitalByPhone(phone: String): HospitalsDto?
    suspend fun addHospital(hospital: HospitalsDto): Task<Void>
    suspend fun updateHospital(hospital: HospitalsDto)
    suspend fun deleteHospital(userId: String)

}