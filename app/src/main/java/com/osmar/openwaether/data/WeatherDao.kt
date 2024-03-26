package com.osmar.openwaether.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WeatherDao {
    @Query("SELECT * FROM weather_data WHERE cityName = :cityName")
    fun getWeatherData(cityName: String): WeatherDataEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWeatherData(weatherDataEntity: WeatherDataEntity)
}