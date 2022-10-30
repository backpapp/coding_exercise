package com.backpapp.gvttest.ui.model

sealed class DialogState {
    object None : DialogState()
    data class DeleteCard(val id: String) : DialogState()
    object GenericError : DialogState()
}
