package com.backpapp.gvttest.data.api

import com.backpapp.gvttest.data.api.mapper.CardApiResponseMapper
import com.backpapp.gvttest.data.db.CardDao
import com.backpapp.gvttest.data.db.mapper.CardEntityMapper
import com.backpapp.gvttest.domain.model.Card
import com.backpapp.gvttest.domain.repository.CardRepository
import com.backpapp.gvttest.domain.repository.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class RealCardRepository @Inject constructor(
    private val cardApi: CardApi,
    private val dao: CardDao,
    private val responseMapper: CardApiResponseMapper,
    private val dbMapper: CardEntityMapper
) : CardRepository {

    override suspend fun getCard(cardId: String, refresh: Boolean): Flow<Response<Card>> {
        return if (refresh) {
            getCardFromApi(cardId)
        } else {
            getCardFromCache(cardId).map { dbResponse ->
                if (dbResponse is Response.Success) {
                    dbResponse
                } else {
                    getCardFromApi(cardId).first()
                }
            }
        }
    }

    override suspend fun getCards(refresh: Boolean): Flow<Response<List<Card>>> {
        return if (refresh) {
            getCardsFromApi()
        } else {
            getCardsFromCache().map { dbResponse ->
                if (dbResponse is Response.Success && dbResponse.item.isNotEmpty()) {
                    dbResponse
                } else {
                    getCardsFromApi().first()
                }
            }
        }
    }

    override suspend fun deleteCard(cardId: String): Flow<Response<*>> =
        flow {
            try {
                dao.delete(cardId)
                emit(Response.Success(null))
            } catch (e: Exception) {
                emit(Response.Error<Nothing>(Throwable()))
            }
        }.flowOn(Dispatchers.IO)


    private suspend fun getCardFromCache(cardId: String): Flow<Response<Card>> =
        flowOf(
            try {
                Response.Success(dbMapper.mapToDomain(dao.get(cardId)))
            } catch (e: Exception) {
                Response.Error(e)
            }
        ).flowOn(Dispatchers.IO)

    private suspend fun getCardFromApi(cardId: String) = flow {
        try {
            val card = responseMapper.mapToDomain(cardApi.getCard(cardId))
            MainScope().launch(context = Dispatchers.IO) {
                dao.insertAll(dbMapper.mapToEntity(card))
            }
            emit(Response.Success(card))
        } catch (e: Exception) {
            emit(Response.Error(e))
        }
    }.flowOn(Dispatchers.IO)

    private suspend fun getCardsFromCache(): Flow<Response<List<Card>>> =
        flowOf(
            try {
                Response.Success(dbMapper.mapToDomain(dao.getAll()))
            } catch (e: Exception) {
                Response.Error(e)
            }
        ).flowOn(Dispatchers.IO)

    private suspend fun getCardsFromApi() = flow {
        try {
            val cards = responseMapper.mapToDomain(cardApi.getCards())
            MainScope().launch(context = Dispatchers.IO) {
                dao.insertAll(*dbMapper.mapToEntity(cards).toTypedArray())
            }
            emit(Response.Success(cards))
        } catch (e: Exception) {
            emit(Response.Error(e))
        }
    }.flowOn(Dispatchers.IO)
}