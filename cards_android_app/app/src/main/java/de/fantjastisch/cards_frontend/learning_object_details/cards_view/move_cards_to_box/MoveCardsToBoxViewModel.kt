package de.fantjastisch.cards_frontend.learning_object_details.cards_view.move_cards_to_box

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.card.CardRepository
import de.fantjastisch.cards_frontend.card.CardSelectItem
import de.fantjastisch.cards_frontend.infrastructure.RepoResult
import de.fantjastisch.cards_frontend.infrastructure.fold
import de.fantjastisch.cards_frontend.learning_box.LearningBoxRepository
import de.fantjastisch.cards_frontend.learning_box.LearningBoxWitNrOfCards
import de.fantjastisch.cards_frontend.learning_box.card_to_learning_box.CardToLearningBoxRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.openapitools.client.models.CardEntity
import org.openapitools.client.models.ErrorEntryEntity
import java.util.*

class MoveCardsToBoxViewModel(
    private val learningBoxId: UUID,
    private val cardRepository: CardRepository = CardRepository(),
    private val cardToLearningBoxRepository: CardToLearningBoxRepository = CardToLearningBoxRepository(),
    private val learningBoxRepository: LearningBoxRepository = LearningBoxRepository(),
    private val learningObjectId: UUID
) : ViewModel() {

    val cards = mutableStateOf<List<CardSelectItem>>(mutableListOf())
    val errors = mutableStateOf<List<ErrorEntryEntity>>(emptyList())
    val error = mutableStateOf<String?>(null)
    val isFinished = mutableStateOf(false)
    val learningBoxesInObject = mutableStateOf<List<LearningBoxWitNrOfCards>>(mutableListOf())
    val learningBoxNum = mutableStateOf<Int>(-1)
    var isLastBox = false
    var isFirstBox = false

    init {
        onPageLoaded()
    }

    fun getLearningBoxNum(): Int {
        return learningBoxesInObject.value.first { box -> box.id == learningBoxId }.boxNumber
    }

    fun onPageLoaded() {
        viewModelScope.launch {
            learningBoxRepository.getAllBoxesForLearningObject(
                learningObjectId = learningObjectId
            )
                .fold(
                    onSuccess = {
                        learningBoxesInObject.value = it
                        learningBoxNum.value = getLearningBoxNum()
                        isFirstBox = learningBoxNum.value == 0
                        isLastBox = learningBoxNum.value == learningBoxesInObject.value.size - 1
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
                    },
                    onUnexpectedError = { error.value = "whoops" },
                    onValidationError = { error.value = "whoops" }
                )
        }
    }

    private fun getContainedCards(allCards: List<CardEntity>) {
        viewModelScope.launch {
            cardToLearningBoxRepository.getCardIdsForBox(
                learningBoxId = learningBoxId
            ).fold(
                onSuccess = {
                    cards.value = allCards
                        .filter { card -> it.contains(card.id) }
                        .map { card ->
                            CardSelectItem(
                                card = card,
                                isChecked = false
                            )
                        }
                },
                onUnexpectedError = {
                    error.value = "Couldnt get card ids for box."
                },
                onValidationError = {
                    error.value = "Couldnt get card ids for box."
                })
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

    // todo try and move stuff like this into the repo layer. It should be done in one transaction, I guess you can even write one query for that.
    fun onMoveToPreviousBox() {
        val previousBoxId = learningBoxesInObject.value[learningBoxNum.value - 1].id

        viewModelScope.launch {
            cardToLearningBoxRepository.moveCards(
                from = learningBoxId,
                to = previousBoxId,
                cardIds = cards.value.filter { card -> card.isChecked }
                    .map { card -> card.card.id })
                .fold(
                    onSuccess = { onPageLoaded() },
                    onUnexpectedError = { error.value = "Whoops" },
                    onValidationError = { error.value = "Whoops" })
        }
    }

    fun onMoveToNextBox() {
        viewModelScope.launch {
            val nextBoxId = learningBoxesInObject.value[learningBoxNum.value + 1].id
            cardToLearningBoxRepository.moveCards(
                from = learningBoxId,
                to = nextBoxId,
                cardIds = cards.value.filter { card -> card.isChecked }
                    .map { card -> card.card.id })
                .fold(
                    onSuccess = { onPageLoaded() },
                    onUnexpectedError = { error.value = "Whoops" },
                    onValidationError = { error.value = "Whoops" })
        }
    }

    fun onMoveCardsClicked() {
//        val selectedCardsIds = cards.value.filter {card -> card.isChecked}.map { card -> card.card.id }
//        val unSelectedCardIds = cards.value.filter {card -> !card.isChecked}.map { card -> card.card.id }
//        cardToLearningBoxRepository.insertAndDeleteInBox(
//            selected = selectedCardsIds,
//            unselected=unSelectedCardIds,
//            learningBoxId = learningBoxId,
//            onSuccess = { isFinished.value = true },
//            onFailure = { error.value = "Whoops" }
//        )
    }
}