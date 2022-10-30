package com.backpapp.gvttest.list

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.testing.TestLifecycleOwner
import com.backpapp.gvttest.list.factory.CardViewModelFactory
import com.backpapp.gvttest.ui.R
import com.backpapp.gvttest.ui.model.DialogState
import com.backpapp.gvttest.ui.theme.GvtTheme
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import com.backpapp.gvttest.view.list.R as ListR

class ListScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private var startedCount = 0
    private var retryCount = 0
    private var onDeleteCount = 0
    private var onDialogConfirmCount = 0
    private var onResetCount = 0
    private var onReorderCount = 0
    private var onCardClickCount = 0
    private var onDismissDialogCount = 0
    private var onDismissSnackBarCount = 0

    private val onStart: () -> Unit = {
        startedCount++
    }
    private val onRetryError: () -> Unit = {
        retryCount++
    }

    private val onDelete: (String) -> Unit = {
        onDeleteCount++
    }
    private val onDeleteConfirm: (String?) -> Unit = {
        onDialogConfirmCount++
    }
    private val onReset: () -> Unit = {
        onResetCount++
    }
    private val onReorder: () -> Unit = {
        onReorderCount++
    }
    private val onCardClick: (String) -> Unit = {
        onCardClickCount++
    }
    private val onDismissDialog: () -> Unit = {
        onDismissDialogCount++
    }
    private val onDismissSnackBar: () -> Unit = {
        onDismissSnackBarCount++
    }

    private val lifecycleOwner: LifecycleOwner = TestLifecycleOwner()

    private val loadingScreen = composeTestRule.onNode(hasTestTag("loading"))
    private val contentScreen = composeTestRule.onNode(hasTestTag("loaded"))
    private val errorScreen = composeTestRule.onNode(hasTestTag("error"))
    private val swapIcon = composeTestRule.onNode(hasTestTag("swap"))

    private val restoreIcon = composeTestRule.onNode(hasTestTag("restore"))
    private val retryButton = composeTestRule.onNode(hasTestTag("retry"))

    @Test
    fun testLoadingState() {
        val viewState = ListViewModel.ViewState.Loading

        initScreen(viewState)

        Assert.assertEquals(1, startedCount)

        loadingScreen.assertIsDisplayed()
        contentScreen.assertDoesNotExist()
        errorScreen.assertDoesNotExist()
    }

    @Test
    fun testLoadedState() {
        val viewState = ListViewModel.ViewState.Loaded(
            listOf(
                CardViewModelFactory.createViewModel(id = "id1"),
                CardViewModelFactory.createViewModel(id = "id2")
            )
        )

        initScreen(viewState)

        Assert.assertEquals(1, startedCount)

        contentScreen.assertIsDisplayed()
        loadingScreen.assertDoesNotExist()
        errorScreen.assertDoesNotExist()

        //Test list is properly sorted but I still to learn about compose test :/
        //composeTestRule.onNode(hasTestTag("cards")).onChildren().filter(hasTestTag("name"))

        swapIcon.performClick()
        Assert.assertEquals(1, onReorderCount)

        restoreIcon.performClick()
        Assert.assertEquals(1, onResetCount)
    }

    @Test
    fun testGenericErrorDialog() {
        val viewState =
            ListViewModel.ViewState.Loaded(cards = listOf(), dialogState = DialogState.GenericError)

        initScreen(viewState)

        with(composeTestRule.activity) {
            val dialogText = getString(R.string.generic_error)
            val confirmButtonText = getString(R.string.dismiss)

            composeTestRule.onNode(hasText(dialogText)).assertIsDisplayed()
            composeTestRule.onNode(hasText(confirmButtonText)).assertIsDisplayed()

            composeTestRule.onNode(hasText(confirmButtonText)).performClick()
            Assert.assertEquals(1, onDismissDialogCount)
        }
    }

    @Test
    fun testDeleteDialog() {
        val viewState = ListViewModel.ViewState.Loaded(
            cards = listOf(),
            dialogState = DialogState.DeleteCard("")
        )

        initScreen(viewState)

        with(composeTestRule.activity) {
            val dialogText = getString(R.string.delete_dialog_text)
            val confirmButtonText = getString(R.string.delete)
            val dismissButtonText = getString(R.string.cancel)

            composeTestRule.onNode(hasText(dialogText)).assertIsDisplayed()
            composeTestRule.onNode(hasText(confirmButtonText)).assertIsDisplayed()
            composeTestRule.onNode(hasText(dismissButtonText)).assertIsDisplayed()

            composeTestRule.onNode(hasText(confirmButtonText)).performClick()
            Assert.assertEquals(1, onDialogConfirmCount)

            composeTestRule.onNode(hasText(dismissButtonText)).performClick()
            Assert.assertEquals(1, onDismissDialogCount)
        }
    }

    @Test
    fun testDetailsErrorSnackbar() {
        val viewState = ListViewModel.ViewState.Loaded(
            cards = listOf(),
            snackBar = ListViewModel.SnackBar.DetailsError
        )

        initScreen(viewState)

        with(composeTestRule.activity) {
            val snackBarText = getString(ListR.string.expired_error_text)

            composeTestRule.onNode(hasText(snackBarText)).assertIsDisplayed()
        }
    }

    @Test
    fun testGenericErrorSnackbar() {
        val viewState = ListViewModel.ViewState.Loaded(
            cards = listOf(),
            snackBar = ListViewModel.SnackBar.GenericError
        )

        initScreen(viewState)

        with(composeTestRule.activity) {
            val snackBarText = getString(R.string.generic_error)

            composeTestRule.onNode(hasText(snackBarText)).assertIsDisplayed()
        }
    }

    @Test
    fun testErrorScreen() {
        val viewState = ListViewModel.ViewState.Error

        initScreen(viewState)

        retryButton.assertIsDisplayed()
        retryButton.performClick()

        Assert.assertEquals(1, startedCount)
        Assert.assertEquals(1, retryCount)

        loadingScreen.assertDoesNotExist()
        contentScreen.assertDoesNotExist()
        errorScreen.assertIsDisplayed()
    }

    private fun initScreen(uiState: ListViewModel.ViewState) {
        composeTestRule.setContent {
            GvtTheme {
                ListScreen(
                    uiState,
                    onStart,
                    onRetryError,
                    onDelete,
                    onDeleteConfirm,
                    onReset,
                    onReorder,
                    onCardClick,
                    onDismissDialog,
                    onDismissSnackBar,
                    lifecycleOwner
                )
            }
        }
    }
}