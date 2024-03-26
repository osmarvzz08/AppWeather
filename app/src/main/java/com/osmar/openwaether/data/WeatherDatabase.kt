package com.osmar.openwaether.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [WeatherDataEntity::class], version = 1)
abstract class WeatherDatabase: RoomDatabase() {
    abstract fun  weatherDao(): WeatherDao
}