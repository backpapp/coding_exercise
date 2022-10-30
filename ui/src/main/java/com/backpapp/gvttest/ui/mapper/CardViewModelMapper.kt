package com.backpapp.gvttest.ui.mapper

import com.backpapp.gvttest.domain.model.Card
import com.backpapp.gvttest.ui.model.CardType
import com.backpapp.gvttest.ui.model.CardViewModel
import com.backpapp.gvttest.ui.model.TextViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import com.backpapp.gvttest.ui.R as UiR

class CardViewModelMapper @Inject constructor() {

    companion object {
        const val DATE_FORMAT = "MM/yy"
    }

    fun mapToViewModel(cards: List<Card>, showDelete: Boolean) = cards.map {
        mapToViewModel(it, showDelete)
    }

    fun mapToViewModel(card: Card, showDelete: Boolean): CardViewModel = when (card) {
        is Card.Blue -> CardViewModel(
            id = card.id,
            name = formatName(card.title, card.name),
            cardNumber = card.number,
            cardType = CardType.BLUE,
            showDelete = showDelete,
            contentDescription = buildContentDescription(card)
        )
        is Card.Green -> CardViewModel(
            id = card.id,
            name = formatName(card.title, card.name),
            cardNumber = card.number,
            expiryDate = card.expiryDate.format(),
            cardType = CardType.GREEN,
            isExpired = card.expiryDate.isExpired(),
            showDelete = showDelete,
            contentDescription = buildContentDescription(card)
        )
        is Card.Purple -> CardViewModel(
            id = card.id,
            name = formatName(card.title, card.firstName, card.surname),
            cardNumber = card.number,
            referenceNumber = TextViewModel.Resource(
                UiR.string.reference_format,
                card.referenceNumber
            ),
            expiryDate = card.expiryDate.format(),
            cardType = CardType.PURPLE,
            isExpired = card.expiryDate.isExpired(),
            showDelete = showDelete,
            contentDescription = buildContentDescription(card)
        )
    }

    private fun formatName(vararg elements: String) = elements.joinToString(" ")

    private fun buildContentDescription(card: Card) = when (card) {
        is Card.Blue -> listOf(
            TextViewModel.Resource(UiR.string.blue_card),
            TextViewModel.Text(card.name),
            TextViewModel.Resource(UiR.string.card_number_accessibility_format, card.number),
            TextViewModel.Resource(UiR.string.no_expiration)
        )
        is Card.Purple -> listOf(
            TextViewModel.Resource(UiR.string.purple_card),
            TextViewModel.Text(formatName(card.title, card.firstName, card.surname)),
            TextViewModel.Resource(UiR.string.card_number_accessibility_format, card.number),
            TextViewModel.Resource(UiR.string.reference_accessibility_format, card.referenceNumber),
            card.expiryDate.format()
        )
        is Card.Green -> listOf(
            TextViewModel.Resource(UiR.string.green_card),
            TextViewModel.Text(formatName(card.title, card.name)),
            TextViewModel.Resource(UiR.string.card_number_accessibility_format, card.number),
            card.expiryDate.format()
        )
    }

    private fun LocalDate.format() = if (this.isExpired()) {
        TextViewModel.Resource(UiR.string.expired)
    } else {
        TextViewModel.Resource(
            UiR.string.expiry_date_format,
            this.toLocaleDateFormat()
        )
    }

    private fun LocalDate.toLocaleDateFormat(): String {
        val dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT)
        return this.format(dateTimeFormatter)
    }

    private fun LocalDate.isExpired() = this.isBefore(LocalDate.now())

}