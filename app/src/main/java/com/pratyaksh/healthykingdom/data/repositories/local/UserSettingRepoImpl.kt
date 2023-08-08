package com.pratyaksh.healthykingdom.data.repositories.local

import com.pratyaksh.healthykingdom.data.database.SettingsDao
import com.pratyaksh.healthykingdom.data.dto.SettingsDto
import com.pratyaksh.healthykingdom.domain.repository.LocalUserSettingRepo

class UserSettingRepoImpl (
    private val settingsDao: SettingsDao
): LocalUserSettingRepo {
    override suspend fun addSettings(settingEntity: SettingsDto) {
        settingsDao.addSettings(settingEntity)
    }

    override suspend fun isSharingLocAllowed(
        id: String
    ) = settingsDao.isSharingLocAllowed(id = id) ?: false


    override suspend fun isSharingLive(id: String
    ) = settingsDao.isSharingLive(id = id)


    override suspend fun updateSetting(setting: SettingsDto) {
        settingsDao.updateSetting(setting = setting)
    }

    override suspend fun deleteSettings(userId: String) {
        settingsDao.deleteSettings(settingsDao.getSettings(userId))
    }

    override suspend fun updateShowLocation(userId: String, permit: Boolean) {
        settingsDao.updateshowLocPermit(userId, permit)
    }

    override suspend fun updateGoLiveState(userId: String, setToGoLive: Boolean) {
        settingsDao.updateGoLiveState(id = userId, setToGoLive)
    }
}