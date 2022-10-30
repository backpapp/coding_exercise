package com.backpapp.gvttest.ui.compose

import androidx.annotation.StringRes
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import com.backpapp.gvttest.ui.model.TextViewModel
import com.backpapp.gvttest.ui.utils.stringViewModel

@Composable
fun Text(
    @StringRes textId: Int,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    color: Color = Color.Unspecified
) = androidx.compose.material3.Text(
    text = stringResource(textId),
    modifier = modifier,
    color = color,
    style = style
)

@Composable
fun Text(
    textViewModel: TextViewModel,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    color: Color = Color.Unspecified,
    textAlign: TextAlign? = null,
) = androidx.compose.material3.Text(
    modifier = modifier,
    text = stringViewModel(textViewModel),
    textAlign = textAlign,
    style = style,
    color = color
)