package com.backpapp.login

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.backpapp.gvttest.ui.model.TextFieldViewModel
import com.backpapp.gvttest.ui.theme.GvtTheme
import com.backpapp.gvttest.view.login.LoginScreen
import com.backpapp.gvttest.view.login.LoginViewModel
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import com.backpapp.gvttest.view.login.R as LoginR

class LoginScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private var onEmailUpdateCount = 0
    private var onPasswordUpdateCount = 0
    private var onSubmitClickCount = 0
    private var onPasswordVisibleClickCount = 0

    private val onEmailUpdate: (String) -> Unit = {
        onEmailUpdateCount++
    }
    private val onPasswordUpdate: (String) -> Unit = {
        onPasswordUpdateCount++
    }
    private val onSubmitClick: () -> Unit = {
        onSubmitClickCount++
    }
    private val onPasswordVisibleClick: () -> Unit = {
        onPasswordVisibleClickCount++
    }

    private val loginNode = composeTestRule.onNode(hasText("login"))
    private val passwordNode = composeTestRule.onNode(hasText("password"))
    private val passwordVisibleButton = composeTestRule.onNode(hasTestTag("passwordVisibility"))
    private val submitButton = composeTestRule.onNode(hasTestTag("submit"))

    @Test
    fun testLoadedState() {
        val viewState = LoginViewModel.ViewState.Loaded(
            TextFieldViewModel("login", isError = true),
            TextFieldViewModel("password", isError = true),
            isPasswordVisible = true
        )

        initScreen(viewState)

        loginNode.assertIsDisplayed()
        passwordNode.assertIsDisplayed()


        with(composeTestRule.activity) {
            val loginError = getString(LoginR.string.login_empty_error)
            val passwordError = getString(LoginR.string.password_empty_error)

            composeTestRule.onNode(hasText(loginError)).assertIsDisplayed()
            composeTestRule.onNode(hasText(passwordError)).assertIsDisplayed()
        }

        passwordVisibleButton.performClick()
        Assert.assertEquals(1, onPasswordVisibleClickCount)

        submitButton.performClick()
        Assert.assertEquals(1, onSubmitClickCount)

        loginNode.performTextInput("1")
        Assert.assertEquals(1, onEmailUpdateCount)

        passwordNode.performTextInput("1")
        Assert.assertEquals(1, onPasswordUpdateCount)
    }

    private fun initScreen(uiState: LoginViewModel.ViewState.Loaded) {
        composeTestRule.setContent {
            GvtTheme {
                LoginScreen(
                    uiState,
                    onEmailUpdate,
                    onPasswordUpdate,
                    onSubmitClick,
                    onPasswordVisibleClick
                )
            }
        }
    }
}