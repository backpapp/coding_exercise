package com.backpapp.gvttest.ui.compose

import androidx.compose.runtime.Composable
import com.backpapp.gvttest.ui.R
import com.backpapp.gvttest.ui.model.DialogState

@Composable
fun DialogScreen(
    dialogState: DialogState,
    onConfirm: (String?) -> Unit,
    onDismissDialog: () -> Unit,
) {
    if (dialogState is DialogState.DeleteCard) {
        SimpleAlertDialog(
            textId = R.string.delete_dialog_text,
            confirmTextId = R.string.delete,
            dismissTextId = R.string.cancel,
            onConfirm = { onConfirm.invoke(dialogState.id) },
            onDismiss = onDismissDialog
        )
    } else if (dialogState is DialogState.GenericError) {
        SimpleAlertDialog(
            textId = R.string.generic_error,
            confirmTextId = R.string.dismiss,
            onConfirm = { onDismissDialog.invoke() }
        )
    }
}