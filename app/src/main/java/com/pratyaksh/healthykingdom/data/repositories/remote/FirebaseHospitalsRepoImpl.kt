package com.pratyaksh.healthykingdom.data.repositories.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.pratyaksh.healthykingdom.domain.model.Hospital
import com.pratyaksh.healthykingdom.domain.repository.RemoteFirebaseRepo
import com.google.firebase.firestore.GeoPoint
import com.pratyaksh.healthykingdom.data.dto.HospitalsDto
import com.pratyaksh.healthykingdom.utils.Constants.Collections
import kotlinx.coroutines.tasks.await

class FirebaseHospitalsRepoImpl(private val firebase: FirebaseFirestore): RemoteFirebaseRepo {
    override suspend fun getAllHospitals(): List<HospitalsDto> {

        val results = firebase.collection(Collections.HOSPITALS_COLLECTION)
                    .get().await()
                    .toObjects(HospitalsDto::class.java)

        return results

    }

    override suspend fun getHospitalById(id: String): HospitalsDto? {
        getAllHospitals()
            .forEach {
                if(it.id == id){
                    return it
                }
            }
        return null
    }

    override suspend fun getHospitalByLocation(geoPoint: GeoPoint): Hospital {
        TODO("Not yet implemented")
    }

    override suspend fun getHospitalsNearby(geoPoint: GeoPoint): List<Hospital> {
        TODO("Not yet implemented")
    }
}