package com.backpapp.gvttest.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
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
import com.backpapp.gvttest.ui.compose.Text
import com.backpapp.gvttest.ui.compose.utils.FontScalePreviews
import com.backpapp.gvttest.ui.model.CardType
import com.backpapp.gvttest.ui.model.CardViewModel
import com.backpapp.gvttest.ui.model.TextViewModel
import com.backpapp.gvttest.ui.theme.GvtTheme
import com.backpapp.gvttest.view.details.R
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.backpapp.gvttest.ui.R as UiR


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    cardId: String,
    uiState: DetailsViewModel.ViewState,
    onStart: (String) -> Unit,
    onBack: () -> Unit,
    onRetryError: (String) -> Unit,
    onDelete: (String) -> Unit,
    onDeleteConfirm: (String?) -> Unit,
    onDismissDialog: () -> Unit,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
) {
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                onStart(cardId)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    Scaffold(
        modifier = Modifier.testTag("details"),
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("back")) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescriptionResId = UiR.string.back
                        )
                    }
                }, actions = {
                    IconButton(
                        onClick = { onDelete.invoke(cardId) },
                        modifier = Modifier.testTag("delete")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescriptionResId = UiR.string.delete
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        when (uiState) {
            is DetailsViewModel.ViewState.Error -> ErrorScreen {
                onRetryError.invoke(cardId)
            }
            is DetailsViewModel.ViewState.Loaded -> LoadedScreen(
                uiState = uiState,
                innerPadding = innerPadding,
                onDeleteConfirm = onDeleteConfirm,
                onDismissDialog = onDismissDialog
            )
            is DetailsViewModel.ViewState.Loading -> LoadingScreen()
        }
    }
}

@Composable
fun LoadedScreen(
    uiState: DetailsViewModel.ViewState.Loaded,
    onDeleteConfirm: (String?) -> Unit,
    onDismissDialog: () -> Unit,
    innerPadding: PaddingValues
) {
    DialogScreen(
        uiState.dialogState,
        onDeleteConfirm,
        onDismissDialog
    )
    Column(
        modifier = Modifier
            .padding(innerPadding)
            .verticalScroll(rememberScrollState())
            .fillMaxHeight()
            .testTag("loaded")
    ) {
        CardItem(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = dimensionResource(id = com.backpapp.gvttest.ui.R.dimen.card_height)),
            cardViewModel = uiState.card
        )
        QrCode(uiState.card.cardNumber)
        Text(
            textId = R.string.description,
            modifier = Modifier.padding(dimensionResource(id = UiR.dimen.margin_between_elements))
        )
    }
}

@Composable
fun QrCode(cardNumber: String) {
    val size = with(LocalDensity.current) {
        dimensionResource(id = R.dimen.qr_code).toPx()
    }.toInt()
    val barcodeEncoder = BarcodeEncoder()
    val bitmap = barcodeEncoder.encodeBitmap(cardNumber, BarcodeFormat.QR_CODE, size, size)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("qrCode"),
        horizontalArrangement = Arrangement.Center
    ) {
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = stringResource(id = R.string.qr_code),
        )
    }
}

@Preview(name = "Light Loaded", showSystemUi = true)
@Composable
fun LoadedDetailsScreenPreviewLight(
    @PreviewParameter(LoadedDetailsScreenPreviewParameterProvider::class) uiState: DetailsViewModel.ViewState.Loaded
) {
    DetailsScreenPreviewTemplate(uiState)
}

@Preview(name = "Dark Loaded", showSystemUi = true)
@Composable
fun LoadedDetailsScreenPreviewDark(
    @PreviewParameter(LoadedDetailsScreenPreviewParameterProvider::class) uiState: DetailsViewModel.ViewState.Loaded
) {
    DetailsScreenPreviewTemplate(uiState, useDarkTheme = true)
}

@Preview(name = "Error", showSystemUi = true)
@Composable
fun LoadedDetailsScreenPreviewDark() {
    DetailsScreenPreviewTemplate(DetailsViewModel.ViewState.Error)
}

@Preview(showSystemUi = true)
@FontScalePreviews
@Composable
fun LoadedDetailsScreenPreviewAccessibility(
    @PreviewParameter(LoadedDetailsScreenPreviewParameterProvider::class) uiState: DetailsViewModel.ViewState.Loaded
) {
    DetailsScreenPreviewTemplate(uiState)
}

@Composable
fun DetailsScreenPreviewTemplate(
    uiState: DetailsViewModel.ViewState,
    useDarkTheme: Boolean = false
) = GvtTheme(useDarkTheme = useDarkTheme) {
    DetailsScreen(
        "",
        uiState,
        {},
        {},
        {},
        {},
        {},
        {}
    )
}

private class LoadedDetailsScreenPreviewParameterProvider :
    PreviewParameterProvider<DetailsViewModel.ViewState.Loaded> {

    override val values = sequenceOf(
        DetailsViewModel.ViewState.Loaded(
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
            )
        )
    )
}