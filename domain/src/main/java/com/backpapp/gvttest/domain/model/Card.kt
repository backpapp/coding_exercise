package com.backpapp.gvttest.domain.model

import java.time.LocalDate

sealed class Card(open val id: String) {
    data class Blue(
        override val id: String,
        val title: String,
        val name: String,
        val number: String
    ) : Card(id)

    data class Green(
        override val id: String,
        val title: String,
        val name: String,
        val expiryDate: LocalDate,
        val number: String
    ) : Card(id)

    data class Purple(
        override val id: String,
        val title: String,
        val firstName: String,
        val surname: String,
        val expiryDate: LocalDate,
        val number: String,
        val referenceNumber: String
    ) : Card(id)
}