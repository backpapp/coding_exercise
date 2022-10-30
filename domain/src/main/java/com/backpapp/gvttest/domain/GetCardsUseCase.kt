package com.backpapp.gvttest.domain

import com.backpapp.gvttest.domain.model.Card
import com.backpapp.gvttest.domain.repository.CardRepository
import com.backpapp.gvttest.domain.repository.Response
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCardsUseCase @Inject constructor(private val repository: CardRepository) {

    suspend operator fun invoke(refresh: Boolean) = repository.getCards(refresh).map { response ->
        if (response is Response.Success) {
            Result.Success(response.item)
        } else {
            Result.Error
        }
    }

    sealed class Result {
        data class Success(val cards: List<Card>) : Result()
        object Error : Result()
    }
}