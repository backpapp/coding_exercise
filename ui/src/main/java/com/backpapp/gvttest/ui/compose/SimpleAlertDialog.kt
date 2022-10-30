package com.backpapp.gvttest.ui.compose

import androidx.annotation.StringRes
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun SimpleAlertDialog(
    @StringRes textId: Int,
    @StringRes confirmTextId: Int,
    onConfirm: () -> Unit,
    @StringRes dismissTextId: Int? = null,
    onDismiss: () -> Unit = { },
) = AlertDialog(
    onDismissRequest = onDismiss,
    confirmButton = {
        TextButton(onClick = { onConfirm() }) {
            Text(textId = confirmTextId, color = MaterialTheme.colorScheme.primary)
        }
    },
    dismissButton = {
        if (dismissTextId != null) {
            TextButton(onClick = onDismiss) {
                Text(textId = dismissTextId, color = MaterialTheme.colorScheme.secondary)
            }
        }
    },
    text = {
        Text(textId = textId)
    })
