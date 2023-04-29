package com.pratyaksh.healthykingdom.domain.use_case.getHospital

import android.util.Log
import com.pratyaksh.healthykingdom.data.dto.toHospital
import com.pratyaksh.healthykingdom.domain.model.Hospital
import com.pratyaksh.healthykingdom.domain.repository.RemoteHospitalFbRepo
import com.pratyaksh.healthykingdom.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAllHospitalsUseCase @Inject constructor(private val firebaseRepo: RemoteHospitalFbRepo) {

    operator fun invoke(): Flow<Resource<List<Hospital>>> = flow{
        try{
            val results = firebaseRepo.getAllHospitals().map {
                it.toHospital()
            }
            Log.d("getAllHospital_Logs", "Hospital retrieved: ${results.size}")
            emit(Resource.Success(results))
        }catch (e: Exception){
            e.printStackTrace()
            Log.d("GetAllHospital_Logs", "Error getting hospitals: ${e.message}")
            emit(Resource.Error(e.message))
        }
    }


}