package com.osmar.openwaether.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.osmar.openwaether.model.WeatherModel
import com.osmar.openwaether.service.WeatherAPIService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class MainViewModel:  ViewModel(){

    private val weatherAPIService = WeatherAPIService()
    private val disponsable = CompositeDisposable()

    val weather_data = MutableLiveData<WeatherModel>()
    val weather_error = MutableLiveData<Boolean>()
    val weather_load = MutableLiveData<Boolean>()

    fun refreshData(cityName: String){
        getDataFromAPI(cityName)
        //getDataFromLocal()
    }

    private fun getDataFromAPI(cityName: String) {
        weather_load.value = true
        disponsable.add(
            weatherAPIService.getDataService(cityName)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableSingleObserver<WeatherModel>(){
                    override fun onSuccess(t: WeatherModel) {
                        weather_data.value = t
                        weather_error.value = false
                        weather_load.value = false
                    }

                    override fun onError(e: Throwable) {
                        weather_error.value = true
                        weather_load.value = true
                    }

                })
        )
    }


}