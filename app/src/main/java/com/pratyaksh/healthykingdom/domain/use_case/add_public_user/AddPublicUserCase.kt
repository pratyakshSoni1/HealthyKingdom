package com.pratyaksh.healthykingdom.domain.use_case.add_public_user

import com.pratyaksh.healthykingdom.domain.model.Users
import com.pratyaksh.healthykingdom.domain.model.toAmbulanceDto
import com.pratyaksh.healthykingdom.domain.model.toPublicUserDto
import com.pratyaksh.healthykingdom.domain.repository.RemoteAmbulanceFbRepo
import com.pratyaksh.healthykingdom.domain.repository.RemotePublicUserFbRepo
import com.pratyaksh.healthykingdom.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AddPublicUserCase @Inject constructor(
    private val fbUserRepo: RemotePublicUserFbRepo
) {

    operator fun invoke(user: Users.PublicUser): Flow<Resource<Boolean>> = flow{
        emit(Resource.Loading( "Adding ambulance"))
        try{
            if(fbUserRepo.addUser(user.toPublicUserDto()))
                emit(Resource.Success(true))
        }catch(e: Exception){
            e.printStackTrace()
            emit(Resource.Error(e.message))
        }
    }

}