package de.fantjastisch.cards_frontend.learning_object_details.cards_view.edit_cards_in_box

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.card.CardRepository
import de.fantjastisch.cards_frontend.card.CardSelectItem
import de.fantjastisch.cards_frontend.glossary.CardsFilters
import de.fantjastisch.cards_frontend.infrastructure.RepoResult
import de.fantjastisch.cards_frontend.infrastructure.fold
import de.fantjastisch.cards_frontend.learning_box.card_to_learning_box.CardToLearningBoxRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.openapitools.client.models.CardEntity
import org.openapitools.client.models.ErrorEntryEntity
import java.util.*

class EditCardsInBoxViewModel(
    private val learningBoxId: UUID,
    private val learningObjectId: UUID,
    private val cardRepository: CardRepository = CardRepository(),
    private val cardToLearningBoxRepository: CardToLearningBoxRepository = CardToLearningBoxRepository()
) : ViewModel() {

    val cards = mutableStateOf<List<CardSelectItem>>(mutableListOf())
    val errors = mutableStateOf<List<ErrorEntryEntity>>(emptyList())
    val error = mutableStateOf<String?>(null)
    val isFinished = mutableStateOf(false)


    init {
        onPageLoaded()
    }

    fun onPageLoaded() {
        viewModelScope.launch {
            val result = cardRepository.getPage(
                categoryIds = null,
                search = null,
                tag = null,
                sort = null
            )
            when (result) {
                is RepoResult.Success -> loadContainedCards(result.result)
                is RepoResult.Error,
                is RepoResult.ServerError -> error.value = "Ein Netzwerkfehler ist aufgetreten."
            }
        }
    }

    private fun loadContainedCards(allCards: List<CardEntity>) {
        viewModelScope.launch {
            val result = cardToLearningBoxRepository.getCardIdsForBox(learningBoxId = learningBoxId)

            when (result) {
                is RepoResult.Success -> {
                    val listOfCardIdsInBox = result.result
                    cardToLearningBoxRepository.getAllCardsForLearningObject(learningObjectId = learningObjectId)
                        .fold(
                            onSuccess = { listOfCardIdsInObject ->
                                val cardsPresentInOtherBoxes =
                                    listOfCardIdsInObject.filter { id ->
                                        !listOfCardIdsInBox.contains(
                                            id
                                        )
                                    }

                                cards.value = allCards.filter { card ->
                                    listOfCardIdsInBox.contains(card.id) || !cardsPresentInOtherBoxes.contains(
                                        card.id
                                    )
                                }.map { card ->
                                    CardSelectItem(
                                        card = card,
                                        isChecked = listOfCardIdsInBox.contains(card.id)
                                    )
                                }
                            },
                            onValidationError = {},
                            onUnexpectedError = {})
                }
                is RepoResult.Error,
                is RepoResult.ServerError -> {
                    error.value = "Ein Netzwerkfehler ist aufgetreten."
                }
            }
        }
    }

    fun onCardSelected(id: UUID) {
        cards.value = cards.value.map {
            if (it.card.id == id) {
                it.copy(isChecked = !it.isChecked)
            } else {
                it
            }
        }
    }

    fun onAddCardsClicked() {
        val selectedCardsIds =
            cards.value
                .filter { card -> card.isChecked }
                .map { card -> card.card.id }

        viewModelScope.launch {
            cardToLearningBoxRepository.updateBoxCards(
                selected = selectedCardsIds,
                learningBoxId = learningBoxId,
            ).fold(
                onSuccess = { isFinished.value = true },
                onValidationError = { error.value = "Fehler bei der Eingabevalidierung." },
                onUnexpectedError = { error.value = "Ein unbekannter Fehler ist aufgetreten." }
            )
        }
    }
}