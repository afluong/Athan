package io.athan.feature.location.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_location")
data class LocationEntity(
    @PrimaryKey val id: Int = 1, // Only have one location saved at a time
    val name: String,
    val country: String,
    val latitude: Double,
    val longitude: Double,
)