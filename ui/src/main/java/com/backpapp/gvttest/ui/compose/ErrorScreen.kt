package com.backpapp.gvttest.ui.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import com.backpapp.gvttest.ui.R

@Composable
fun ErrorScreen(onRetryError: () -> Unit) = Box(
    modifier = Modifier
        .fillMaxSize()
        .testTag("error"), contentAlignment = Alignment.Center
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(textId = R.string.generic_error)
        IconButton(modifier = Modifier.testTag("retry"), onClick = onRetryError) {
            Icon(
                modifier = Modifier.size(dimensionResource(id = R.dimen.big_icon)),
                imageVector = Icons.Filled.Refresh,
                contentDescriptionResId = R.string.refresh
            )
        }
    }
}