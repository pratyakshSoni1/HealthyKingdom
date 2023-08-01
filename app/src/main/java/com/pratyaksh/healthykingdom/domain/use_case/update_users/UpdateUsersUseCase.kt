package com.pratyaksh.healthykingdom.domain.use_case.update_users

import com.pratyaksh.healthykingdom.domain.model.Users
import com.pratyaksh.healthykingdom.domain.model.toAmbulanceDto
import com.pratyaksh.healthykingdom.domain.model.toHospitalDto
import com.pratyaksh.healthykingdom.domain.model.toPublicUserDto
import com.pratyaksh.healthykingdom.domain.repository.RemoteAmbulanceFbRepo
import com.pratyaksh.healthykingdom.domain.repository.RemoteHospitalFbRepo
import com.pratyaksh.healthykingdom.domain.repository.RemoteLifeFluidsFbRepo
import com.pratyaksh.healthykingdom.domain.repository.RemotePublicUserFbRepo
import com.pratyaksh.healthykingdom.domain.repository.RemoteRequestsRepo
import com.pratyaksh.healthykingdom.utils.Resource
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateUsersUseCase @Inject constructor(
    val hospitalRepo: RemoteHospitalFbRepo,
    val ambulanceFbRepo: RemoteAmbulanceFbRepo,
    val publicUserRepo: RemotePublicUserFbRepo,
) {

    fun updateAmbulance( ambulance: Users.Ambulance) = flow<Resource<Unit>>{
        emit(Resource.Loading("Updating Ambulance..."))
        try{
            ambulanceFbRepo.updateAmbulance(ambulance.toAmbulanceDto())
            emit(Resource.Success(Unit))
        }catch(e: Exception){
            e.printStackTrace()
            emit(Resource.Error("Error Updating ambulance"))
        }
    }

    fun updatePublicUser( user: Users.PublicUser) = flow<Resource<Unit>>{
        emit(Resource.Loading("Updating Ambulance..."))
        try{
            publicUserRepo.updateUser(user.toPublicUserDto())
            emit(Resource.Success(Unit))
        }catch(e: Exception){
            e.printStackTrace()
            emit(Resource.Error("Error Updating ambulance"))
        }
    }

    fun updateHospital( hospital: Users.Hospital) = flow<Resource<Unit>>{
        emit(Resource.Loading("Updating Ambulance..."))
        try{
            hospitalRepo.updateHospital(hospital.toHospitalDto())
            emit(Resource.Success(Unit))
        }catch(e: Exception){
            e.printStackTrace()
            emit(Resource.Error("Error Updating ambulance"))
        }
    }




}