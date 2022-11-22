package com.example.weatherapp.di

import com.lasha.rxweather.data.remote.RemoteService
import com.lasha.rxweather.data.repository.RepositoryImpl
import com.lasha.rxweather.domain.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule  {

    @Provides
    @Singleton
    fun providesRepository(remoteService: RemoteService) : Repository {
        return RepositoryImpl(remoteService = remoteService)
    }

}