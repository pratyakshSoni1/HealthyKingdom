package com.pratyaksh.healthykingdom.domain.use_case.settings

import com.pratyaksh.healthykingdom.data.dto.SettingsDto
import com.pratyaksh.healthykingdom.domain.repository.LocalUserSettingRepo
import javax.inject.Inject

class AddSettingsUseCase @Inject constructor(
    val settingsRepo: LocalUserSettingRepo
) {

    suspend operator fun invoke(
        isGoingLive: Boolean,
        showLocaOnMap: Boolean,
        userId: String
    ){
        settingsRepo.addSettings(
            SettingsDto(
                userId,
                showLocaOnMap,
                isGoingLive
            )
        )
    }

}