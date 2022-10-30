package com.backpapp.gvttest.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.backpapp.gvttest.domain.DeleteCardUseCase
import com.backpapp.gvttest.domain.GetCardUseCase
import com.backpapp.gvttest.domain.GetCardsUseCase
import com.backpapp.gvttest.domain.model.Card
import com.backpapp.gvttest.navigation.NavigationEvent.Screen
import com.backpapp.gvttest.navigation.Navigator
import com.backpapp.gvttest.ui.mapper.CardViewModelMapper
import com.backpapp.gvttest.ui.model.CardViewModel
import com.backpapp.gvttest.ui.model.DialogState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val getCardsUseCase: GetCardsUseCase,
    private val getCardUseCase: GetCardUseCase,
    private val deleteCardUseCase: DeleteCardUseCase,
    private val navigator: Navigator,
    private val viewModelMapper: CardViewModelMapper
) : ViewModel() {

    var viewState by mutableStateOf<ViewState>(ViewState.Loading)
        private set

    fun onStart() = getCards(refresh = false)

    fun onRetryError() = getCards(refresh = true)

    private fun getCards(refresh: Boolean) =
        viewModelScope.launch {
            viewState = ViewState.Loading
            getCardsUseCase.invoke(refresh).collect { result ->
                viewState = if (result is GetCardsUseCase.Result.Success) {
                    ViewState.Loaded(
                        viewModelMapper.mapToViewModel(result.cards, showDelete = true)
                    ).reorder(Sorting.ASC)
                } else {
                    ViewState.Error
                }
            }
        }

    fun onDelete(id: String) {
        viewState = viewState.showDeleteDialog(id)
    }

    fun onDeleteConfirm(cardId: String?) {
        if (cardId == null) return
        viewModelScope.launch {
            val originalCards = (viewState as? ViewState.Loaded)?.cards
            viewState = viewState.removeCard(cardId).dismissDialog()
            deleteCardUseCase.invoke(cardId).collect { result ->
                if (result is DeleteCardUseCase.Result.Error && originalCards != null) {
                    viewState = viewState.restoreCards(originalCards).showGenericErrorSnackBar()
                } else if (result is DeleteCardUseCase.Result.Error) {
                    viewState = viewState.showGenericErrorSnackBar()
                    onReset()
                }
            }
        }
    }

    fun onDismissDialog() {
        viewState = viewState.dismissDialog()
    }

    fun onDismissSnackbar() {
        viewState = viewState.dismissSnackBar()
    }

    fun onReset() = getCards(refresh = true)

    fun onReorder() {
        val newSorting = when (viewState.sorting()) {
            Sorting.ASC -> {
                Sorting.DSC
            }
            Sorting.DSC -> {
                Sorting.ASC
            }
            else -> {
                return
            }
        }
        viewState = viewState.reorder(newSorting)
    }

    fun onCardClick(id: String) {
        viewModelScope.launch {
            getCardUseCase.invoke(cardId = id, refresh = false).collect { result ->
                val now = LocalDate.now()
                if (result is GetCardUseCase.Result.Success) {
                    val card = result.card
                    when {
                        card is Card.Green && now.isAfter(card.expiryDate) -> {
                            viewState = viewState.showExpiredErrorSnackBar()
                        }
                        card is Card.Purple && now.isAfter(card.expiryDate) -> {
                            viewState = viewState.showExpiredErrorSnackBar()
                        }
                        else -> {
                            navigator.navigateTo(Screen.Detail(result.card.id))
                        }
                    }
                } else {
                    viewState = viewState.showGenericErrorSnackBar()
                }
            }
        }
    }

    sealed class ViewState {
        object Loading : ViewState()
        data class Loaded(
            val cards: List<CardViewModel>,
            val sorting: Sorting = Sorting.ASC,
            val dialogState: DialogState = DialogState.None,
            val snackBar: SnackBar? = null
        ) : ViewState()

        object Error : ViewState()

        fun showDeleteDialog(id: String) =
            if (this is Loaded) this.copy(dialogState = DialogState.DeleteCard(id)) else this

        fun dismissDialog() =
            if (this is Loaded) this.copy(dialogState = DialogState.None) else this

        fun showGenericErrorSnackBar() =
            if (this is Loaded) this.copy(snackBar = SnackBar.GenericError) else this

        fun showExpiredErrorSnackBar() =
            if (this is Loaded) this.copy(snackBar = SnackBar.DetailsError) else this

        fun dismissSnackBar() =
            if (this is Loaded) this.copy(snackBar = null) else this

        fun removeCard(cardId: String) =
            if (this is Loaded) this.copy(cards = cards.filter { it.id != cardId }) else this

        fun restoreCards(cards: List<CardViewModel>) =
            if (this is Loaded) this.copy(cards = cards) else this

        fun reorder(sorting: Sorting) =
            if (this is Loaded) {
                this.copy(
                    cards = if (sorting == Sorting.ASC) cards.sortedBy { it.name } else cards.sortedByDescending { it.name },
                    sorting = sorting
                )
            } else {
                this
            }

        fun sorting() = if (this is Loaded) this.sorting else null
    }

    sealed class SnackBar {
        object GenericError : SnackBar()
        object DetailsError : SnackBar()
    }

    enum class Sorting {
        ASC,
        DSC
    }
}

