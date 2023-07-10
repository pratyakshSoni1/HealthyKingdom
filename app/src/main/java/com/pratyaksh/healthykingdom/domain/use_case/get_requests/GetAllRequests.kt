package com.pratyaksh.healthykingdom.domain.use_case.get_requests

import com.pratyaksh.healthykingdom.data.dto.toRequests
import com.pratyaksh.healthykingdom.domain.model.Requests
import com.pratyaksh.healthykingdom.domain.repository.RemoteRequestsRepo
import com.pratyaksh.healthykingdom.utils.Resource
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAllRequests @Inject constructor(
    private val reqRepo: RemoteRequestsRepo
){

    operator fun invoke() = flow<Resource<List<Requests>>> {
        emit( Resource.Loading("Loading Requests"))
        try{
            emit( Resource.Success(reqRepo.getAllRequests().map { it.toRequests() }) )
        }catch(e: Exception ){
            emit(Resource.Error("Error loading requests"))
        }
    }

}