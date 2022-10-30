package com.backpapp.gvttest.data.factory

import com.backpapp.gvttest.data.api.model.CardApiResponse

object CardApiResponseFactory {

    fun createBlue(
        id: String = "id",
        title: String = "title",
        name: String = "name",
        number: String = "number"
    ) = CardApiResponse.Blue(id, title, name, number)

    fun createGreen(
        id: String = "id",
        title: String = "title",
        name: String = "name",
        expiryDateAet: String = "2024-10-11",
        number: String = "number"
    ) = CardApiResponse.Green(id, title, name, expiryDateAet, number)

    fun createPurple(
        id: String = "id",
        title: String = "title",
        firstName: String = "firstName",
        surname: String = "surname",
        expiryDateAet: String = "2024-10-11",
        number: String = "number",
        referenceNumber: String = "referenceNumber"
    ) = CardApiResponse.Purple(
        id,
        title,
        firstName,
        surname,
        expiryDateAet,
        number,
        referenceNumber
    )


}