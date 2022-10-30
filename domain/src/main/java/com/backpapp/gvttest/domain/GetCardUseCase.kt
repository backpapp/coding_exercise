package com.backpapp.gvttest.domain

import com.backpapp.gvttest.domain.model.Card
import com.backpapp.gvttest.domain.repository.CardRepository
import com.backpapp.gvttest.domain.repository.Response
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCardUseCase @Inject constructor(private val repository: CardRepository) {

    suspend operator fun invoke(cardId: String, refresh: Boolean) =
        repository.getCard(cardId, refresh).map { response ->
            if (response is Response.Success) {
                Result.Success(response.item)
            } else {
                Result.Error
            }
        }

    sealed class Result {
        data class Success(val card: Card) : Result()
        object Error : Result()
    }
}