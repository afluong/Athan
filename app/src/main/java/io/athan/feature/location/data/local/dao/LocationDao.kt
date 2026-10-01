package io.athan.feature.location.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.athan.feature.location.data.local.entity.LocationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {

    @Query("SELECT * FROM saved_location WHERE id = 1 LIMIT 1")
    fun getSavedLocation(): Flow<LocationEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveLocation(location: LocationEntity)

    @Query("DELETE FROM saved_location")
    fun clearLocation()
}