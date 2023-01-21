package de.fantjastisch.cards_frontend.glossary

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import de.fantjastisch.cards_frontend.card.CardRepository
import org.openapitools.client.models.CardEntity

class GlossaryModel(
    private val cardRepository: CardRepository = CardRepository()
) : ViewModel() {

    val cards = mutableStateOf<List<CardEntity>>(emptyList())

    val error = mutableStateOf<String?>(null)

    val currentDeleteDialog = mutableStateOf<DeletionProgress?>(null)

    sealed class DeletionProgress {
        abstract val card: CardEntity

        data class ConfirmWithUser(override val card: CardEntity) : DeletionProgress()
        data class Deleting(override val card: CardEntity) : DeletionProgress()
    }

    suspend fun getCards() = cardRepository.getPage(
        categoryIds = null,
        search = null,
        tag = null,
        sort = null
    )

    fun onTryDeleteCard(card: CardEntity) {
        currentDeleteDialog.value = DeletionProgress.ConfirmWithUser(card)
    }

    fun onDeleteCardClicked() {
        val card = currentDeleteDialog.value!!.card
        currentDeleteDialog.value = DeletionProgress.Deleting(card)
        error.value = null
        cardRepository.deleteCard(
            cardId = card.id
        )

        ,
        onSuccess = {
            onPageLoaded()
            currentDeleteDialog.value = null
        },
        onFailure = {
            // Fehler anzeigen:
            error.value = "Irgendwas ist schief gelaufen"
        }
        )
    }

    fun onDeleteCardAborted() {
        currentDeleteDialog.value = null
    }

    fun onDeleteSuccessful() {
        onPageLoaded()
    }

    init {
        onPageLoaded()
    }
}