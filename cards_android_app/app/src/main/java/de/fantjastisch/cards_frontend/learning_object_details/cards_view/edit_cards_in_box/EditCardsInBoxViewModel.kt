package de.fantjastisch.cards_frontend.learning_object_details.cards_view.edit_cards_in_box

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.card.CardRepository
import de.fantjastisch.cards_frontend.card.CardSelectItem
import de.fantjastisch.cards_frontend.infrastructure.RepoResult
import de.fantjastisch.cards_frontend.learning_box.card_to_learning_box.CardToLearningBoxRepository
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
                is RepoResult.Success -> getContainedCards(result.result)
                is RepoResult.Error,
                is RepoResult.ServerError -> error.value = "Couldnt fetch cards."
            }
        }
    }

    private fun getContainedCards(allCards: List<CardEntity>) {
        cardToLearningBoxRepository.getCardIdsForBox(learningBoxId = learningBoxId,
            onSuccess = { listOfCardIdsInBox ->
                cardToLearningBoxRepository.getAllCardsForLearningObject(learningObjectId = learningObjectId,
                    onSuccess = { listOfCardIdsInObject ->
                        val cardsPresentInOtherBoxes =
                            listOfCardIdsInObject.filter { id -> !listOfCardIdsInBox.contains(id) }

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
                    onFailure = {})
            },
            onFailure = {
                error.value = "Couldnt get card ids for box."
            })
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
            cards.value.filter { card -> card.isChecked }.map { card -> card.card.id }
        val unSelectedCardIds =
            cards.value.filter { card -> !card.isChecked }.map { card -> card.card.id }
        cardToLearningBoxRepository.insertAndDeleteInBox(
            selected = selectedCardsIds,
            unselected = unSelectedCardIds,
            learningBoxId = learningBoxId,
            onSuccess = { isFinished.value = true },
            onFailure = { error.value = "Whoops" }
        )
    }
}