package com.backpapp.gvttest.data.factory

import com.backpapp.gvttest.data.db.CardEntity
import com.backpapp.gvttest.data.db.CardEntityType

object CardEntityFactory {

    fun createEntity(
        id: String = "id",
        title: String = "title",
        name: String? = "name",
        firstName: String? = "firstName",
        surname: String? = "surname",
        expiryDateAet: String? = "10-11-24",
        number: String = "number",
        referenceNumber: String? = "referenceNumber",
        type: CardEntityType = CardEntityType.GREEN
    ) = CardEntity(
        id,
        title,
        name,
        firstName,
        surname,
        expiryDateAet,
        number,
        referenceNumber,
        type
    )
}