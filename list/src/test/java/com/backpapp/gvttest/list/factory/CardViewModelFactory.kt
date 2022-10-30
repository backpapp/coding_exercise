package com.backpapp.gvttest.list.factory

import com.backpapp.gvttest.ui.model.CardType
import com.backpapp.gvttest.ui.model.CardViewModel
import com.backpapp.gvttest.ui.model.TextViewModel

object CardViewModelFactory {

    fun createViewModel(
        id: String = "id",
        name: String = "name",
        cardNumber: String = "cardNumber",
        cardType: CardType = CardType.PURPLE,
        contentDescription: List<TextViewModel> = listOf(),
        referenceNumber: TextViewModel? = null,
        expiryDate: TextViewModel? = null,
        isExpired: Boolean = false,
        showDelete: Boolean = true
    ) = CardViewModel(
        id,
        name,
        cardNumber,
        cardType,
        contentDescription,
        referenceNumber,
        expiryDate,
        isExpired,
        showDelete
    )
}