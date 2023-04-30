package com.pratyaksh.healthykingdom.domain.use_case.get_public_user

import com.pratyaksh.healthykingdom.data.dto.toPublicUser
import com.pratyaksh.healthykingdom.domain.model.PublicUser
import com.pratyaksh.healthykingdom.domain.repository.RemotePublicUserFbRepo
import com.pratyaksh.healthykingdom.utils.Resource
import kotlinx.coroutines.flow.flow
import java.util.concurrent.Flow
import javax.inject.Inject


class GetPublicUserById @Inject constructor(
    private val fbRepo: RemotePublicUserFbRepo
) {

    operator fun invoke(userId: String) = flow<Resource<PublicUser?>>{

        emit(Resource.Loading("Finding user"))
        try{
            val user = fbRepo.getUserWithId(userId)?.toPublicUser()
            if(user == null ){
                emit(Resource.Success(user))
            }else{
                emit(Resource.Error("User Not found"))
            }
        }catch( e: Exception ){
            e.printStackTrace()
            emit(Resource.Error("Unexpected error occured ! Try again later"))
        }
    }

}