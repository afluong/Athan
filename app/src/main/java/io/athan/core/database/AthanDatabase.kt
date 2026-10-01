package io.athan.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import io.athan.feature.location.data.local.dao.LocationDao
import io.athan.feature.location.data.local.entity.LocationEntity

@Database(
    entities = [LocationEntity::class],
    version = 3,
    exportSchema = false
)
abstract class AthanDatabase : RoomDatabase() {
    abstract fun locationDao(): LocationDao
}