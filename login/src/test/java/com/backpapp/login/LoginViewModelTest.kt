package com.backpapp.login

import com.backpapp.details.test.MainCoroutineListener
import com.backpapp.details.test.TestBehaviorSpec
import com.backpapp.gvttest.domain.LoginUseCase
import com.backpapp.gvttest.navigation.NavigationEvent
import com.backpapp.gvttest.navigation.Navigator
import com.backpapp.gvttest.view.login.LoginViewModel
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf

class LoginViewModelTest : TestBehaviorSpec({

    val mainCoroutineListener = MainCoroutineListener()
    listeners(mainCoroutineListener)

    val navigator: Navigator = mockk()
    val loginUseCase: LoginUseCase = mockk()

    Given("login returns success") {
        every { loginUseCase("userName", "password") } returns flowOf(LoginUseCase.Result.Success)

        val viewModel = LoginViewModel(navigator, loginUseCase)

        When("user submits with userName and password") {
            viewModel.updateUserName("userName")
            viewModel.updatePassword("password")
            viewModel.onSubmitClick()

            mainCoroutineListener.advanceUntilIdle()

            Then("it navigates to list screen") {
                verify {
                    navigator.navigateTo(NavigationEvent.Screen.List)
                }
            }
        }
    }

    Given("login returns error") {
        every { loginUseCase(any(), any()) } returns flowOf(
            LoginUseCase.Result.Error(
                listOf(
                    LoginUseCase.ErrorType.UserNameEmpty, LoginUseCase.ErrorType.PasswordEmpty
                )
            )
        )

        val viewModel = LoginViewModel(navigator, loginUseCase)

        When("user submits") {

            viewModel.onSubmitClick()

            mainCoroutineListener.advanceUntilIdle()

            Then("it shows errors") {
                viewModel.viewState.userName.isError shouldBe true
                viewModel.viewState.password.isError shouldBe true
            }
        }
    }
})