package com.backpapp.gvttest.ui.compose

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.backpapp.gvttest.ui.R
import com.backpapp.gvttest.ui.compose.utils.FontScalePreviews
import com.backpapp.gvttest.ui.model.CardType
import com.backpapp.gvttest.ui.model.CardViewModel
import com.backpapp.gvttest.ui.model.TextViewModel
import com.backpapp.gvttest.ui.theme.GvtTheme
import com.backpapp.gvttest.ui.theme.dark_blue_card_on_primary
import com.backpapp.gvttest.ui.theme.dark_blue_card_primary
import com.backpapp.gvttest.ui.theme.dark_green_card_on_primary
import com.backpapp.gvttest.ui.theme.dark_green_card_primary
import com.backpapp.gvttest.ui.theme.dark_purple_card_on_primary
import com.backpapp.gvttest.ui.theme.dark_purple_card_primary
import com.backpapp.gvttest.ui.theme.light_blue_card_on_primary
import com.backpapp.gvttest.ui.theme.light_blue_card_primary
import com.backpapp.gvttest.ui.theme.light_green_card_on_primary
import com.backpapp.gvttest.ui.theme.light_green_card_primary
import com.backpapp.gvttest.ui.theme.light_purple_card_on_primary
import com.backpapp.gvttest.ui.theme.light_purple_card_primary
import com.backpapp.gvttest.ui.R as UiR

@Composable
fun CardItem(
    cardViewModel: CardViewModel,
    modifier: Modifier = Modifier,
    hasParentSemantic: Boolean = false,
    onDelete: () -> Unit = {}
) = with(cardViewModel) {
    val padding = dimensionResource(id = R.dimen.margin_between_elements)
    val baseModifierAccessibility = if (hasParentSemantic) {
        Modifier.clearAndSetSemantics {
            contentDescription = ""
        }
    } else {
        Modifier
    }
    Card(
        modifier = modifier
            .padding(dimensionResource(id = R.dimen.margin_between_elements))
            .testTag("card"),
        colors = CardDefaults.cardColors(
            containerColor = getCardContainerColor(
                cardViewModel.cardType,
                isSystemInDarkTheme()
            ),
            contentColor = getCardContentColor(cardViewModel.cardType, isSystemInDarkTheme())
        )
    ) {

        Column(
            Modifier
                .fillMaxWidth()
                .padding(start = padding, end = padding, top = padding)
                .defaultMinSize(minHeight = dimensionResource(id = R.dimen.card_height)),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {

            Text(
                modifier = baseModifierAccessibility
                    .fillMaxWidth()
                    .alpha(if (expiryDate != null) 1.0f else 0.0f),
                textViewModel = expiryDate ?: TextViewModel.Text(""),
                textAlign = TextAlign.End,
                style = MaterialTheme.typography.labelSmall,
                color = if (isExpired) MaterialTheme.colorScheme.error else Color.Unspecified
            )
            Column(
                verticalArrangement = Arrangement.Center
            ) {
                androidx.compose.material3.Text(
                    modifier = baseModifierAccessibility.testTag("name"),
                    text = name,
                    style = MaterialTheme.typography.headlineSmall
                )
                androidx.compose.material3.Text(
                    modifier = baseModifierAccessibility,
                    text = cardNumber,
                    style = MaterialTheme.typography.labelSmall
                )
                if (referenceNumber != null) {
                    Text(
                        modifier = baseModifierAccessibility,
                        textViewModel = referenceNumber,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
            Box(
                modifier = Modifier
                    .align(End)
                    .alpha(if (showDelete) 1.0f else 0f)
            ) {
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescriptionResId = UiR.string.delete
                    )
                }
            }
        }
    }
}

fun getCardContainerColor(cardType: CardType, useDarkTheme: Boolean) = when (cardType) {
    CardType.PURPLE -> if (useDarkTheme) dark_purple_card_primary else light_purple_card_primary
    CardType.BLUE -> if (useDarkTheme) dark_blue_card_primary else light_blue_card_primary
    CardType.GREEN -> if (useDarkTheme) dark_green_card_primary else light_green_card_primary
}

fun getCardContentColor(cardType: CardType, useDarkTheme: Boolean) = when (cardType) {
    CardType.PURPLE -> if (useDarkTheme) dark_purple_card_on_primary else light_purple_card_on_primary
    CardType.BLUE -> if (useDarkTheme) dark_blue_card_on_primary else light_blue_card_on_primary
    CardType.GREEN -> if (useDarkTheme) dark_green_card_on_primary else light_green_card_on_primary
}

@Preview(name = "Light Loaded")
@Composable
fun LoadedListScreenPreviewLight(
    @PreviewParameter(CardItemPreviewParameterProvider::class) cardViewModel: CardViewModel
) {
    CardItemPreviewTemplate(cardViewModel)
}

@Preview(name = "Dark Loaded")
@Composable
fun CardItemPreviewDark(
    @PreviewParameter(CardItemPreviewParameterProvider::class) cardViewModel: CardViewModel
) {
    CardItemPreviewTemplate(cardViewModel, useDarkTheme = true)
}

@Preview
@FontScalePreviews
@Composable
fun CardItemPreviewAccessibility(
    @PreviewParameter(CardItemPreviewParameterProvider::class) cardViewModel: CardViewModel
) {
    CardItemPreviewTemplate(cardViewModel)
}

@Composable
fun CardItemPreviewTemplate(
    cardViewModel: CardViewModel,
    useDarkTheme: Boolean = false
) = GvtTheme(useDarkTheme = useDarkTheme) {
    CardItem(cardViewModel)
}

private class CardItemPreviewParameterProvider :
    PreviewParameterProvider<CardViewModel> {

    override val values = sequenceOf(
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
            expiryDate = TextViewModel.Text("Expire: 11/2023"),
            contentDescription = listOf()
        ),
        CardViewModel(
            id = "",
            name = "Mr Bruce Wayne",
            expiryDate = TextViewModel.Text("Expire: 10/2024"),
            cardNumber = "8475 1231 3231",
            cardType = CardType.PURPLE,
            referenceNumber = TextViewModel.Text("Ref: 4561 5487 4861 5348"),
            contentDescription = listOf()
        ),
        CardViewModel(
            id = "14125",
            name = "Mrs Wonderwoman",
            cardType = CardType.BLUE,
            expiryDate = TextViewModel.Text("Expire: 01/2019"),
            cardNumber = "8475 1231 3231",
            isExpired = true,
            contentDescription = listOf()
        )
    )
}