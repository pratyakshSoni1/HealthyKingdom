package com.pratyaksh.healthykingdom.data.repositories.remote

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.pratyaksh.healthykingdom.data.dto.RequestsDto
import com.pratyaksh.healthykingdom.domain.repository.RemoteRequestsRepo
import com.pratyaksh.healthykingdom.utils.Constants
import kotlinx.coroutines.tasks.await

class RequestsRepoImpl(
    val firestore: FirebaseFirestore
): RemoteRequestsRepo {

    override suspend fun getAllRequests(): List<RequestsDto> {
        val resp = firestore.collection(Constants.Collections.REQUESTS)
            .get().await()
            .toObjects(RequestsDto::class.java)
        return resp
    }

    override suspend fun getSpecificHospitalRequest(hospitalId: String): RequestsDto? {
        val resp = firestore.collection(Constants.Collections.REQUESTS)
            .document(hospitalId).get().await()
            .toObject(RequestsDto::class.java)
        return resp
    }

    override suspend fun addRequest(request: RequestsDto): Task<Void> {
        return firestore.collection(Constants.Collections.REQUESTS)
            .document(request.hospitalId!!)
            .set(request)
    }

    override suspend fun updateRequest(request: RequestsDto) {
        firestore.collection(Constants.Collections.REQUESTS)
            .document(request.hospitalId!!)
            .update(
                mapOf(
                    Constants.RequestsDocField.blood to request.bloods,
                    Constants.RequestsDocField.plasma to request.plasma,
                    Constants.RequestsDocField.platelets to request.platelets
                )
            )
    }
}