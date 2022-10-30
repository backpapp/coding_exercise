package com.backpapp.gvttest.navigation

sealed class NavigationEvent {
    object PopBackStack : NavigationEvent()
    sealed class Screen(val route: String, val popupTo: Screen? = null) : NavigationEvent() {
        object Login : Screen("Login")
        object List : Screen("List", popupTo = Login)
        data class Detail constructor(val cardId: String) :
            Screen(ROUTE.replace("{$CARD_ID}", cardId)) {
            companion object {
                const val CARD_ID = "cardId"
                const val ROUTE = "Detail/{cardId}"
            }
        }
    }
}

