package com.pratyaksh.healthykingdom.data.repositories.remote

import com.pratyaksh.healthykingdom.domain.model.Hospital
import com.pratyaksh.healthykingdom.domain.repository.RemoteHospitalFbRepo
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.GeoPoint
import com.pratyaksh.healthykingdom.data.dto.HospitalsDto
import com.pratyaksh.healthykingdom.utils.Constants
import com.pratyaksh.healthykingdom.utils.Constants.Collections
import com.pratyaksh.healthykingdom.utils.Resource
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class HospitalFbHospitalsRepoImpl(private val fireStore: FirebaseFirestore): RemoteHospitalFbRepo {
    override suspend fun getAllHospitals(): List<HospitalsDto> {

        val results = fireStore.collection(Collections.HOSPITALS_COLLECTION)
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

    override suspend fun getHospitalByPhone(phone: String): HospitalsDto? {
        return getAllHospitals().find {
                it.phone == phone
            }
    }

    override suspend fun addHospital(hospital: HospitalsDto) = flow{

        try{
            val task = fireStore.collection(Constants.Collections.HOSPITALS_COLLECTION)
                .document().set(hospital)
            task.await()
            emit(Resource.Success(true))
        }catch(e: FirebaseFirestoreException){
            e.printStackTrace()
            emit(Resource.Error<Boolean>(e.message))
        }

    }
    override suspend fun getHospitalByLocation(geoPoint: GeoPoint): Hospital {
        TODO("Not yet implemented")
    }

    override suspend fun getHospitalsNearby(geoPoint: GeoPoint): List<Hospital> {
        TODO("Not yet implemented")
    }


}