package com.osmar.openwaether.service

import com.osmar.openwaether.model.WeatherModel
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class WeatherAPIService {
//http://api.openweathermap.org/data/2.5/weather?q=bingöl&APPID=2d2b725fc891eee831b00c0c1ca10955

    private val BASE_URL = "http://api.openweathermap.org/"
    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(WeatherAPI::class.java)

    fun getDataService(cityName: String): Single<WeatherModel>{
        return api.getData(cityName)
    }
}