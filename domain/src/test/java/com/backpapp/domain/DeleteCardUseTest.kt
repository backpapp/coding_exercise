package com.backpapp.domain

import app.cash.turbine.test
import com.backpapp.details.test.MainCoroutineListener
import com.backpapp.details.test.TestBehaviorSpec
import com.backpapp.gvttest.domain.DeleteCardUseCase
import com.backpapp.gvttest.domain.repository.CardRepository
import com.backpapp.gvttest.domain.repository.Response
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf

class DeleteCardUseTest : TestBehaviorSpec({

    val mainCoroutineListener = MainCoroutineListener()
    listeners(mainCoroutineListener)

    val repository: CardRepository = mockk()

    val usecase = DeleteCardUseCase(repository)

    Given("repository returns success") {
        coEvery { repository.deleteCard("cardId") } returns flowOf(Response.Success(null))

        When("invoke usecase") {
            val result = usecase("cardId")

            Then("result is Success") {
                result.test {
                    awaitItem() shouldBe DeleteCardUseCase.Result.Success
                    awaitComplete()
                }
            }
        }
    }

    Given("repository returns error") {
        coEvery { repository.deleteCard("cardId") } returns flowOf(Response.Error<Unit>(Throwable()))

        When("invoke usecase") {
            val result = usecase("cardId")

            Then("result is Error") {
                result.test {
                    awaitItem() shouldBe DeleteCardUseCase.Result.Error
                    awaitComplete()
                }
            }
        }
    }
})