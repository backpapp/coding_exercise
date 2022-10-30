package com.backpapp.domain

import app.cash.turbine.test
import com.backpapp.details.test.MainCoroutineListener
import com.backpapp.details.test.TestBehaviorSpec
import com.backpapp.gvttest.domain.LoginUseCase
import io.kotest.matchers.shouldBe

class LoginUseCaseTest : TestBehaviorSpec({

    val mainCoroutineListener = MainCoroutineListener()
    listeners(mainCoroutineListener)

    val usecase = LoginUseCase()

    Given("login and password are not empty") {
        val login = "login"
        val password = "password"

        When("invoke usecase") {
            val result = usecase(login, password)

            Then("result is success") {
                result.test {
                    awaitItem() shouldBe LoginUseCase.Result.Success
                    awaitComplete()
                }
            }
        }
    }

    Given("login is empty") {
        val login = ""
        val password = "password"

        When("invoke usecase") {
            val result = usecase(login, password)

            Then("result is success") {
                result.test {
                    awaitItem() shouldBe LoginUseCase.Result.Error(listOf(LoginUseCase.ErrorType.UserNameEmpty))
                    awaitComplete()
                }
            }
        }
    }

    Given("password is empty") {
        val login = "login"
        val password = ""

        When("invoke usecase") {
            val result = usecase(login, password)

            Then("result is success") {
                result.test {
                    awaitItem() shouldBe LoginUseCase.Result.Error(listOf(LoginUseCase.ErrorType.PasswordEmpty))
                    awaitComplete()
                }
            }
        }
    }

    Given("login and password are empty") {
        val login = ""
        val password = ""

        When("invoke usecase") {
            val result = usecase(login, password)

            Then("result is success") {
                result.test {
                    awaitItem() shouldBe LoginUseCase.Result.Error(
                        listOf(
                            LoginUseCase.ErrorType.UserNameEmpty,
                            LoginUseCase.ErrorType.PasswordEmpty
                        )
                    )
                    awaitComplete()
                }
            }
        }
    }
})