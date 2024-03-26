package com.osmar.openwaether.view

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.osmar.openwaether.R
import com.osmar.openwaether.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    private lateinit var GET: SharedPreferences
    private lateinit var SET: SharedPreferences.Editor
    private lateinit var map:GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        GET = getSharedPreferences(packageName, MODE_PRIVATE)
        SET = GET.edit()

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        var cName = GET.getString("cityName", "ankara")
        binding.edtCityName.setText(cName)

        viewModel.refreshData(cName!!)
        getLiveData()

        binding.swipeRefreshLayout.setOnRefreshListener{
            binding.llDataView.visibility = View.GONE
            binding.tvError.visibility = View.GONE
            binding.progressBar.visibility = View.GONE

            var cityName = GET.getString("cityName", cName)
            binding.edtCityName.setText(cityName)
            viewModel.refreshData(cityName!!)
            binding.swipeRefreshLayout.isRefreshing = false
        }

        binding.imgSearch.setOnClickListener {
            val cityName = binding.edtCityName.text.toString()
            SET.putString("cityName", cityName)
            SET.apply()
            viewModel.refreshData(cityName)
            getLiveData()
            getCityCoordinates(cityName)
        }

        createFragment()

    }

    private fun getCityCoordinates(cityName: String) {
        viewModel.weather_data.observe(this,{data ->
            data?.let {
                val lat = data.coord.lat
                val lon = data.coord.lon
                createMarkert(LatLng(lat, lon))
            }
        })


    }

    private fun createFragment() {
        val mapFragment: SupportMapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun getLiveData() {
        viewModel.weather_data.observe(this, {data ->
            data?.let {
                binding.llDataView.visibility = View.VISIBLE
                binding.progressBar.visibility = View.VISIBLE
               //binding.tvDegree.text = data.main.temp.toString()
                binding.tvCountryCode.text = data.sys.country.toString()
                binding.tvCityName.text = data.name.toString()
                binding.tvHumidity.text = data.main.humidity.toString()
                binding.tvWindSpeed.text = data.wind.speed.toString()
                binding.tvLat.text = data.coord.lat.toString()
                binding.tvLon.text = data.coord.lon.toString()
            }
        })
        viewModel.weather_load.observe(this, {load ->
            load?.let {
                if (it){

                    binding.progressBar.visibility = View.VISIBLE
                    binding.tvError.visibility = View.GONE
                    binding.llDataView.visibility = View.GONE
                } else {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        })
        viewModel.weather_error.observe(this, {error ->
            error?.let {
                if (it){
                    binding.tvError.visibility = View.GONE
                    binding.llDataView.visibility = View.GONE
                    binding.progressBar.visibility = View.VISIBLE
                } else{
                    binding.tvError.visibility = View.GONE
                }
            }
        })
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        val defaultCoordinates = LatLng(19.442119, -99.203815) // Coordenadas por defecto
        createMarkert(defaultCoordinates)
    }

    private fun createMarkert(coordinates: LatLng) {

        val marker: MarkerOptions = MarkerOptions().position(coordinates).title("Mi lgar favorito!")
        map.addMarker(marker)
        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(coordinates, 12f),
            4000,null
        )
    }
}