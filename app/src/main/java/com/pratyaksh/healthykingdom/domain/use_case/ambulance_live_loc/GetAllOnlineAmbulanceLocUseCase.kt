package com.pratyaksh.healthykingdom.domain.use_case.ambulance_live_loc

import com.pratyaksh.healthykingdom.data.dto.toAmbulance
import com.pratyaksh.healthykingdom.domain.model.Users
import com.pratyaksh.healthykingdom.domain.repository.RemoteAmbulanceFbRepo
import com.pratyaksh.healthykingdom.utils.Resource
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAllOnlineAmbulanceLocUseCase @Inject constructor(
    val fbRepo: RemoteAmbulanceFbRepo
){

    operator fun invoke() = flow<Resource<List<Users.Ambulance>>>{

        try{
            emit(Resource.Loading("Fetching live ambulances"))
            val onlineAmbulances = fbRepo.getAllAmbulances().filter { it.online }
            emit(Resource.Success(onlineAmbulances.map { it.toAmbulance() }))
        }catch(e: Exception){
            e.printStackTrace()
            emit(Resource.Error("Unable to fetch ambulances"))
        }

    }

}