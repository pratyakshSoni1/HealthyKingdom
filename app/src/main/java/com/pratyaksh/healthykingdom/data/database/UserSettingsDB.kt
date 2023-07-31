package com.pratyaksh.healthykingdom.data.database

import androidx.room.Database
import androidx.room.DatabaseConfiguration
import androidx.room.Entity
import androidx.room.InvalidationTracker
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.pratyaksh.healthykingdom.data.dto.SettingsDto

@Database(
    entities = [SettingsDto::class],
    version= 1
)
abstract class UserSettingsDB: RoomDatabase() {
    abstract val dao: SettingsDao
}