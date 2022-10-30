package com.backpapp.gvttest.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.backpapp.gvttest.domain.DeleteCardUseCase
import com.backpapp.gvttest.domain.GetCardUseCase
import com.backpapp.gvttest.navigation.NavigationEvent
import com.backpapp.gvttest.navigation.Navigator
import com.backpapp.gvttest.ui.mapper.CardViewModelMapper
import com.backpapp.gvttest.ui.model.CardViewModel
import com.backpapp.gvttest.ui.model.DialogState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val getCardUseCase: GetCardUseCase,
    private val deleteCardUseCase: DeleteCardUseCase,
    private val viewModelMapper: CardViewModelMapper,
    private val navigator: Navigator
) : ViewModel() {

    var viewState by mutableStateOf<ViewState>(ViewState.Loading)
        private set

    fun onStart(cardId: String) = getCard(cardId, refresh = false)

    fun onRetryError(cardId: String) {
        getCard(cardId, refresh = true)
    }

    private fun getCard(cardId: String, refresh: Boolean) = viewModelScope.launch {
        viewState = ViewState.Loading
        getCardUseCase.invoke(cardId = cardId, refresh = refresh).collect { result ->
            viewState = if (result is GetCardUseCase.Result.Success) {
                ViewState.Loaded(
                    viewModelMapper.mapToViewModel(result.card, showDelete = false)
                )
            } else {
                ViewState.Error
            }
        }
    }

    fun onBack() {
        navigator.navigateTo(NavigationEvent.PopBackStack)
    }

    fun onDelete(id: String) {
        viewState = viewState.showDeleteDialog(id)
    }

    fun onDeleteConfirm(cardId: String?) {
        if (cardId == null) return
        viewModelScope.launch {
            viewState = viewState.dismissDialog()
            deleteCardUseCase.invoke(cardId).collect { result ->
                if (result is DeleteCardUseCase.Result.Error) {
                    viewState = viewState.showGenericErrorDialog()
                } else {
                    navigator.navigateTo(NavigationEvent.PopBackStack)
                }
            }
        }
    }

    fun onDismissDialog() {
        viewState = viewState.dismissDialog()
    }

    sealed class ViewState {
        object Loading : ViewState()
        data class Loaded(
            val card: CardViewModel,
            val dialogState: DialogState = DialogState.None
        ) : ViewState()

        object Error : ViewState()

        fun showDeleteDialog(id: String) =
            if (this is Loaded) this.copy(dialogState = DialogState.DeleteCard(id)) else this

        fun dismissDialog() =
            if (this is Loaded) this.copy(dialogState = DialogState.None) else this

        fun showGenericErrorDialog() =
            if (this is Loaded) this.copy(dialogState = DialogState.GenericError) else this
    }

}