package com.pratyaksh.healthykingdom.domain.use_case.settings

import com.pratyaksh.healthykingdom.data.dto.SettingsDto
import com.pratyaksh.healthykingdom.domain.repository.LocalUserSettingRepo
import javax.inject.Inject

class GetSettingsUseCase @Inject constructor(
    private val settingsRepo: LocalUserSettingRepo
) {

    suspend fun isGoingLive(
        userId: String
    ): Boolean{
        return settingsRepo.isSharingLive(userId)
    }

    suspend fun showLiveLocAllowed(
        userId: String
    ): Boolean{
        return settingsRepo.isSharingLocAllowed(userId)
    }

}