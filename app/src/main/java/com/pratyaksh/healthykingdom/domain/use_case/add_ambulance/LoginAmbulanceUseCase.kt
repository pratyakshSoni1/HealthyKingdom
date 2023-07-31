package com.pratyaksh.healthykingdom.domain.use_case.add_ambulance

import com.pratyaksh.healthykingdom.domain.repository.RemoteAmbulanceFbRepo
import com.pratyaksh.healthykingdom.domain.use_case.settings.UpdateSettingUseCase
import com.pratyaksh.healthykingdom.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginAmbulanceUseCase @Inject constructor(
    private val ambulanceFbRepo: RemoteAmbulanceFbRepo,
    private val settingUpdateUseCase: UpdateSettingUseCase
) {

    operator fun invoke(phone: String, password: String): Flow<Resource<String?>> = flow{

        emit(Resource.Loading("Authenticating..."))
        try{
            val reqUser = ambulanceFbRepo.getAmbulanceByPhone(phone)
            if(reqUser != null){
                if(reqUser.password == password) {
                    settingUpdateUseCase.updateSettings(
                        userId = reqUser.userId!!,
                        showLocOnMap = false,
                        goLive = reqUser.online
                    )
                    emit(Resource.Success(reqUser.userId))
                }
                else
                    emit(Resource.Error("Invalid password"))
            }else{
                emit(Resource.Error("User not found."))
            }
        }catch(e: Exception){
            e.printStackTrace()
            emit(Resource.Error("Unexpected error !\nPlease Try again later"))
        }

    }



}