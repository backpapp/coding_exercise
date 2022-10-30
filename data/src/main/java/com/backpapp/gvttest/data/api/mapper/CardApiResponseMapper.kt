package com.backpapp.gvttest.data.api.mapper

import com.backpapp.gvttest.data.Settings
import com.backpapp.gvttest.data.api.model.CardApiResponse
import com.backpapp.gvttest.domain.model.Card
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class CardApiResponseMapper @Inject constructor() {

    fun mapToDomain(response: List<CardApiResponse>) = response.map {
        mapToDomain(it)
    }

    fun mapToDomain(response: CardApiResponse) = when (response) {
        is CardApiResponse.Blue -> Card.Blue(
            id = response.id,
            title = response.title,
            name = response.name,
            number = response.number
        )
        is CardApiResponse.Green -> Card.Green(
            id = response.id,
            title = response.title,
            name = response.name,
            number = response.number,
            expiryDate = response.expiryDateAet.toLocalDate()
        )
        is CardApiResponse.Purple -> Card.Purple(
            id = response.id,
            title = response.title,
            firstName = response.firstName,
            surname = response.surname,
            expiryDate = response.expiryDateAet.toLocalDate(),
            number = response.number,
            referenceNumber = response.referenceNumber
        )
    }

    private fun String.toLocalDate(): LocalDate {
        val formatter = DateTimeFormatter.ofPattern(CardApiResponse.DATE_FORMAT)
            .withZone(ZoneId.of(Settings.ZONE_ID))
        return LocalDate.parse(this, formatter)
    }
}

