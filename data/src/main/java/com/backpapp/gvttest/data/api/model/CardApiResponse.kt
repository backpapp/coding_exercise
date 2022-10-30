package com.backpapp.gvttest.data.api.model


sealed class CardApiResponse(open val id: String) {

    companion object {
        const val DATE_FORMAT = "yyyy-MM-dd"
    }

    data class Blue(
        override val id: String,
        val title: String,
        val name: String,
        val number: String
    ) : CardApiResponse(id)

    data class Green(
        override val id: String,
        val title: String,
        val name: String,
        val expiryDateAet: String,
        val number: String
    ) : CardApiResponse(id)

    data class Purple(
        override val id: String,
        val title: String,
        val firstName: String,
        val surname: String,
        val expiryDateAet: String,
        val number: String,
        val referenceNumber: String
    ) : CardApiResponse(id)
}