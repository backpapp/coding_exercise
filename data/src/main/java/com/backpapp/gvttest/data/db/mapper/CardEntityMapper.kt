package com.backpapp.gvttest.data.db.mapper

import com.backpapp.gvttest.data.Settings
import com.backpapp.gvttest.data.db.CardEntity
import com.backpapp.gvttest.data.db.CardEntityType
import com.backpapp.gvttest.data.db.exception.MalformedDbException
import com.backpapp.gvttest.domain.model.Card
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class CardEntityMapper @Inject constructor() {

    private companion object {
        const val DATE_FORMAT = "dd-MM-yy"
    }

    fun mapToDomain(entity: List<CardEntity>) = entity.map {
        mapToDomain(it)
    }

    fun mapToDomain(entity: CardEntity) = when (entity.type) {
        CardEntityType.PURPLE -> Card.Purple(
            id = entity.id,
            title = entity.title,
            firstName = entity.firstName ?: throw MalformedDbException,
            surname = entity.surname ?: throw MalformedDbException,
            expiryDate = entity.expiryDateAet?.toLocalDate() ?: throw MalformedDbException,
            number = entity.number,
            referenceNumber = entity.referenceNumber ?: throw MalformedDbException
        )
        CardEntityType.GREEN -> Card.Green(
            id = entity.id,
            title = entity.title,
            name = entity.name ?: throw MalformedDbException,
            number = entity.number,
            expiryDate = entity.expiryDateAet?.toLocalDate() ?: throw MalformedDbException
        )
        CardEntityType.BLUE -> Card.Blue(
            id = entity.id,
            title = entity.title,
            name = entity.name ?: throw MalformedDbException,
            number = entity.number
        )
    }

    fun mapToEntity(cards: List<Card>) = cards.map { card ->
        mapToEntity(card)
    }

    fun mapToEntity(card: Card) = when (card) {
        is Card.Blue -> CardEntity(
            id = card.id,
            title = card.title,
            name = card.name,
            number = card.number,
            type = CardEntityType.BLUE
        )
        is Card.Green -> CardEntity(
            id = card.id,
            title = card.title,
            name = card.name,
            number = card.number,
            expiryDateAet = card.expiryDate.toStoredDate(),
            type = CardEntityType.GREEN
        )
        is Card.Purple -> CardEntity(
            id = card.id,
            title = card.title,
            firstName = card.firstName,
            surname = card.surname,
            expiryDateAet = card.expiryDate.toStoredDate(),
            number = card.number,
            referenceNumber = card.referenceNumber,
            type = CardEntityType.PURPLE
        )
    }

    private fun String.toLocalDate(): LocalDate {
        val formatter = DateTimeFormatter.ofPattern(DATE_FORMAT)
            .withZone(ZoneId.of(Settings.ZONE_ID))
        return LocalDate.parse(this, formatter)
    }

    private fun LocalDate.toStoredDate(): String {
        val formatter = DateTimeFormatter.ofPattern(DATE_FORMAT)
            .withZone(ZoneId.of(Settings.ZONE_ID))
        return this.format(formatter)
    }
}