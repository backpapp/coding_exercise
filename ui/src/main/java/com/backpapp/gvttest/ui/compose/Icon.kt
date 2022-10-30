package com.backpapp.gvttest.ui.compose

import androidx.annotation.StringRes
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource

@Composable
fun Icon(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    @StringRes contentDescriptionResId: Int,
    tint: Color = LocalContentColor.current
) =
    androidx.compose.material3.Icon(
        modifier = modifier,
        imageVector = imageVector,
        contentDescription = stringResource(contentDescriptionResId),
        tint = tint
    )