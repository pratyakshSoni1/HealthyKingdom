package com.pratyaksh.healthykingdom.data.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SettingsDto (
    @PrimaryKey(autoGenerate = false) val userId: String,
    val showYourLocOnMap: Boolean,
    val goLive: Boolean
)