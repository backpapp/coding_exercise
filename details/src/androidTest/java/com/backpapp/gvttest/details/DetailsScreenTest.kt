package com.backpapp.gvttest.details

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.filter
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.performClick
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.testing.TestLifecycleOwner
import com.backpapp.gvttest.ui.model.DialogState
import com.backpapp.gvttest.ui.theme.GvtTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import com.backpapp.gvttest.ui.R as UiR
import com.backpapp.gvttest.view.details.R as detailsR

class DetailsScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private var clickOnBackCount = 0
    private var clickOnDeleteCount = 0
    private var clickOnRetryCount = 0
    private var clickOnConfirmCount = 0
    private var clickOnDismissCount = 0

    private var started = 0

    private val cardId: String = "cardId"
    private val onStart: (String) -> Unit = {
        started++
    }
    private val onBack: () -> Unit = {
        clickOnBackCount++
    }
    private val onRetryError: (String) -> Unit = {
        clickOnRetryCount++
    }
    private val onDelete: (String) -> Unit = {
        clickOnDeleteCount++
    }
    private val onDeleteConfirm: (String?) -> Unit = {
        clickOnConfirmCount++
    }
    private val onDismissDialog: () -> Unit = {
        clickOnDismissCount++
    }
    private val lifecycleOwner: LifecycleOwner = TestLifecycleOwner()

    private val loadingScreen = composeTestRule.onNode(hasTestTag("loading"))
    private val contentScreen = composeTestRule.onNode(hasTestTag("loaded"))
    private val errorScreen = composeTestRule.onNode(hasTestTag("error"))
    private val backButton = composeTestRule.onNode(hasTestTag("back"))
    private val deleteButton = composeTestRule.onNode(hasTestTag("delete"))
    private val retryButton = composeTestRule.onNode(hasTestTag("retry"))
    private val cardView = composeTestRule.onNode(hasTestTag("card"))
    private val qrCode = composeTestRule.onNode(hasTestTag("qrCode"))

    @Test
    fun testLoadingState() {
        val viewState = DetailsViewModel.ViewState.Loading

        initScreen(viewState)

        backButton.assertIsDisplayed()
        backButton.performClick()

        deleteButton.assertIsDisplayed()
        deleteButton.performClick()

        assertEquals(1, clickOnDeleteCount)
        assertEquals(1, clickOnBackCount)
        assertEquals(1, started)

        loadingScreen.assertIsDisplayed()
        contentScreen.assertDoesNotExist()
        errorScreen.assertDoesNotExist()
    }

    @Test
    fun testLoadedScreen() {
        val viewState = DetailsViewStateFactory.viewStateLoaded()

        initScreen(viewState)

        backButton.assertIsDisplayed()
        backButton.performClick()

        deleteButton.assertIsDisplayed()
        deleteButton.performClick()

        val descriptionText = composeTestRule.activity.getString(detailsR.string.description)
        val description = composeTestRule.onNode(hasText(descriptionText))
        val qrCodeContentDescription = composeTestRule.activity.getString(detailsR.string.qr_code)

        description.assertIsDisplayed()
        cardView.assertIsDisplayed()
        qrCode.assertIsDisplayed()
        qrCode.onChildren().filter(hasContentDescription(qrCodeContentDescription)).onFirst()
            .assertIsDisplayed()

        assertEquals(1, clickOnBackCount)
        assertEquals(1, clickOnDeleteCount)
    }

    @Test
    fun testGenericErrorDialog() {
        val viewState =
            DetailsViewStateFactory.viewStateLoaded(dialogState = DialogState.GenericError)

        initScreen(viewState)

        with(composeTestRule.activity) {
            val dialogText = getString(UiR.string.generic_error)
            val confirmButtonText = getString(UiR.string.dismiss)

            composeTestRule.onNode(hasText(dialogText)).assertIsDisplayed()
            composeTestRule.onNode(hasText(confirmButtonText)).assertIsDisplayed()
            composeTestRule.onNode(hasText(confirmButtonText)).performClick()
            assertEquals(1, clickOnDismissCount)

        }
    }

    @Test
    fun testDeleteDialog() {
        val viewState =
            DetailsViewStateFactory.viewStateLoaded(dialogState = DialogState.DeleteCard(""))

        initScreen(viewState)

        with(composeTestRule.activity) {
            val dialogText = getString(UiR.string.delete_dialog_text)
            val confirmButtonText = getString(UiR.string.delete)
            val dismissButtonText = getString(UiR.string.cancel)

            composeTestRule.onNode(hasText(dialogText)).assertIsDisplayed()
            composeTestRule.onNode(hasText(confirmButtonText)).assertIsDisplayed()
            composeTestRule.onNode(hasText(dismissButtonText)).assertIsDisplayed()

            composeTestRule.onNode(hasText(confirmButtonText)).performClick()
            assertEquals(1, clickOnConfirmCount)

            composeTestRule.onNode(hasText(dismissButtonText)).performClick()
            assertEquals(1, clickOnDismissCount)
        }
    }

    @Test
    fun testErrorScreen() {
        val viewState = DetailsViewModel.ViewState.Error

        initScreen(viewState)

        backButton.assertIsDisplayed()
        backButton.performClick()

        deleteButton.assertIsDisplayed()
        deleteButton.performClick()

        retryButton.assertIsDisplayed()
        retryButton.performClick()

        assertEquals(1, clickOnDeleteCount)
        assertEquals(1, clickOnBackCount)
        assertEquals(1, started)
        assertEquals(1, clickOnRetryCount)

        loadingScreen.assertDoesNotExist()
        contentScreen.assertDoesNotExist()
        errorScreen.assertIsDisplayed()
    }

    private fun initScreen(uiState: DetailsViewModel.ViewState) {
        composeTestRule.setContent {
            GvtTheme {
                DetailsScreen(
                    cardId = cardId,
                    uiState = uiState,
                    onStart = onStart,
                    onBack = onBack,
                    onRetryError = onRetryError,
                    onDelete = onDelete,
                    onDeleteConfirm = onDeleteConfirm,
                    onDismissDialog = onDismissDialog,
                    lifecycleOwner
                )
            }
        }
    }
}
