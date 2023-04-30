package com.pratyaksh.healthykingdom.domain.use_case.get_public_user

import com.pratyaksh.healthykingdom.domain.repository.RemotePublicUserFbRepo
import com.pratyaksh.healthykingdom.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PublicUserLoginUseCase @Inject constructor(
    private val userFbRepo: RemotePublicUserFbRepo
) {

    operator fun invoke(phone: String, password: String): Flow<Resource<String?>> = flow{

        emit(Resource.Loading("Authenticating..."))
        try{
            val reqUser = userFbRepo.getUserWithPhone(phone)
            if(reqUser != null){
                if(reqUser.password == password)
                    emit(Resource.Success(reqUser.userId))
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