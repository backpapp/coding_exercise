package com.backpapp.gvttest.data.di

import com.backpapp.gvttest.data.Environment.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun retrofitBuilder() = Retrofit.Builder().baseUrl(BASE_URL)

}