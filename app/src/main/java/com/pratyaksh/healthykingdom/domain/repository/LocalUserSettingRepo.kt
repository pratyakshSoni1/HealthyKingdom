package com.pratyaksh.healthykingdom.domain.repository

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.pratyaksh.healthykingdom.data.dto.SettingsDto

interface LocalUserSettingRepo{

    suspend fun addSettings( settingEntity: SettingsDto)

    suspend fun isSharingLocAllowed(id: String): Boolean

    suspend fun isSharingLive(id: String): Boolean

    suspend fun updateSetting(setting: SettingsDto)

    suspend fun deleteSettings(userId: String)

    suspend fun updateShowLocation(userId: String, permit:Boolean)

    suspend fun updateGoLiveState(userId: String, setToGoLive: Boolean)

}