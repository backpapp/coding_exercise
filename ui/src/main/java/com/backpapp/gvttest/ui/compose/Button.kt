package com.backpapp.gvttest.ui.compose

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Button(
    @StringRes textId: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) = androidx.compose.material3.Button(onClick = onClick, modifier = modifier) {
    Text(textId = textId)
}
