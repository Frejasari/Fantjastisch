package de.fantjastisch.cards_frontend.glossary

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.infrastructure.RepoResult
import de.fantjastisch.cards_frontend.infrastructure.fold
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.openapitools.client.models.CardEntity
import org.openapitools.client.models.LinkEntity
import java.util.*


object CardsFilters {
    val filters = MutableStateFlow(
        CardFilters(
            search = "",
            tag = "",
            categories = emptyList(),
            sort = false
        )
    )
}

data class CardFilters(
    val search: String,
    val tag: String,
    val categories: List<UUID>,
    val sort: Boolean
)

class GlossaryViewModel(
    private val glossaryModel: GlossaryModel = GlossaryModel()
) : ViewModel() {

    val cards = mutableStateOf<List<CardEntity>>(emptyList())
    val error = mutableStateOf<String?>(null)
    val currentDeleteDialog = mutableStateOf<DeletionProgress?>(null)
//    val cardLinks = mutableStateOf<List<LinkEntity>>(emptyList())

    init {
        viewModelScope.launch {
            CardsFilters.filters.collectLatest {
                onPageLoaded()
            }
            /*glossaryModel
                .initializePage()
                .fold(
                    onSuccess = { card ->
                        cardLinks.value = card.links
                    },
                    onError = { error.value = "Something is wrong" },
                    onUnexpectedError = { error.value = "Irgendwas ist schief gelaufen" }
                )*/
        }
    }

    fun onPageLoaded() {
        viewModelScope.launch {
            val result = glossaryModel.getCards(
                categoryIds = CardsFilters.filters.value.categories,
                search = CardsFilters.filters.value.search,
                tag = CardsFilters.filters.value.tag,
                sort = CardsFilters.filters.value.sort
            )
            when (result) {
                is RepoResult.Success -> {
                    cards.value = result.result
                    Log.v("CardsFilter", "Received ${result.result.size} cards")
                }
                is RepoResult.Error -> Unit
                is RepoResult.ServerError -> Unit

            }
        }
    }

    fun onTryDeleteCard(card: CardEntity) {
        currentDeleteDialog.value = DeletionProgress.ConfirmWithUser(card)
    }

    fun onDeleteCardClicked() {
        val card = currentDeleteDialog.value!!.card
        currentDeleteDialog.value = DeletionProgress.Deleting(card)
        error.value = null
        viewModelScope.launch {
            val result = glossaryModel.deleteCard(
                cardId = card.id
            )
            when (result) {
                is RepoResult.Success -> {
                    onPageLoaded()
                    currentDeleteDialog.value = null
                }
                is RepoResult.Error,
                is RepoResult.ServerError -> {
                    // Fehler anzeigen:
                    error.value = "Irgendwas ist schief gelaufen"
                }

            }
        }
    }

    fun onDeleteCardAborted() {
        currentDeleteDialog.value = null
    }

    sealed class DeletionProgress {
        abstract val card: CardEntity

        data class ConfirmWithUser(override val card: CardEntity) : DeletionProgress()
        data class Deleting(override val card: CardEntity) : DeletionProgress()
    }



}