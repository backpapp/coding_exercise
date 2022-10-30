package com.backpapp.gvttest.ui.compose

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import com.backpapp.gvttest.ui.model.TextFieldViewModel

@ExperimentalMaterial3Api
@Composable
fun OutlinedTextField(
    textFieldViewModel: TextFieldViewModel,
    @StringRes labelResId: Int,
    modifier: Modifier = Modifier,
    @StringRes errorResId: Int? = null,
    onValueChange: (String) -> Unit,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    trailingIcon: @Composable (() -> Unit)? = null
) = Column {
    androidx.compose.material3.OutlinedTextField(
        value = textFieldViewModel.text,
        onValueChange = onValueChange,
        label = { Text(labelResId) },
        modifier = modifier,
        isError = textFieldViewModel.isError,
        singleLine = true,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        trailingIcon = trailingIcon
    )
    if (textFieldViewModel.isError && errorResId != null) {
        Text(
            errorResId,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall
        )
    }
}
