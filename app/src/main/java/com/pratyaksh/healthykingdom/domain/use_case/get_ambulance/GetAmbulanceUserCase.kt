package com.pratyaksh.healthykingdom.domain.use_case.get_ambulance

import com.pratyaksh.healthykingdom.data.dto.toAmbulance
import com.pratyaksh.healthykingdom.domain.model.Users
import com.pratyaksh.healthykingdom.domain.repository.RemoteAmbulanceFbRepo
import com.pratyaksh.healthykingdom.utils.Resource
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAmbulanceUserCase @Inject constructor(
    private val fbRepo: RemoteAmbulanceFbRepo
){

    fun getAmbulanceByUserId(userId: String) = flow<Resource<Users.Ambulance>>{
        emit(Resource.Loading("Can't retreive data"))
        try{
            emit(Resource.Success(fbRepo.getAmbulanceById(userId)!!.toAmbulance()))
        }catch(e: Exception){
            e.printStackTrace()
            emit(Resource.Error("Can't retreive data"))
        }
    }

    fun getAmbulanceByVehicleNumber(vehicleNum: String) = flow<Resource<Users.Ambulance>>{
        emit(Resource.Loading("Can't retreive data"))
        try{
            emit(Resource.Success(fbRepo.getAmbulanceByNumber(vehicleNum)!!.toAmbulance()))
        }catch(e: Exception){
            e.printStackTrace()
            emit(Resource.Error("Can't retreive data"))
        }
    }

    fun getAmbulanceByPhone(phone: String) = flow<Resource<Users.Ambulance>>{
        emit(Resource.Loading("Can't retreive data"))
        try{
            emit(Resource.Success(fbRepo.getAmbulanceByPhone(phone)!!.toAmbulance()))
        }catch(e: Exception){
            e.printStackTrace()
            emit(Resource.Error("Can't retreive data"))
        }
    }

}