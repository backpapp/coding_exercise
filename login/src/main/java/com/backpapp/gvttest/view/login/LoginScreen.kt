package com.backpapp.gvttest.view.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.backpapp.gvttest.ui.compose.Button
import com.backpapp.gvttest.ui.compose.Icon
import com.backpapp.gvttest.ui.compose.OutlinedTextField
import com.backpapp.gvttest.ui.compose.utils.FontScalePreviews
import com.backpapp.gvttest.ui.model.TextFieldViewModel
import com.backpapp.gvttest.ui.theme.GvtTheme
import com.backpapp.gvttest.ui.R as UiR
import com.backpapp.gvttest.view.login.R as LoginR

@Composable
fun LoginScreen(
    uiState: LoginViewModel.ViewState.Loaded,
    onEmailUpdate: (String) -> Unit,
    onPasswordUpdate: (String) -> Unit,
    onSubmitClick: () -> Unit,
    onPasswordVisibleClick: () -> Unit
) = LoadedScreen(
    uiState,
    onEmailUpdate,
    onPasswordUpdate,
    onSubmitClick,
    onPasswordVisibleClick
)

@Composable
fun LoadedScreen(
    uiState: LoginViewModel.ViewState.Loaded,
    onEmailUpdate: (String) -> Unit,
    onPasswordUpdate: (String) -> Unit,
    onSubmitClick: () -> Unit,
    onPasswordVisibleClick: () -> Unit
) = Surface(modifier = Modifier.testTag("login")) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(id = UiR.dimen.side_screen))
    ) {
        LoginTextField(uiState.userName, onEmailUpdate)
        PasswordTextField(
            uiState.password,
            uiState.isPasswordVisible,
            onPasswordUpdate,
            onSubmitClick,
            onPasswordVisibleClick
        )
        SubmitButton(onSubmitClick)
    }
}

// I wouldn't use this in production right now :)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginTextField(userName: TextFieldViewModel, onEmailUpdate: (String) -> Unit) =
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        textFieldViewModel = userName,
        errorResId = LoginR.string.login_empty_error,
        onValueChange = {
            onEmailUpdate(it)
        },
        labelResId = LoginR.string.username,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
    )

// I wouldn't use this in production right now :)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordTextField(
    password: TextFieldViewModel,
    isPasswordVisible: Boolean,
    onPasswordUpdate: (String) -> Unit,
    onSubmitClick: () -> Unit,
    onPasswordVisibleClick: () -> Unit
) = OutlinedTextField(
    modifier = Modifier
        .padding(top = dimensionResource(id = UiR.dimen.margin_between_elements))
        .fillMaxWidth(),
    textFieldViewModel = password,
    errorResId = LoginR.string.password_empty_error,
    onValueChange = {
        onPasswordUpdate(it)
    },
    labelResId = LoginR.string.password,
    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
    keyboardOptions = KeyboardOptions(
        imeAction = ImeAction.Done,
        keyboardType = KeyboardType.Password
    ),
    keyboardActions = KeyboardActions {
        onSubmitClick()
    },
    trailingIcon = {
        IconButton(
            onClick = onPasswordVisibleClick,
            modifier = Modifier.testTag("passwordVisibility")
        ) {
            Icon(
                imageVector = if (isPasswordVisible) {
                    Icons.Filled.Visibility
                } else {
                    Icons.Filled.VisibilityOff
                }, contentDescriptionResId = if (isPasswordVisible) {
                    LoginR.string.hide_password
                } else {
                    LoginR.string.show_password
                }
            )
        }
    }
)

@Composable
fun SubmitButton(onSubmitClick: () -> Unit) = Button(
    textId = LoginR.string.login,
    modifier = Modifier
        .fillMaxWidth()
        .padding(top = dimensionResource(id = UiR.dimen.margin_between_elements))
        .testTag("submit"),
    onClick = onSubmitClick
)


@Preview(name = "Default", showSystemUi = true)
@Composable
fun LoginScreenPreview(
    @PreviewParameter(LoginScreenPreviewParameterProvider::class) uiState: LoginViewModel.ViewState.Loaded
) {
    LoginScreenPreviewTemplate(uiState)
}

@Preview(name = "Dark", showSystemUi = true)
@Composable
fun LoginScreenPreviewDark(
    @PreviewParameter(LoginScreenPreviewParameterProvider::class) uiState: LoginViewModel.ViewState.Loaded
) {
    LoginScreenPreviewTemplate(uiState, useDarkTheme = true)
}

@Preview(showSystemUi = true)
@FontScalePreviews
@Composable
fun LoginScreenPreviewAccessibility(
    @PreviewParameter(LoginScreenPreviewParameterProvider::class) uiState: LoginViewModel.ViewState.Loaded
) {
    LoginScreenPreviewTemplate(uiState)
}

@Composable
fun LoginScreenPreviewTemplate(
    uiState: LoginViewModel.ViewState.Loaded,
    useDarkTheme: Boolean = false
) = GvtTheme(useDarkTheme = useDarkTheme) {
    LoginScreen(
        uiState,
        {},
        {},
        {},
        {}
    )
}

private class LoginScreenPreviewParameterProvider :
    PreviewParameterProvider<LoginViewModel.ViewState.Loaded> {

    override val values = sequenceOf(
        LoginViewModel.ViewState.Loaded(
            userName = TextFieldViewModel("userName"),
            password = TextFieldViewModel("password"),
            isPasswordVisible = false
        ),
        LoginViewModel.ViewState.Loaded(
            userName = TextFieldViewModel("userName", isError = true),
            password = TextFieldViewModel("password", isError = true),
            isPasswordVisible = true
        )
    )
}
