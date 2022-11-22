package com.lasha.rxweather.ui

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.lasha.rxweather.R
import com.lasha.rxweather.data.entity.WeatherResponse
import com.lasha.rxweather.domain.model.ForecastItem
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.time.LocalDateTime
import java.util.*
import kotlin.math.roundToInt

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var viewModel: MainViewModel
    private lateinit var currentLocation: Location
    private var geoCityName: String? = null
    private val onlineForecastPost = ArrayList<ForecastItem>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
        initView()
        setupSearchButtons()
        setupFusedLocation()
        setupLocationWeatherButton()
    }

    private fun initView() {
        textInput.setOnEditorActionListener { _, keyCode, event ->
            if (((event?.action ?: -1) == KeyEvent.ACTION_DOWN)
                || keyCode == EditorInfo.IME_ACTION_DONE
            ) {
                searchView.visibility = View.INVISIBLE
                textInput.visibility = View.INVISIBLE
                cancelSearchBtn.visibility = View.INVISIBLE
                weatherDisplay(textInput.text.toString())
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    private fun setupViews(weatherResponse: WeatherResponse){
        enteredCityName.text = weatherResponse.city.name
        val temperature = weatherResponse.list[0].main?.temp?.roundToInt().toString() + "°C"
        tvTemperature.text = temperature
        tvTemperature.visibility = View.VISIBLE
        Picasso.get()
            .load("https://openweathermap.org/img/w/" + weatherResponse.list[0].weather?.get(0)?.icon.toString() + ".png")
            .error(androidx.constraintlayout.widget.R.drawable.abc_btn_check_to_on_mtrl_000)
            .into(imageView)
        imageView.visibility = View.VISIBLE
        tvHumidityValue.text = weatherResponse.list[0].main?.humidity.toString()
        tvAirPressureValue.text = weatherResponse.list[0].main?.pressure.toString()
        windStatusText.text = weatherResponse.list[0].wind?.speed.toString()
        tvVisibilityValue.text = weatherResponse.list[0].visibility.toString()
        weatherDiscription.text = weatherResponse.list[0].weather?.get(0)?.description.toString()
        for (weatherElement in 1..11){
            val localDateTime = LocalDateTime.parse((weatherResponse.list[weatherElement].dt_txt.toString().replace(" ", "T")))
            onlineForecastPost.add(ForecastItem(weatherResponse.list[weatherElement].main?.temp?.roundToInt().toString() + "°C", "https://openweathermap.org/img/w/" + weatherResponse.list[weatherElement].weather?.get(0)?.icon.toString() + ".png",localDateTime.hour.toString() + ":00"))
        }
        initRecyclerView(data = onlineForecastPost)
    }

    private fun initRecyclerView(data: ArrayList<ForecastItem>){
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val adapter = CustomRecyclerAdapter(this, data)
        recyclerView.adapter = adapter
    }

    private fun initViewModel(){
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
    }

    private fun setupLocationWeatherButton(){
        locationButton.setOnClickListener {
            getWeatherInfoFromGps(geoCityName)
        }
    }

    private fun setupSearchButtons() {
        searchBtn.setOnClickListener {
            searchView.visibility = View.VISIBLE
            textInput.visibility = View.VISIBLE
            cancelSearchBtn.visibility = View.VISIBLE
        }
        cancelSearchBtn.setOnClickListener{
            searchView.visibility = View.INVISIBLE
            textInput.visibility = View.INVISIBLE
            cancelSearchBtn.visibility = View.INVISIBLE
        }
    }

    private fun getWeatherInfoFromGps(geoCityName: String?){
        if (!geoCityName.isNullOrEmpty()){
            weatherDisplay(geoCityName)
        }
    }

    private fun weatherDisplay(cityInput: String) {
        enteredCityName.visibility = View.VISIBLE
        viewModel.makeRequest(cityInput)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                   setupViews(it)
                },
                {
                    Log.e("Request", it.message.toString())
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
            )
    }

    private fun setupFusedLocation(){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        var isLocationGranted = true
        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            isLocationGranted = when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    true
                }
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    true
                } else -> {
                    false
                }
            }
        }

        locationPermissionRequest.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION))

        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) -> {
                val geocoder = Geocoder(this, Locale.getDefault())
                isLocationGranted = true
                fusedLocationClient.lastLocation
                    .addOnSuccessListener {
                        if (it != null){
                            currentLocation = it
                            geoCityName = geocoder.getFromLocation(currentLocation.latitude, currentLocation.longitude, 1)
                                ?.get(0)?.locality
                        } else {
                            Toast.makeText(this, "Isn't possible to take your last location, setting to default", Toast.LENGTH_LONG).show()
                        }
                    }

                    .addOnFailureListener {

                    }


            }
            else -> {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
                    1)
            }
        }
    }
}