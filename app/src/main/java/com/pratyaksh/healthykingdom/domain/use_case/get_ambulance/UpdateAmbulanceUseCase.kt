package com.pratyaksh.healthykingdom.domain.use_case.get_ambulance

import com.pratyaksh.healthykingdom.domain.model.Users
import com.pratyaksh.healthykingdom.domain.model.toAmbulanceDto
import com.pratyaksh.healthykingdom.domain.repository.RemoteAmbulanceFbRepo
import com.pratyaksh.healthykingdom.utils.Resource
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateAmbulanceUseCase @Inject constructor(
    private val fbRepo: RemoteAmbulanceFbRepo
) {

    operator fun invoke(ambulance: Users.Ambulance) = flow<Resource<Unit>>{
        try{
            fbRepo.updateAmbulance(ambulance.toAmbulanceDto())
            emit(Resource.Success(Unit))
        }catch(e: Exception){
            emit(Resource.Error("Can't update data"))
            e.printStackTrace()
        }

    }

}