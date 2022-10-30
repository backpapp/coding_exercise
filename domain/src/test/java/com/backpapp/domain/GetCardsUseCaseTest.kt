package com.backpapp.domain

import app.cash.turbine.test
import com.backpapp.details.test.MainCoroutineListener
import com.backpapp.details.test.TestBehaviorSpec
import com.backpapp.gvttest.domain.GetCardsUseCase
import com.backpapp.gvttest.domain.model.Card
import com.backpapp.gvttest.domain.repository.CardRepository
import com.backpapp.gvttest.domain.repository.Response
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf

class GetCardsUseCaseTest : TestBehaviorSpec({

    val mainCoroutineListener = MainCoroutineListener()
    listeners(mainCoroutineListener)

    val card: Card = mockk()
    val cards = listOf(card)

    val repository: CardRepository = mockk()
    val usecase = GetCardsUseCase(repository)

    Given("repository returns success") {
        coEvery { repository.getCards(true) } returns flowOf(Response.Success(cards))

        When("invoke usecase with refresh") {
            val result = usecase(refresh = true)

            Then("result is Success") {
                result.test {
                    awaitItem() shouldBe GetCardsUseCase.Result.Success(cards)
                    awaitComplete()
                }
            }
        }

        When("invoke usecase with refresh") {
            val result = usecase(refresh = true)

            Then("result is Success") {
                result.test {
                    awaitItem() shouldBe GetCardsUseCase.Result.Success(cards)
                    awaitComplete()
                }
            }
        }
    }

    Given("repository returns error") {
        coEvery { repository.getCards(false) } returns flowOf(Response.Error(Throwable()))

        When("invoke usecase with refresh") {
            val result = usecase(refresh = false)

            Then("result is Error") {
                result.test {
                    awaitItem() shouldBe GetCardsUseCase.Result.Error
                    awaitComplete()
                }
            }
        }

        When("invoke usecase with refresh") {
            val result = usecase(refresh = false)

            Then("result is Success") {
                result.test {
                    awaitItem() shouldBe GetCardsUseCase.Result.Error
                    awaitComplete()
                }
            }
        }
    }
})