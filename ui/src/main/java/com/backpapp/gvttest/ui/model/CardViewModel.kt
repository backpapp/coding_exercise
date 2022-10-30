package com.backpapp.gvttest.ui.model

data class CardViewModel(
    val id: String,
    val name: String,
    val cardNumber: String,
    val cardType: CardType,
    val contentDescription: List<TextViewModel>,
    val referenceNumber: TextViewModel? = null,
    val expiryDate: TextViewModel? = null,
    val isExpired: Boolean = false,
    val showDelete: Boolean = true
)

enum class CardType {
    PURPLE,
    BLUE,
    GREEN
}