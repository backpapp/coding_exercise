package com.backpapp.gvttest

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.backpapp.gvttest.details.DetailsScreen
import com.backpapp.gvttest.details.DetailsViewModel
import com.backpapp.gvttest.list.ListScreen
import com.backpapp.gvttest.list.ListViewModel
import com.backpapp.gvttest.navigation.NavigationEvent.Screen
import com.backpapp.gvttest.navigation.Navigator
import com.backpapp.gvttest.view.login.LoginScreen
import com.backpapp.gvttest.view.login.LoginViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun MainScreen(navController: NavHostController, navigator: Navigator) {

    LaunchedEffect("navigation") {
        navigator.sharedFlow.onEach { navigationEvent ->
            if (navigationEvent is Screen) {
                navController.navigate(navigationEvent.route) {
                    val popupTo = navigationEvent.popupTo
                    if (popupTo != null) {
                        popUpTo(popupTo.route) { inclusive = true }
                    }
                }
            } else {
                navController.popBackStack()
            }
        }.launchIn(this)
    }
    NavHost(
        navController = navController, startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            val viewModel = hiltViewModel<LoginViewModel>()
            LoginScreen(
                viewModel.viewState,
                viewModel::updateUserName,
                viewModel::updatePassword,
                viewModel::onSubmitClick,
                viewModel::togglePasswordVisible
            )
        }
        composable(Screen.List.route) {
            val viewModel = hiltViewModel<ListViewModel>()
            ListScreen(
                viewModel.viewState,
                viewModel::onStart,
                viewModel::onRetryError,
                viewModel::onDelete,
                viewModel::onDeleteConfirm,
                viewModel::onReset,
                viewModel::onReorder,
                viewModel::onCardClick,
                viewModel::onDismissDialog,
                viewModel::onDismissSnackbar
            )
        }
        composable(Screen.Detail.ROUTE) { navBackStackEntry ->
            val cardId = navBackStackEntry.arguments?.getString(Screen.Detail.CARD_ID)
            cardId?.let {
                val viewModel = hiltViewModel<DetailsViewModel>()
                DetailsScreen(
                    cardId,
                    viewModel.viewState,
                    viewModel::onStart,
                    viewModel::onBack,
                    viewModel::onRetryError,
                    viewModel::onDelete,
                    viewModel::onDeleteConfirm,
                    viewModel::onDismissDialog,
                )
            }
        }
    }
}