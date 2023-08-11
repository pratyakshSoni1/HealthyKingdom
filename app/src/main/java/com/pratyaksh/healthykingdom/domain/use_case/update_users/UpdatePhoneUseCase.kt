package com.pratyaksh.healthykingdom.domain.use_case.update_users

import com.pratyaksh.healthykingdom.data.dto.SettingsDto
import com.pratyaksh.healthykingdom.data.dto.toAmbulance
import com.pratyaksh.healthykingdom.domain.repository.LocalUserSettingRepo
import com.pratyaksh.healthykingdom.domain.repository.RemoteAmbulanceFbRepo
import com.pratyaksh.healthykingdom.domain.repository.RemoteHospitalFbRepo
import com.pratyaksh.healthykingdom.domain.repository.RemoteLifeFluidsFbRepo
import com.pratyaksh.healthykingdom.domain.repository.RemotePublicUserFbRepo
import com.pratyaksh.healthykingdom.domain.repository.RemoteRequestsRepo
import com.pratyaksh.healthykingdom.utils.Resource
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdatePhoneUseCase @Inject constructor(
    val hospitalFbRepo: RemoteHospitalFbRepo,
    val ambulanceFbRepo: RemoteAmbulanceFbRepo,
    val publicUserFbRepo: RemotePublicUserFbRepo,
    val requestFbRepo: RemoteRequestsRepo,
    val lifeFluidsRepo: RemoteLifeFluidsFbRepo,
    val localSettingsRepo: LocalUserSettingRepo
){
    fun updateAmbulancePhone(userId: String, newPhone: String) = flow<Resource<Unit>>{
        emit(Resource.Loading("Updating..."))
        try{
            var doc = ambulanceFbRepo.getAmbulanceById(userId)
            val newUserId: String = "${userId.split("-")[0]}-${newPhone.substring(8..11)}"
            doc = doc?.copy(
                phone = newPhone,
                userId = newUserId
            )

            if(doc != null) {
                ambulanceFbRepo.deleteAmbulance(userId)
                ambulanceFbRepo.addAmbulance(doc)
                localSettingsRepo.updateSetting(
                    SettingsDto(
                        newUserId,
                        false,
                        doc.online
                    )
                )
                emit(Resource.Success(Unit))
            }else{
                emit(Resource.Error("No such user file found"))
                throw NoSuchFieldException()
            }

        }catch(e: Exception){
            emit(Resource.Error("An error occur while updating phone"))
            e.printStackTrace()
        }

    }

    fun updatePublicUserPhone(userId: String, newPhone: String) = flow<Resource<Unit>>{
        emit(Resource.Loading("Updating..."))
        try{
            var doc = publicUserFbRepo.getUserWithId(userId)
            val newUserId = "${userId.split("-")[0]}-${newPhone.substring(8..11)}"
            doc = doc?.copy(
                phone = newPhone,
                userId = newUserId
            )

            if(doc != null) {
                publicUserFbRepo.deleteUser(userId)
                publicUserFbRepo.addUser(doc)
                localSettingsRepo.updateSetting(
                    SettingsDto(
                        newUserId,
                        doc.providesLocation ?: false,
                        false
                    )
                )
                emit(Resource.Success(Unit))
            }else{
                emit(Resource.Error("No such user file found"))
                throw NoSuchFieldException()
            }

        }catch(e: Exception){
            emit(Resource.Error("An error occur while updating phone"))
            e.printStackTrace()
        }

    }

    fun updateHospitalPhone(userId: String, newPhone: String) = flow<Resource<Unit>>{
        emit(Resource.Loading("Updating..."))
        try{
            val newUserId = "${userId.split("-")[0]}-${newPhone.substring(8..11)}"
            var doc = hospitalFbRepo.getHospitalById(userId)
            doc = doc?.copy(
                phone = newPhone,
                userId = newUserId
            )

            if(doc != null) {
                hospitalFbRepo.deleteHospital(userId)
                hospitalFbRepo.addHospital(doc)

                var reqDoc = requestFbRepo.getSpecificHospitalRequest(userId)
                reqDoc = reqDoc?.copy(
                    hospitalId = newUserId
                )
                if(reqDoc != null) {
                    requestFbRepo.deleteRequest(userId)
                    requestFbRepo.addRequest(reqDoc)
                }else{
                    throw NoSuchFieldException()
                }

                val fluidDoc = lifeFluidsRepo.getLifeFluidFromHospital(userId)
                if(fluidDoc != null) {
                    lifeFluidsRepo.deleteHospitalLifeFluidData(userId)
                    lifeFluidsRepo.addHospitalLifeFluidData(newUserId, fluidDoc)
                    localSettingsRepo.updateSetting(
                        SettingsDto(
                            newUserId,
                            false,
                            false
                        )
                    )

                    emit(Resource.Success(Unit))
                }else{
                    emit(Resource.Error("No such lifefluid file found"))
                    throw NoSuchFieldException()
                }

            }else{
                emit(Resource.Error("No such user file found"))
                throw NoSuchFieldException()
            }

        }catch(e: Exception){
            emit(Resource.Error("An error occur while updating phone"))
            e.printStackTrace()
        }

    }




}