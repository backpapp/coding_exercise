package com.backpapp.gvttest.view.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.backpapp.gvttest.domain.LoginUseCase
import com.backpapp.gvttest.navigation.NavigationEvent.Screen
import com.backpapp.gvttest.navigation.Navigator
import com.backpapp.gvttest.ui.model.TextFieldViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val navigator: Navigator,
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    var viewState by mutableStateOf(ViewState.Loaded())
        private set

    fun onSubmitClick() =
        viewModelScope.launch {
            loginUseCase.invoke(
                viewState.userName.text,
                viewState.password.text
            ).collect { result ->
                when (result) {
                    is LoginUseCase.Result.Success -> {
                        navigator.navigateTo(Screen.List)
                    }
                    is LoginUseCase.Result.Error -> {
                        viewState = result.errors.fold(viewState) { acc, error ->
                            when (error) {
                                LoginUseCase.ErrorType.UserNameEmpty -> acc.userNameError(true)
                                LoginUseCase.ErrorType.PasswordEmpty -> acc.passwordError(true)
                            }
                        }
                    }
                }
            }
        }

    fun updateUserName(userName: String) {
        viewState = viewState.userNameText(userName).userNameError(false)
    }

    fun updatePassword(password: String) {
        viewState = viewState.passwordText(password).passwordError(false)
    }

    fun togglePasswordVisible() {
        viewState = viewState.passwordVisible(viewState.isPasswordVisible.not())
    }

    sealed class ViewState {
        data class Loaded(
            val userName: TextFieldViewModel = TextFieldViewModel(),
            val password: TextFieldViewModel = TextFieldViewModel(),
            val isPasswordVisible: Boolean = false
        ) : ViewState() {
            fun userNameText(text: String) = this.copy(userName = this.userName.copy(text = text))

            fun userNameError(isError: Boolean) =
                this.copy(userName = this.userName.copy(isError = isError))

            fun passwordText(text: String) = this.copy(password = this.password.copy(text = text))

            fun passwordError(isError: Boolean) =
                this.copy(password = this.password.copy(isError = isError))

            fun passwordVisible(passwordVisible: Boolean) =
                this.copy(isPasswordVisible = passwordVisible)
        }
    }
}