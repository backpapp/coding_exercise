package com.backpapp.gvttest.domain

import com.backpapp.gvttest.domain.repository.CardRepository
import com.backpapp.gvttest.domain.repository.Response
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DeleteCardUseCase @Inject constructor(private val repository: CardRepository) {

    suspend operator fun invoke(cardId: String) = repository.deleteCard(cardId).map { response ->
        if (response is Response.Success) {
            Result.Success
        } else {
            Result.Error
        }
    }

    sealed class Result {
        object Success : Result()
        object Error : Result()
    }
}