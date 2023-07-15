package com.pratyaksh.healthykingdom.domain.use_case.update_requests

import com.pratyaksh.healthykingdom.domain.model.Requests
import com.pratyaksh.healthykingdom.domain.model.toRequestsDto
import com.pratyaksh.healthykingdom.domain.repository.RemoteRequestsRepo
import com.pratyaksh.healthykingdom.utils.Resource
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateRequestsUseCase @Inject constructor(
    val fbRepo: RemoteRequestsRepo
){
    operator fun invoke( request: Requests )= flow<Resource<Unit>>{
        try{
            fbRepo.updateRequest( request.toRequestsDto() )
            emit(Resource.Success(Unit))
        }catch(e: Exception){
            emit(Resource.Error("Unable to update requests !"))
            e.printStackTrace()
        }
    }

}