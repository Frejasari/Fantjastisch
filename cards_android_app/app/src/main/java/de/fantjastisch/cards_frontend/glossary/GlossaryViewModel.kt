package de.fantjastisch.cards_frontend.glossary

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.infrastructure.RepoResult
import kotlinx.coroutines.launch
import org.openapitools.client.models.CardEntity

class GlossaryViewModel(
    private val glossaryModel: GlossaryModel = GlossaryModel()
) : ViewModel() {

    val cards = mutableStateOf<List<CardEntity>>(emptyList())

    val error = mutableStateOf<String?>(null)

    val currentDeleteDialog = mutableStateOf<DeletionProgress?>(null)

    fun onPageLoaded() {
        viewModelScope.launch {
            val result = glossaryModel.getCards()
            when (result) {
                is RepoResult.Success -> cards.value = result.result
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