package com.backpapp.gvttest.data.di.apiModule

import com.backpapp.gvttest.data.api.CardApi
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
        return retrofitBuilder.build().create(CardApi::class.java)
    }

}