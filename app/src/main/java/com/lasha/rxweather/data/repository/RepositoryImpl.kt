package com.lasha.rxweather.data.repository

import com.lasha.rxweather.data.entity.WeatherResponse
import com.lasha.rxweather.data.remote.RemoteService
import com.lasha.rxweather.domain.repository.Repository
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import javax.inject.Singleton

@Singleton
class RepositoryImpl(private val remoteService: RemoteService) : Repository {
    override fun getTempInfo(
        cityName: String,
    ): Single<WeatherResponse> {
        return remoteService.getCityTemp(cityName, 12,"metric", "6941119512c9b9a65ba6b2583362f475")
    }
}