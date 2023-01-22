package de.fantjastisch.cards_frontend.learning_object_details.cards_view.move_cards_to_box

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.card.CardRepository
import de.fantjastisch.cards_frontend.card.CardSelectItem
import de.fantjastisch.cards_frontend.infrastructure.RepoResult
import de.fantjastisch.cards_frontend.learning_box.LearningBox
import de.fantjastisch.cards_frontend.learning_box.LearningBoxRepository
import de.fantjastisch.cards_frontend.learning_box.card_to_learning_box.CardToLearningBoxRepository
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
    val learningBoxesInObject = mutableStateOf<List<LearningBox>>(mutableListOf())
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
        learningBoxRepository.getAllBoxesForLearningObject(learningObjectId = learningObjectId,
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
            onFailure = { error.value = "whoops" })
    }

    private fun getContainedCards(allCards: List<CardEntity>) {
        cardToLearningBoxRepository.getCardIdsForBox(learningBoxId = learningBoxId,
            onSuccess = {
                cards.value = allCards.filter { card -> it.contains(card.id) }
                    .map { card -> CardSelectItem(card = card, isChecked = false) }
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

    fun onMoveToPreviousBox() {
        cardToLearningBoxRepository.deleteCardsFromBox(cardIds = cards.value.filter { card -> card.isChecked }
            .map { card -> card.card.id },
            learningBoxId = learningBoxId,
            onSuccess = {
                val previousBoxId = learningBoxesInObject.value[learningBoxNum.value - 1].id
                cardToLearningBoxRepository.insertCardsForBox(cardIds = cards.value.filter { card -> card.isChecked }
                    .map { card -> card.card.id }, learningBoxId = previousBoxId,
                    onSuccess = { onPageLoaded() },
                    onFailure = { error.value = "Whoops" })
            },
            onFailure = { error.value = "Whoops" })
    }

    fun onMoveToNextBox() {
        cardToLearningBoxRepository.deleteCardsFromBox(cardIds = cards.value.filter { card -> card.isChecked }
            .map { card -> card.card.id },
            learningBoxId = learningBoxId,
            onSuccess = {
                val nextBoxId = learningBoxesInObject.value[learningBoxNum.value + 1].id
                cardToLearningBoxRepository.insertCardsForBox(cardIds = cards.value.filter { card -> card.isChecked }
                    .map { card -> card.card.id }, learningBoxId = nextBoxId,
                    onSuccess = { onPageLoaded() },
                    onFailure = { error.value = "Whoops" })
            },
            onFailure = { error.value = "Whoops" })
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