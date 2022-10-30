package com.backpapp.gvttest.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CardEntity(
    @PrimaryKey val id: String,
    val title: String,
    val name: String? = null,
    val firstName: String? = null,
    val surname: String? = null,
    val expiryDateAet: String? = null,
    val number: String,
    val referenceNumber: String? = null,
    val type: CardEntityType
)

enum class CardEntityType {
    PURPLE,
    GREEN,
    BLUE
}
