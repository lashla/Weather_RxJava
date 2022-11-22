package com.lasha.rxweather.data.entity

import com.google.gson.annotations.SerializedName
import com.lasha.rxweather.domain.model.ApiResponse
import com.lasha.rxweather.domain.model.City

class WeatherResponse(
    @SerializedName("list") val list: List<ApiResponse>,
    @SerializedName("city") val city: City
)