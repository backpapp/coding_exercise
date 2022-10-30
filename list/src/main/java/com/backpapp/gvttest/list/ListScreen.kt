package com.backpapp.gvttest.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.backpapp.gvttest.ui.compose.CardItem
import com.backpapp.gvttest.ui.compose.DialogScreen
import com.backpapp.gvttest.ui.compose.ErrorScreen
import com.backpapp.gvttest.ui.compose.Icon
import com.backpapp.gvttest.ui.compose.LoadingScreen
import com.backpapp.gvttest.ui.compose.utils.FontScalePreviews
import com.backpapp.gvttest.ui.model.CardType
import com.backpapp.gvttest.ui.model.CardViewModel
import com.backpapp.gvttest.ui.model.TextViewModel
import com.backpapp.gvttest.ui.theme.GvtTheme
import com.backpapp.gvttest.ui.utils.stringViewModelContentDesc
import com.backpapp.gvttest.ui.R as UiR
import com.backpapp.gvttest.view.list.R as ListR

@Composable
fun ListScreen(
    uiState: ListViewModel.ViewState,
    onStart: () -> Unit,
    onRetryError: () -> Unit,
    onDelete: (String) -> Unit,
    onDeleteConfirm: (String?) -> Unit,
    onReset: () -> Unit,
    onReorder: () -> Unit,
    onCardClick: (String) -> Unit,
    onDismissDialog: () -> Unit,
    onDismissSnackBar: () -> Unit,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
) {
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                onStart()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Surface(Modifier.testTag("list")) {
        when (uiState) {
            is ListViewModel.ViewState.Loading -> LoadingScreen()
            is ListViewModel.ViewState.Loaded -> LoadedScreen(
                uiState,
                onReset,
                onReorder,
                onCardClick,
                onDelete,
                onDeleteConfirm,
                onDismissDialog,
                onDismissSnackBar
            )
            is ListViewModel.ViewState.Error -> ErrorScreen(onRetryError)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun LoadedScreen(
    uiState: ListViewModel.ViewState.Loaded,
    onReset: () -> Unit,
    onReorder: () -> Unit,
    onCardClick: (String) -> Unit,
    onDelete: (String) -> Unit,
    onDeleteConfirm: (String?) -> Unit,
    onDismissDialog: () -> Unit,
    onDismissSnackBar: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val genericErrorMessage = stringResource(UiR.string.generic_error)
    val expiredErrorMessage = stringResource(ListR.string.expired_error_text)

    if (uiState.snackBar != null) {
        LaunchedEffect(uiState.snackBar) {
            when (snackbarHostState.showSnackbar(
                message = when (uiState.snackBar) {
                    is ListViewModel.SnackBar.DetailsError -> expiredErrorMessage
                    else -> genericErrorMessage
                }
            )) {
                SnackbarResult.Dismissed -> onDismissSnackBar.invoke()
                SnackbarResult.ActionPerformed -> {}
            }
        }
    }

    return Scaffold(
        modifier = Modifier.testTag("loaded"),
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        DialogScreen(uiState.dialogState, onDeleteConfirm, onDismissDialog)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                IconButton(modifier = Modifier.testTag("swap"), onClick = onReorder) {
                    Icon(
                        imageVector = Icons.Filled.SwapVert,
                        contentDescriptionResId = UiR.string.reorder,
                        tint = if (uiState.sorting == ListViewModel.Sorting.ASC) LocalContentColor.current else MaterialTheme.colorScheme.tertiary
                    )
                }
                IconButton(onClick = onReset, modifier = Modifier.testTag("restore")) {
                    Icon(
                        imageVector = Icons.Filled.Restore,
                        contentDescriptionResId = UiR.string.reset
                    )
                }
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("cards")
            ) {
                itemsIndexed(
                    items = uiState.cards,
                    key = { _, item -> item.id }
                ) { _, item ->
                    val contentDesc = stringViewModelContentDesc(item.contentDescription)
                    CardItem(
                        cardViewModel = item,
                        onDelete = { onDelete.invoke(item.id) },
                        hasParentSemantic = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItemPlacement()
                            .semantics(mergeDescendants = true) {
                                contentDescription = contentDesc
                            }
                            .clickable(onClickLabel = stringResource(id = ListR.string.view_details)) {
                                onCardClick.invoke(item.id)
                            },
                    )
                }
            }
        }
    }
}

@Preview(name = "Light Loaded", showSystemUi = true)
@Composable
fun LoadedListScreenPreviewLight(
    @PreviewParameter(LoadedListScreenPreviewParameterProvider::class) uiState: ListViewModel.ViewState.Loaded
) {
    ListScreenPreviewTemplate(uiState)
}

@Preview(name = "Dark Loaded", showSystemUi = true)
@Composable
fun LoadedListScreenPreviewDark(
    @PreviewParameter(LoadedListScreenPreviewParameterProvider::class) uiState: ListViewModel.ViewState.Loaded
) {
    ListScreenPreviewTemplate(uiState, useDarkTheme = true)
}

@Preview(name = "Error", showSystemUi = true)
@Composable
fun ErrorListScreenPreviewDark() {
    ListScreenPreviewTemplate(ListViewModel.ViewState.Error)
}

@Preview(showSystemUi = true)
@FontScalePreviews
@Composable
fun LoadedListScreenPreviewAccessibility(
    @PreviewParameter(LoadedListScreenPreviewParameterProvider::class) uiState: ListViewModel.ViewState.Loaded
) {
    ListScreenPreviewTemplate(uiState)
}

@Composable
fun ListScreenPreviewTemplate(
    uiState: ListViewModel.ViewState,
    useDarkTheme: Boolean = false
) = GvtTheme(useDarkTheme = useDarkTheme) {
    ListScreen(
        uiState,
        {},
        {},
        {},
        {},
        {},
        {},
        {},
        {},
        {}
    )
}

private class LoadedListScreenPreviewParameterProvider :
    PreviewParameterProvider<ListViewModel.ViewState.Loaded> {

    override val values = sequenceOf(
        ListViewModel.ViewState.Loaded(
            listOf(
                CardViewModel(
                    id = "",
                    name = "Mr Batman",
                    cardNumber = "1212 1212 3134 1254",
                    cardType = CardType.GREEN,
                    contentDescription = listOf()
                ),
                CardViewModel(
                    id = "",
                    name = "Mr Superman",
                    cardNumber = "8475 12313 1231",
                    cardType = CardType.BLUE,
                    expiryDate = TextViewModel.Resource(
                        UiR.string.expiry_date_format,
                        "10/11/2023"
                    ),
                    contentDescription = listOf()
                ),
                CardViewModel(
                    id = "",
                    name = "Mr Bruce Wayne",
                    expiryDate = TextViewModel.Resource(
                        UiR.string.expiry_date_format,
                        "10/10/2024"
                    ),
                    cardNumber = "8475 1231 3231",
                    cardType = CardType.PURPLE,
                    referenceNumber = TextViewModel.Resource(
                        UiR.string.reference_format,
                        "4561 5487 4861 5348"
                    ),
                    contentDescription = listOf()
                ),
                CardViewModel(
                    id = "14125",
                    name = "Mrs Wonderwoman",
                    cardType = CardType.BLUE,
                    expiryDate = TextViewModel.Resource(
                        UiR.string.expiry_date_format,
                        "11/01/2019"
                    ),
                    cardNumber = "8475 1231 3231",
                    isExpired = true,
                    contentDescription = listOf()
                )
            )
        )
    )
}