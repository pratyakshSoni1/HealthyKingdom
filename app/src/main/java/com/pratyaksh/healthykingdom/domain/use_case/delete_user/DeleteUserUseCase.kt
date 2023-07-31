package com.pratyaksh.healthykingdom.domain.use_case.delete_user

import com.google.firebase.firestore.FirebaseFirestore
import com.pratyaksh.healthykingdom.utils.Constants
import com.pratyaksh.healthykingdom.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DeleteUserUseCase @Inject constructor(
    private val fireStore: FirebaseFirestore
) {

    suspend fun deleteHospital(hospitalId: String): Flow<Resource<Unit>> = flow {
        try{
            emit(Resource.Loading("Deleting data..."))
            fireStore.collection(Constants.Collections.HOSPITALS_COLLECTION)
                .document( hospitalId )
                .delete()

            fireStore.collection(Constants.Collections.REQUESTS)
                .document(hospitalId)
                .delete()

            fireStore.collection(Constants.Collections.LIFE_FLUIDS)
                .document(hospitalId)
                .delete()
            emit(Resource.Success(Unit))
        }catch(e: Exception){
            e.printStackTrace()
            emit(Resource.Error("Error Deleting Account"))
        }

    }

    suspend fun deletePublicUser(userId:String): Flow<Resource<Unit>> = flow {
        try{
            emit(Resource.Loading("Deleting data..."))
            fireStore.collection(Constants.Collections.PUBLIC_USERS)
                .document( userId )
                .delete()
            emit(Resource.Success(Unit))
        }catch(e: Exception){
            e.printStackTrace()
            emit(Resource.Error("Error Deleting Account"))
        }
    }

    suspend fun deleteAmbulance(userId:String): Flow<Resource<Unit>> = flow {
        try{
            emit(Resource.Loading("Deleting data..."))
            fireStore.collection(Constants.Collections.AMBLANCE_DRIVERS)
                .document( userId )
                .delete()
            emit(Resource.Success(Unit))
        }catch(e: Exception){
            e.printStackTrace()
            emit(Resource.Error("Error Deleting Account"))
        }
    }


}