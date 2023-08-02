package com.pratyaksh.healthykingdom.domain.use_case.update_password

import com.pratyaksh.healthykingdom.domain.repository.RemoteAmbulanceFbRepo
import com.pratyaksh.healthykingdom.domain.repository.RemoteHospitalFbRepo
import com.pratyaksh.healthykingdom.domain.repository.RemotePublicUserFbRepo
import com.pratyaksh.healthykingdom.utils.Resource
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdatePasswordUseCase @Inject constructor(
    val hospitalFbRepo: RemoteHospitalFbRepo,
    val publicUserFbRepo: RemotePublicUserFbRepo,
    val ambulanceFbRepo: RemoteAmbulanceFbRepo,
) {

    fun updateAmbulancePassword(userId: String, newPass: String) =
        flow<Resource<Unit>> {
            try {
                ambulanceFbRepo.updatePassword(userId, newPass)
                emit(Resource.Success(Unit))
            } catch (e: Exception) {
                emit(Resource.Error("Error updating password"))
                e.printStackTrace()
            }
        }

    fun updateHospitalPassword(userId: String, newPass: String) =
        flow<Resource<Unit>> {
            try {
                hospitalFbRepo.updatePassword(userId, newPass)
                emit(Resource.Success(Unit))
            } catch (e: Exception) {
                emit(Resource.Error("Error updating password"))
                e.printStackTrace()
            }
        }

    fun updatePublicUserPassword(userId: String, newPass: String) =
        flow<Resource<Unit>> {
            try {
                publicUserFbRepo.updatePassword(userId, newPass)
                emit(Resource.Success(Unit))

            } catch (e: Exception) {
                emit(Resource.Error("Error updating password"))
                e.printStackTrace()
            }
        }

}