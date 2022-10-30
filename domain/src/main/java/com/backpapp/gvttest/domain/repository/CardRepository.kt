package com.backpapp.gvttest.domain.repository

import com.backpapp.gvttest.domain.model.Card
import kotlinx.coroutines.flow.Flow

interface CardRepository {
    suspend fun getCard(cardId: String, refresh: Boolean): Flow<Response<Card>>
    suspend fun getCards(refresh: Boolean): Flow<Response<List<Card>>>
    suspend fun deleteCard(cardId: String): Flow<Response<*>>
}