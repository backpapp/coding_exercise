package com.backpapp.gvttest.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import com.backpapp.gvttest.ui.model.TextViewModel

@Composable
@ReadOnlyComposable
fun stringViewModel(textViewModel: TextViewModel): String {
    return when (textViewModel) {
        is TextViewModel.Text -> textViewModel.value
        is TextViewModel.Resource -> {
            if (textViewModel.args.isEmpty()) {
                androidx.compose.ui.res.stringResource(textViewModel.resId)
            } else {
                val args2 = textViewModel.args.map {
                    if (it is TextViewModel) {
                        stringViewModel(it)
                    } else {
                        it
                    }
                }
                androidx.compose.ui.res.stringResource(textViewModel.resId, *args2.toTypedArray())
            }
        }
    }
}

@Composable
@ReadOnlyComposable
fun stringViewModelContentDesc(textViewModels: List<TextViewModel>): String {
    return textViewModels.map { textViewModel ->
        stringViewModel(textViewModel = textViewModel)
    }.joinToString(separator = "\n")
}