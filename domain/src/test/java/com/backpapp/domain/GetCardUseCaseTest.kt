package com.backpapp.domain

import app.cash.turbine.test
import com.backpapp.details.test.MainCoroutineListener
import com.backpapp.details.test.TestBehaviorSpec
import com.backpapp.gvttest.domain.GetCardUseCase
import com.backpapp.gvttest.domain.model.Card
import com.backpapp.gvttest.domain.repository.CardRepository
import com.backpapp.gvttest.domain.repository.Response
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf

class GetCardUseCaseTest : TestBehaviorSpec({

    val mainCoroutineListener = MainCoroutineListener()
    listeners(mainCoroutineListener)

    val card: Card = mockk()

    val repository: CardRepository = mockk()
    val cardId = "cardId"
    val usecase = GetCardUseCase(repository)

    Given("repository returns success") {
        coEvery { repository.getCard(cardId, true) } returns flowOf(Response.Success(card))

        When("invoke usecase with refresh") {
            val result = usecase(cardId, refresh = true)

            Then("result is Success") {
                result.test {
                    awaitItem() shouldBe GetCardUseCase.Result.Success(card)
                    awaitComplete()
                }
            }
        }

        When("invoke usecase with refresh") {
            val result = usecase(cardId, refresh = true)

            Then("result is Success") {
                result.test {
                    awaitItem() shouldBe GetCardUseCase.Result.Success(card)
                    awaitComplete()
                }
            }
        }
    }

    Given("repository returns error") {
        coEvery { repository.getCard(cardId, false) } returns flowOf(Response.Error(Throwable()))

        When("invoke usecase with refresh") {
            val result = usecase(cardId, refresh = false)

            Then("result is Error") {
                result.test {
                    awaitItem() shouldBe GetCardUseCase.Result.Error
                    awaitComplete()
                }
            }
        }

        When("invoke usecase with refresh") {
            val result = usecase(cardId, refresh = false)

            Then("result is Success") {
                result.test {
                    awaitItem() shouldBe GetCardUseCase.Result.Error
                    awaitComplete()
                }
            }
        }
    }
})