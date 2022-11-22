package com.lasha.rxweather.data.remote

import com.lasha.rxweather.data.entity.WeatherResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RemoteService {

    @GET("/data/2.5/forecast")
    fun getCityTemp(
        @Query("q") cityName: String,
        @Query("cnt") count: Int,
        @Query("units") units: String,
        @Query("appid") appId: String
    ): Single<WeatherResponse>

}