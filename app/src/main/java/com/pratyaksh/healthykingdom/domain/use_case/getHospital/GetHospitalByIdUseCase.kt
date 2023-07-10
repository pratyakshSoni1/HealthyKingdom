package com.pratyaksh.healthykingdom.domain.use_case.getHospital

import com.pratyaksh.healthykingdom.data.dto.toHospital
import com.pratyaksh.healthykingdom.domain.model.Users
import com.pratyaksh.healthykingdom.domain.repository.RemoteHospitalFbRepo
import com.pratyaksh.healthykingdom.utils.Resource
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetHospitalByIdUseCase @Inject constructor(
    private val fbRepo: RemoteHospitalFbRepo
) {
    operator fun invoke(id: String) = flow<Resource<Users.Hospital?>>{

        emit(Resource.Loading("Fetching hospital details"))
        try{
            emit(Resource.Success(fbRepo.getHospitalById(id)?.toHospital()))
        }catch(e: Exception){
            e.printStackTrace()
            emit(Resource.Error("Error fetching hospital details !"))
        }
    }

}