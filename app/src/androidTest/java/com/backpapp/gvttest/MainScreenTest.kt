package com.backpapp.gvttest

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.backpapp.gvttest.navigation.NavigationEvent
import com.backpapp.gvttest.navigation.Navigator
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<TestActivity>()

    private val navigator = Navigator()
    private lateinit var navController: TestNavHostController

    @Before
    fun setupAppNavHost() {
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            MainScreen(navController = navController, navigator = navigator)
        }
    }

    @Test
    fun testNavigation() {
        composeTestRule.onNode(hasTestTag("login")).assertIsDisplayed()

        composeTestRule.runOnUiThread {
            navigator.navigateTo(NavigationEvent.Screen.List)
        }
        composeTestRule.onNode(hasTestTag("list")).assertIsDisplayed()
        navController.backStack.forEach {
            assertNotEquals(it.destination.route, NavigationEvent.Screen.Login.route)
        }

        composeTestRule.runOnUiThread {
            navigator.navigateTo(NavigationEvent.Screen.Detail("cardId"))
        }
        composeTestRule.onNode(hasTestTag("details")).assertIsDisplayed()
        val cardIdParam = navController.currentDestination?.route
        assert(cardIdParam!!.contains("cardId"))
    }

}