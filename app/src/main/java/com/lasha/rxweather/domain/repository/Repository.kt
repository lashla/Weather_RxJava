package com.lasha.rxweather.domain.repository

import com.lasha.rxweather.data.entity.WeatherResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import javax.inject.Singleton

@Singleton
interface Repository{
    fun getTempInfo(cityName: String):Single<WeatherResponse>
}