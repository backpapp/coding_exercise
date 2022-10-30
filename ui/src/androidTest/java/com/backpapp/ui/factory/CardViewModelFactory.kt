package com.backpapp.ui.factory

import com.backpapp.gvttest.ui.model.CardType
import com.backpapp.gvttest.ui.model.CardViewModel
import com.backpapp.gvttest.ui.model.TextViewModel

object CardViewModelFactory {

    fun cardViewModel(
        id: String = "id",
        name: String = "name",
        cardNumber: String = "card number",
        cardType: CardType = CardType.PURPLE,
        contentDescription: List<TextViewModel> = listOf(),
        referenceNumber: TextViewModel? = TextViewModel.Text("reference number"),
        expiryDate: TextViewModel? = TextViewModel.Text("expiryDate"),
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