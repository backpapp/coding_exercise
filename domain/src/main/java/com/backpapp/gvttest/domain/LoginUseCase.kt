package com.backpapp.gvttest.domain

import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class LoginUseCase @Inject constructor() {
    operator fun invoke(userName: String, password: String) = flowOf(
        if (userName.isNotBlank() && password.isNotBlank()) {
            Result.Success
        } else {
            Result.Error(buildList {
                if (userName.isBlank()) add(ErrorType.UserNameEmpty)
                if (password.isBlank()) add(ErrorType.PasswordEmpty)
            })
        }
    )

    sealed class Result {
        object Success : Result()
        data class Error(val errors: List<ErrorType>) : Result()
    }

    sealed class ErrorType {
        object UserNameEmpty : ErrorType()
        object PasswordEmpty : ErrorType()
    }
}