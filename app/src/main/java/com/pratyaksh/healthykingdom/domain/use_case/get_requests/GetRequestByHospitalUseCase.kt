package com.pratyaksh.healthykingdom.domain.use_case.get_requests

import com.pratyaksh.healthykingdom.data.dto.toRequests
import com.pratyaksh.healthykingdom.domain.model.Requests
import com.pratyaksh.healthykingdom.domain.repository.RemoteRequestsRepo
import com.pratyaksh.healthykingdom.utils.Resource
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetRequestByHospitalUseCase @Inject constructor(
    val reqRepo: RemoteRequestsRepo
) {

    operator fun invoke(hospitalId: String)= flow<Resource<Requests>>{

        emit( Resource.Loading("Loading...") )
        try {
            emit(Resource.Success(reqRepo.getSpecificHospitalRequest(hospitalId)!!.toRequests()))
        }catch (e: Exception){
            e.printStackTrace()
            emit( Resource.Error("Error getting request !") )
        }
    }

}