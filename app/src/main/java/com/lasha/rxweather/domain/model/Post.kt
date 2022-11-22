package com.lasha.rxweather.domain.model

data class Post(
    val city: City?,
    val cnt: Int?,
    val cod: String?,
    val list: List<ApiResponse>?,
    val message: Int?
)