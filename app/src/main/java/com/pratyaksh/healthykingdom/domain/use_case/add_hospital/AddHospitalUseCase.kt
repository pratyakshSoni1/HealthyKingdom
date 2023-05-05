package com.pratyaksh.healthykingdom.domain.use_case.add_hospital

import com.pratyaksh.healthykingdom.data.dto.HospitalsDto
import com.pratyaksh.healthykingdom.domain.model.Users
import com.pratyaksh.healthykingdom.domain.model.toHospitalDto
import com.pratyaksh.healthykingdom.domain.repository.RemoteHospitalFbRepo
import com.pratyaksh.healthykingdom.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AddHospitalUseCase @Inject constructor(
    private val firebaseHospitalRepo: RemoteHospitalFbRepo
) {

    suspend operator fun invoke(
        hospital: Users.Hospital
    ): Flow<Resource<Boolean>> = flow{
        try {
            val resp = firebaseHospitalRepo.addHospital(hospital.toHospitalDto())
            if(resp)
                emit(Resource.Success(resp))
            else
                emit(Resource.Error("User can't be added, try again later"))
        }catch(e: Exception){
            emit(Resource.Error("Unexpected error occured"))
        }
    }

}