package com.backpapp.gvttest.data.di

import com.backpapp.gvttest.data.api.CardApi
import com.backpapp.gvttest.data.api.MockApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.mock.MockRetrofit
import retrofit2.mock.NetworkBehavior
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @Provides
    @Singleton
    fun api(retrofitBuilder: Retrofit.Builder): CardApi {
        val mockRetrofit = MockRetrofit.Builder(retrofitBuilder.build()).networkBehavior(
            NetworkBehavior.create().also {
                it.setDelay(1, TimeUnit.SECONDS)
            }).build()
        return MockApi(mockRetrofit)
    }

}