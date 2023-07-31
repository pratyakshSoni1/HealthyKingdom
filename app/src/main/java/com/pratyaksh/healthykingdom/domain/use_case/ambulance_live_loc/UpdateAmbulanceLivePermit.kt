package com.pratyaksh.healthykingdom.domain.use_case.ambulance_live_loc

import com.pratyaksh.healthykingdom.domain.repository.RemoteAmbulanceFbRepo
import com.pratyaksh.healthykingdom.utils.Resource
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateAmbulanceLivePermit @Inject constructor(
    val fbRepo: RemoteAmbulanceFbRepo
){

    operator fun invoke(userId: String, permit: Boolean) = flow<Resource<Unit>>{

        try{
            emit(Resource.Loading("Fetching live ambulances"))
            fbRepo.updateAmbulanceLivePermit(userId, permit)
            emit(Resource.Success(Unit))
        }catch(e: Exception){
            e.printStackTrace()
            emit(Resource.Error("Unable to update ambulance permit"))
        }

    }

}