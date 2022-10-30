package com.backpapp.gvttest.data.api

import com.backpapp.gvttest.data.api.model.CardApiResponse
import retrofit2.mock.BehaviorDelegate
import retrofit2.mock.MockRetrofit

class MockApi(mockRetrofit: MockRetrofit) : CardApi {

    private val delegate: BehaviorDelegate<CardApi> = mockRetrofit.create(CardApi::class.java)

    override suspend fun getCards(): List<CardApiResponse> {
        val response = listOf(
            CardApiResponse.Blue(
                id = "14515",
                title = "Mr",
                name = "Batman",
                number = "1212 1212 3134 1254"
            ),
            CardApiResponse.Green(
                id = "14121",
                title = "Mr",
                name = "Superman",
                expiryDateAet = "2023-11-10",
                number = "8475 12313 1231"
            ),
            CardApiResponse.Purple(
                id = "14122",
                title = "Mr",
                firstName = "Bruce",
                surname = "Wayne",
                expiryDateAet = "2024-10-10",
                number = "8475 1231 3231",
                referenceNumber = "4561 5487 4861 5348"
            ),
            CardApiResponse.Green(
                id = "14125",
                title = "Mrs",
                name = "Wonderwoman",
                expiryDateAet = "2019-11-01",
                number = "8475 1231 3231"
            )
        )
        return delegate.returningResponse(response).getCards()
    }

    override suspend fun getCard(id: String): CardApiResponse {
        val response = getCards().first {
            it.id == id
        }
        return delegate.returningResponse(response).getCard(id)
    }
}