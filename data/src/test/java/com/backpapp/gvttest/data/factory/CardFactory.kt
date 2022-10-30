package com.backpapp.gvttest.data.factory

import com.backpapp.gvttest.domain.model.Card
import java.time.LocalDate

object CardFactory {
    fun blueCard(
        id: String = "id",
        title: String = "title",
        name: String = "name",
        number: String = "number"
    ) = Card.Blue(id, title, name, number)

    fun greenCard(
        id: String = "id",
        title: String = "title",
        name: String = "name",
        number: String = "number",
        expiryDate: LocalDate = LocalDate.of(2024, 11, 10)
    ) = Card.Green(id, title, name, expiryDate, number)

    fun purpleCard(
        id: String = "id",
        title: String = "title",
        firstName: String = "firstName",
        surname: String = "surname",
        number: String = "number",
        referenceNumber: String = "referenceNumber",
        expiryDate: LocalDate = LocalDate.of(2024, 11, 10)
    ) = Card.Purple(id, title, firstName, surname, expiryDate, number, referenceNumber)

}