package com.backpapp.gvttest.details

import com.backpapp.details.test.MainCoroutineListener
import com.backpapp.details.test.TestBehaviorSpec
import com.backpapp.gvttest.domain.DeleteCardUseCase
import com.backpapp.gvttest.domain.GetCardUseCase
import com.backpapp.gvttest.domain.model.Card
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

class DetailsViewModelTest : TestBehaviorSpec({

    val mainCoroutineListener = MainCoroutineListener()
    listeners(mainCoroutineListener)

    val getCardUseCase: GetCardUseCase = mockk()
    val deleteCardUseCase: DeleteCardUseCase = mockk()
    val viewModelMapper: CardViewModelMapper = mockk()
    val navigator: Navigator = mockk(relaxed = true)

    val cardId = "cardId"

    Given("getCardUseCase returns success") {

        val viewModel =
            DetailsViewModel(getCardUseCase, deleteCardUseCase, viewModelMapper, navigator)
        val card: Card.Blue = mockk()
        val cardViewModel: CardViewModel = mockk()

        coEvery {
            getCardUseCase(
                cardId,
                false
            )
        } returns flowOf(GetCardUseCase.Result.Success(card))

        every {
            viewModelMapper.mapToViewModel(card, false)
        } returns cardViewModel


        When("screen starts") {

            viewModel.onStart("cardId")

            Then("it shows loading") {
                viewModel.viewState.shouldBe(DetailsViewModel.ViewState.Loading)
            }

            Then("it updates screen to loaded with cards") {
                mainCoroutineListener.advanceUntilIdle()

                viewModel.viewState.shouldBe(
                    DetailsViewModel.ViewState.Loaded(
                        cardViewModel,
                        DialogState.None
                    )
                )
            }

            And("user deletes card") {
                viewModel.onDelete(cardId)

                Then("it shows the dialog") {
                    (viewModel.viewState as DetailsViewModel.ViewState.Loaded).dialogState shouldBe DialogState.DeleteCard(
                        cardId
                    )
                }

                And("delete card is successful") {
                    coEvery {
                        deleteCardUseCase(cardId)
                    } returns flowOf(DeleteCardUseCase.Result.Success)

                    And("user presses delete confirm") {
                        viewModel.onDeleteConfirm(cardId)

                        mainCoroutineListener.advanceUntilIdle()

                        Then("it pops the back stack") {
                            verify {
                                navigator.navigateTo(NavigationEvent.PopBackStack)
                            }
                        }
                    }
                }

                And("delete card fails") {
                    coEvery {
                        deleteCardUseCase(cardId)
                    } returns flowOf(DeleteCardUseCase.Result.Error)

                    And("user presses delete confirm") {
                        viewModel.onDeleteConfirm(cardId)

                        mainCoroutineListener.advanceUntilIdle()

                        Then("it shows a generic error") {
                            (viewModel.viewState as DetailsViewModel.ViewState.Loaded).dialogState shouldBe DialogState.GenericError
                        }
                    }
                }
            }
        }
    }

    Given("getCardUseCase returns error") {

        val viewModel =
            DetailsViewModel(getCardUseCase, deleteCardUseCase, viewModelMapper, navigator)

        coEvery {
            getCardUseCase(
                cardId,
                false
            )
        } returns flowOf(GetCardUseCase.Result.Error)

        When("screen starts") {

            viewModel.onStart("cardId")

            Then("it shows loading") {
                viewModel.viewState.shouldBe(DetailsViewModel.ViewState.Loading)
            }

            Then("it updates screen to error") {
                mainCoroutineListener.advanceUntilIdle()

                viewModel.viewState.shouldBe(DetailsViewModel.ViewState.Error)
            }

            And("user retry to load the screen") {
                viewModel.onRetryError(cardId)

                mainCoroutineListener.advanceUntilIdle()

                Then("it retrieves the card") {
                    coVerify {
                        getCardUseCase.invoke(cardId = cardId, refresh = true)
                    }
                }
            }
        }
    }

    Given("user is on screen") {
        val viewModel =
            DetailsViewModel(getCardUseCase, deleteCardUseCase, viewModelMapper, navigator)

        When("user presses back") {
            viewModel.onBack()

            Then("it pops the back stack") {
                verify {
                    navigator.navigateTo(NavigationEvent.PopBackStack)
                }
            }
        }
    }
})