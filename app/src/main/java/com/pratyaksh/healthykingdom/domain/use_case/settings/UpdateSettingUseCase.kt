package com.pratyaksh.healthykingdom.domain.use_case.settings

import com.pratyaksh.healthykingdom.data.dto.SettingsDto
import com.pratyaksh.healthykingdom.domain.repository.LocalUserSettingRepo
import javax.inject.Inject

class UpdateSettingUseCase @Inject constructor(
    private val settingsRepo: LocalUserSettingRepo
) {

    suspend fun logoutUser(userId: String){
        settingsRepo.deleteSettings(userId)
    }

    suspend fun addLocalSettings(
        userId: String,
        showLocOnMap: Boolean,
        goLive: Boolean
    ){
        settingsRepo.addSettings(
            SettingsDto(
                userId,
                showYourLocOnMap = showLocOnMap,
                goLive
            )
        )
    }

    suspend fun updateSettings(
        userId: String,
        showLocOnMap: Boolean,
        goLive: Boolean
    ){
        settingsRepo.updateSetting(
            SettingsDto(
                userId,
                showYourLocOnMap = showLocOnMap,
                goLive
            )
        )
    }

    suspend fun setShowLiveLocPermission(
        userId: String,
        permit: Boolean
    ){
        settingsRepo.updateShowLocation(userId, permit)
    }

    suspend fun setGoLive(
        userId: String,
        setToGoLive: Boolean
    ){
        settingsRepo.updateGoLiveState(userId, setToGoLive)
    }

    suspend fun deleteSettings(
        userId: String
    ){
        settingsRepo.deleteSettings(userId)
    }

}