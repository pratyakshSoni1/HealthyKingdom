package com.pratyaksh.healthykingdom.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.pratyaksh.healthykingdom.data.dto.SettingsDto

@Dao
interface SettingsDao {

    @Insert( onConflict = OnConflictStrategy.REPLACE )
    suspend fun addSettings( settingEntity: SettingsDto )

    @Query( "SELECT showYourLocOnMap FROM SettingsDto WHERE userId =:id" )
    suspend fun isSharingLocAllowed(id: String): Boolean

    @Query( "SELECT goLive FROM SettingsDto WHERE userId =:id" )
    suspend fun isSharingLive(id: String): Boolean

    @Query( "SELECT * FROM SettingsDto WHERE userId =:id" )
    suspend fun getSettings(id: String): SettingsDto

    @Query( "UPDATE SettingsDto SET goLive =:permit WHERE userId =:id" )
    suspend fun updateGoLiveState(id: String, permit: Boolean)

    @Query( "UPDATE SettingsDto SET showYourLocOnMap =:permit WHERE userId =:id" )
    suspend fun updateshowLocPermit(id: String, permit: Boolean)

    @Update
    suspend fun updateSetting(setting: SettingsDto)

    @Delete
    suspend fun deleteSettings(setting: SettingsDto)

}