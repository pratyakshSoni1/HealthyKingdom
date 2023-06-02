package com.pratyaksh.healthykingdom.domain.repository

import com.google.android.gms.tasks.Task
import com.pratyaksh.healthykingdom.data.dto.RequestsDto

interface RemoteRequestsRepo {

    suspend fun getAllRequests(): List<RequestsDto>

    suspend fun getSpecificHospitalRequest( hospitalId: String ): RequestsDto?

    suspend fun addRequest( request: RequestsDto ): Task<Void>

    suspend fun updateRequest( request: RequestsDto )

}