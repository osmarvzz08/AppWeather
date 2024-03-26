package com.osmar.openwaether.service

import com.osmar.openwaether.model.WeatherModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

//http://api.openweathermap.org/data/2.5/weather?q=bingöl&APPID=2d2b725fc891eee831b00c0c1ca10955
interface WeatherAPI {

  @GET("data/2.5/weather?&APPID=2d2b725fc891eee831b00c0c1ca10955")
  fun getData(
    @Query("q") cityName: String
  ): Single<WeatherModel>
}