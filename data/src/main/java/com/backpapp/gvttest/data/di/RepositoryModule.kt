package com.backpapp.gvttest.data.di

import android.content.Context
import androidx.room.Room
import com.backpapp.gvttest.data.api.CardApi
import com.backpapp.gvttest.data.api.RealCardRepository
import com.backpapp.gvttest.data.api.mapper.CardApiResponseMapper
import com.backpapp.gvttest.data.db.AppDatabase
import com.backpapp.gvttest.data.db.CardDao
import com.backpapp.gvttest.data.db.mapper.CardEntityMapper
import com.backpapp.gvttest.domain.repository.CardRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "Cards"
        ).build()
    }

    @Provides
    fun provideCardDao(appDatabase: AppDatabase): CardDao {
        return appDatabase.cardDao()
    }

    @Provides
    fun repository(
        cardRepository: CardApi,
        cardDao: CardDao,
        responseMapper: CardApiResponseMapper,
        entityMapper: CardEntityMapper
    ): CardRepository = RealCardRepository(cardRepository, cardDao, responseMapper, entityMapper)

}