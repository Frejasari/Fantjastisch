package de.fantjastisch.cards_frontend.card.delete

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import de.fantjastisch.cards_frontend.card.CardRepository
import java.util.*

class DeleteCardViewModel(
    private val cardRepository: CardRepository = CardRepository(),
    private val cardId: UUID
    // : ViewModel() = extends ViewModel
) : ViewModel() {

    val error = mutableStateOf<String?>(null)
    val isFinished = mutableStateOf(false)

    fun onDeleteClicked() {
        error.value = null
        cardRepository.deleteCard(
            cardId = cardId,
            onSuccess = {
                isFinished.value = true
            },
            onFailure = {
                // Fehler anzeigen:
                error.value = "Irgendwas ist schief gelaufen"

            }
        )
    }

}