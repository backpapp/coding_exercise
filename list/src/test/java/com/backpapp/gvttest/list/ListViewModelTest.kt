package com.backpapp.gvttest.list

import com.backpapp.details.test.MainCoroutineListener
import com.backpapp.details.test.TestBehaviorSpec
import com.backpapp.gvttest.domain.DeleteCardUseCase
import com.backpapp.gvttest.domain.GetCardUseCase
import com.backpapp.gvttest.domain.GetCardsUseCase
import com.backpapp.gvttest.domain.model.Card
import com.backpapp.gvttest.list.factory.CardViewModelFactory
import com.backpapp.gvttest.navigation.NavigationEvent
import com.backpapp.gvttest.navigation.Navigator
import com.backpapp.gvttest.ui.mapper.CardViewModelMapper
import com.backpapp.gvttest.ui.model.CardViewModel
import com.backpapp.gvttest.ui.model.DialogState
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDate

class ListViewModelTest : TestBehaviorSpec({

    val mainCoroutineListener = MainCoroutineListener()
    listeners(mainCoroutineListener)

    val getCardsUseCase: GetCardsUseCase = mockk()
    val getCardUseCase: GetCardUseCase = mockk()
    val deleteCardUseCase: DeleteCardUseCase = mockk()
    val navigator: Navigator = mockk()
    val viewModelMapper: CardViewModelMapper = mockk()

    Given("getCardUseCase returns success") {
        val viewModel =
            ListViewModel(
                getCardsUseCase,
                getCardUseCase,
                deleteCardUseCase,
                navigator,
                viewModelMapper
            )
        val cardA: Card.Blue = mockk()
        val cardB: Card.Blue = mockk()
        val cards = listOf(cardA, cardB)
        val cardAId = "cardA"
        val cardBId = "cardB"
        val cardCId = "cardC"

        val cardViewModelA: CardViewModel =
            CardViewModelFactory.createViewModel(id = cardAId, name = cardAId)
        val cardViewModelB: CardViewModel =
            CardViewModelFactory.createViewModel(id = cardBId, name = cardBId)
        val cardViewModelC: CardViewModel =
            CardViewModelFactory.createViewModel(id = cardCId, name = cardCId)

        val cardViewModels = listOf(cardViewModelA, cardViewModelB, cardViewModelC)

        coEvery {
            getCardsUseCase(
                false
            )
        } returns flowOf(GetCardsUseCase.Result.Success(cards))

        coEvery {
            getCardsUseCase(
                true
            )
        } returns flowOf(GetCardsUseCase.Result.Success(cards))

        every {
            viewModelMapper.mapToViewModel(cards, true)
        } returns cardViewModels

        When("screen starts") {
            viewModel.onStart()

            Then("it shows loading") {
                viewModel.viewState.shouldBe(ListViewModel.ViewState.Loading)
            }

            Then("it updates screen to loaded with cards") {
                mainCoroutineListener.advanceUntilIdle()

                viewModel.viewState.shouldBe(
                    ListViewModel.ViewState.Loaded(
                        cardViewModels,
                        ListViewModel.Sorting.ASC,
                        DialogState.None,
                        null
                    )
                )
            }

            And("user deletes card") {
                viewModel.onDelete(cardAId)

                Then("it shows the dialog") {
                    (viewModel.viewState as ListViewModel.ViewState.Loaded).dialogState shouldBe DialogState.DeleteCard(
                        cardAId
                    )
                }

                And("delete card is successful") {
                    coEvery {
                        deleteCardUseCase(cardAId)
                    } returns flowOf(DeleteCardUseCase.Result.Success)

                    And("user presses delete confirm") {
                        viewModel.onDeleteConfirm(cardAId)

                        mainCoroutineListener.advanceUntilIdle()

                        Then("card is removed from the state") {
                            (viewModel.viewState as ListViewModel.ViewState.Loaded).cards shouldBe listOf(
                                cardViewModelB, cardViewModelC
                            )
                        }

                        And("user presses reset") {
                            viewModel.onReset()

                            mainCoroutineListener.advanceUntilIdle()

                            Then("cards are restored from the repo") {
                                (viewModel.viewState as ListViewModel.ViewState.Loaded).cards shouldBe listOf(
                                    cardViewModelA, cardViewModelB, cardViewModelC
                                )
                            }
                        }
                    }
                }

                And("delete card fails") {
                    coEvery {
                        deleteCardUseCase(cardBId)
                    } returns flowOf(DeleteCardUseCase.Result.Error)

                    And("user presses delete confirm") {
                        viewModel.onDeleteConfirm(cardBId)

                        mainCoroutineListener.advanceUntilIdle()

                        Then("it shows a toast generic error") {
                            (viewModel.viewState as ListViewModel.ViewState.Loaded).snackBar shouldBe ListViewModel.SnackBar.GenericError
                        }
                        And("it restores original state") {
                            (viewModel.viewState as ListViewModel.ViewState.Loaded).cards shouldBe listOf(
                                cardViewModelA, cardViewModelB, cardViewModelC
                            )
                        }
                        And("it dismisses dialog") {
                            (viewModel.viewState as ListViewModel.ViewState.Loaded).dialogState shouldBe DialogState.None
                        }
                    }
                }
            }
        }
    }

    Given("getCardUseCase returns error") {

        val viewModel =
            ListViewModel(
                getCardsUseCase,
                getCardUseCase,
                deleteCardUseCase,
                navigator,
                viewModelMapper
            )

        coEvery {
            getCardsUseCase(
                false
            )
        } returns flowOf(GetCardsUseCase.Result.Error)

        When("screen starts") {

            viewModel.onStart()

            Then("it shows loading") {
                viewModel.viewState.shouldBe(ListViewModel.ViewState.Loading)
            }

            Then("it updates screen to error") {
                mainCoroutineListener.advanceUntilIdle()

                viewModel.viewState.shouldBe(ListViewModel.ViewState.Error)
            }

            And("user retry to load the screen") {
                viewModel.onRetryError()

                mainCoroutineListener.advanceUntilIdle()

                Then("it retrieves the card") {
                    coVerify {
                        getCardsUseCase.invoke(refresh = true)
                    }
                }
            }
        }
    }

    Given("user is on screen") {
        val viewModel =
            ListViewModel(
                getCardsUseCase,
                getCardUseCase,
                deleteCardUseCase,
                navigator,
                viewModelMapper
            )

        val cardAId = "cardA"
        val cardBId = "cardB"
        val cardCId = "cardC"

        val cardA: Card.Blue = mockk {
            every { id } returns cardAId
        }
        val cardB: Card.Green = mockk {
            every { id } returns cardBId
            every { expiryDate } returns LocalDate.of(3000, 1, 1)
        }
        val cardC: Card.Green = mockk {
            every { id } returns cardCId
            every { expiryDate } returns LocalDate.of(1999, 1, 1)
        }
        val cards = listOf(cardA, cardB, cardC)

        val cardViewModelA: CardViewModel =
            CardViewModelFactory.createViewModel(id = cardAId, name = cardAId)
        val cardViewModelB: CardViewModel =
            CardViewModelFactory.createViewModel(id = cardBId, name = cardBId)
        val cardViewModelC: CardViewModel =
            CardViewModelFactory.createViewModel(id = cardCId, name = cardCId)

        val cardViewModels = listOf(cardViewModelA, cardViewModelB, cardViewModelC)

        coEvery {
            getCardsUseCase(
                false
            )
        } returns flowOf(GetCardsUseCase.Result.Success(cards))

        every {
            viewModelMapper.mapToViewModel(cards, true)
        } returns cardViewModels

        viewModel.onStart()

        mainCoroutineListener.advanceUntilIdle()

        When("user presses on reorder") {
            (viewModel.viewState as ListViewModel.ViewState.Loaded).cards shouldBe listOf(
                cardViewModelA,
                cardViewModelB,
                cardViewModelC
            )

            viewModel.onReorder()

            Then("it shows list in reverse") {
                (viewModel.viewState as ListViewModel.ViewState.Loaded).cards shouldBe listOf(
                    cardViewModelC,
                    cardViewModelB,
                    cardViewModelA
                )

                (viewModel.viewState as ListViewModel.ViewState.Loaded).sorting shouldBe ListViewModel.Sorting.DSC
            }
            And("it shows list in original order") {
                viewModel.onReorder()

                (viewModel.viewState as ListViewModel.ViewState.Loaded).cards shouldBe listOf(
                    cardViewModelA,
                    cardViewModelB,
                    cardViewModelC
                )

                (viewModel.viewState as ListViewModel.ViewState.Loaded).sorting shouldBe ListViewModel.Sorting.ASC
            }
        }

        When("user presses on card") {

            And("cardUseCase returns success") {

                coEvery {
                    getCardUseCase(
                        cardAId,
                        false
                    )
                } returns flowOf(GetCardUseCase.Result.Success(cardA))

                viewModel.onCardClick(cardAId)

                mainCoroutineListener.advanceUntilIdle()

                Then("it navigates to card details") {
                    verify {
                        navigator.navigateTo(NavigationEvent.Screen.Detail(cardAId))
                    }
                }
            }

            And("cardUseCase returns error") {
                coEvery {
                    getCardUseCase(
                        cardAId,
                        false
                    )
                } returns flowOf(GetCardUseCase.Result.Error)

                viewModel.onCardClick(cardAId)

                mainCoroutineListener.advanceUntilIdle()

                Then("it shows a generic error snackbar") {
                    (viewModel.viewState as ListViewModel.ViewState.Loaded).snackBar shouldBe ListViewModel.SnackBar.GenericError
                }
            }

            And("card is not expired") {
                coEvery {
                    getCardUseCase(
                        cardBId,
                        false
                    )
                } returns flowOf(GetCardUseCase.Result.Success(cardB))

                viewModel.onCardClick(cardBId)

                mainCoroutineListener.advanceUntilIdle()

                Then("it navigates to card details") {
                    verify {
                        navigator.navigateTo(NavigationEvent.Screen.Detail(cardAId))
                    }
                }
            }

            And("card is expired") {
                coEvery {
                    getCardUseCase(
                        cardCId,
                        false
                    )
                } returns flowOf(GetCardUseCase.Result.Success(cardC))

                viewModel.onCardClick(cardCId)

                mainCoroutineListener.advanceUntilIdle()

                Then("it shows a details error snackbar") {
                    (viewModel.viewState as ListViewModel.ViewState.Loaded).snackBar shouldBe ListViewModel.SnackBar.DetailsError
                }
            }
        }
    }
})