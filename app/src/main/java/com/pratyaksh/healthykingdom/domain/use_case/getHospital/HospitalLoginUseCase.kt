package com.pratyaksh.healthykingdom.domain.use_case.getHospital

import com.pratyaksh.healthykingdom.data.dto.toHospital
import com.pratyaksh.healthykingdom.domain.repository.RemoteHospitalFbRepo
import com.pratyaksh.healthykingdom.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HospitalLoginUseCase @Inject constructor(
    private val fbRepo: RemoteHospitalFbRepo
) {

    suspend operator fun invoke(phone: String, password: String): Flow<Resource<String?>> = flow{
        emit(Resource.Loading("Authenticating..."))
        try{
            val reqUser = fbRepo.getHospitalByPhone(phone)
            if(reqUser != null){
                if(reqUser.password == password)
                    emit(Resource.Success(reqUser.id))
                else
                    emit(Resource.Error("Invalid password"))
            }else{
                emit(Resource.Error("User not found."))
            }
        }catch(e: Exception){
            e.printStackTrace()
            emit(Resource.Error("Unexpected error !\nPlease Try again later"))
        }

    }

}