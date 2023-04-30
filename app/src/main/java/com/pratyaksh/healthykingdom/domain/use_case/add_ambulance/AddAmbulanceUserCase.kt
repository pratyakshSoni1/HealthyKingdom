package com.pratyaksh.healthykingdom.domain.use_case.add_ambulance

import com.pratyaksh.healthykingdom.domain.model.Ambulance
import com.pratyaksh.healthykingdom.domain.model.toAmbulanceDto
import com.pratyaksh.healthykingdom.domain.repository.RemoteAmbulanceFbRepo
import com.pratyaksh.healthykingdom.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AddAmbulanceUserCase @Inject constructor(
    private val fbAmbulanceRepo: RemoteAmbulanceFbRepo
) {

    operator fun invoke(ambulance: Ambulance): Flow<Resource<Boolean>> = flow{
        emit(Resource.Loading( "Adding ambulance"))
        try{
            if(fbAmbulanceRepo.addAmbulance(ambulance.toAmbulanceDto()))
                emit(Resource.Success(true))
        }catch(e: Exception){
            e.printStackTrace()
            emit(Resource.Error(e.message))
        }
    }

}