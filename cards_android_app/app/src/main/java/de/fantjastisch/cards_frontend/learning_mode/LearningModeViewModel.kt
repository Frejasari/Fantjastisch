package de.fantjastisch.cards_frontend.learning_mode

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.card.CardRepository
import de.fantjastisch.cards_frontend.infrastructure.fold
import de.fantjastisch.cards_frontend.learning_box.LearningBox
import de.fantjastisch.cards_frontend.learning_box.LearningBoxRepository
import de.fantjastisch.cards_frontend.learning_box.card_to_learning_box.CardToLearningBoxRepository
import kotlinx.coroutines.launch
import org.openapitools.client.models.CardEntity
import java.util.*

class LearningModeViewModel(
    private val learningObjectId: UUID,
    private val learningBoxId: UUID,
    private val cardToLearningBoxRepository: CardToLearningBoxRepository = CardToLearningBoxRepository(),
    private val learningBoxRepository: LearningBoxRepository = LearningBoxRepository(),
    private val cardRepository: CardRepository = CardRepository()
) : ViewModel() {

    val error = mutableStateOf("")
    val isFinished = mutableStateOf<Boolean>(false)
    val isShowingAnswer = mutableStateOf<Boolean>(false)
    val cardsInBox = mutableStateOf<List<CardEntity>>(mutableListOf())
    val currentCard = mutableStateOf<CardEntity?>(null)
    val learningBox = mutableStateOf<LearningBox?>(null)
    val learningBoxesInObject = mutableStateOf<List<LearningBox>>(mutableListOf())
    var currentCardIndex = 0
    var numberOfCardsRemaining = 0
    var isLastBox = false
    var isFirstBox = false

    init {
        onPageLoaded()
    }

    fun onFlipCardClicked() {
        isShowingAnswer.value = !isShowingAnswer.value
    }

    fun onCardStaysInBoxClicked() {
        isShowingAnswer.value = false
        currentCardIndex++
        numberOfCardsRemaining--
        if (currentCardIndex >= cardsInBox.value.size) {
            isFinished.value = true
        } else {
            currentCard.value = getCurrentCard(currentCardIndex)
        }
    }


    fun onCardGoesToNextBoxClicked() {
        isShowingAnswer.value = false
        currentCardIndex++
        numberOfCardsRemaining--
        onMoveToNextBox()
        if (currentCardIndex >= cardsInBox.value.size) {
            isFinished.value = true
        } else {
            currentCard.value = getCurrentCard(currentCardIndex)
        }
    }

    fun onPageLoaded() {

        viewModelScope.launch {
            cardRepository.getPage(
                categoryIds = null,
                search = null,
                tag = null,
                sort = null
            ).fold(
                onSuccess = {
                    getContainedCards(it)
                },
                onError = { error.value = "Couldnt fetch cards." },
                onUnexpectedError = { error.value = "Couldnt fetch cards." }
            )
        }
        learningBoxRepository.getAllBoxesForLearningObject(learningObjectId = learningObjectId,
            onSuccess = {
                learningBoxesInObject.value = it
                learningBoxRepository.findByBoxId(learningBoxId,
                    onSuccess = {
                        learningBox.value = it
                        isFirstBox = learningBox.value!!.boxNumber == 0
                        isLastBox =
                            learningBox.value!!.boxNumber == learningBoxesInObject.value.size - 1
                    },
                    onFailure = { error.value = "Lernbox konnte nicht eingeholt werden." }
                )
            },
            onFailure = { error.value = "Fehler" }
        )
    }

    private fun onMoveToNextBox() {
        val nextBoxNum = learningBox.value!!.boxNumber + 1
        if (nextBoxNum < learningBoxesInObject.value.size) {
            val nextBoxId = learningBoxesInObject.value[nextBoxNum].id
            cardToLearningBoxRepository.deleteCardsFromBox(
                listOf(currentCard.value!!.id),
                learningBoxId = learningBoxId,
                onSuccess = {
                    cardToLearningBoxRepository.insertCardsForBox(
                        listOf(currentCard.value!!.id),
                        learningBoxId = nextBoxId,
                        onSuccess = { onPageLoaded() },
                        onFailure = { error.value = "Whoops" })
                },
                onFailure = { error.value = "Whoops" })
        }
    }

    private fun getContainedCards(allCards: List<CardEntity>) {
        cardToLearningBoxRepository.getCardIdsForBox(learningBoxId = learningBoxId,
            onSuccess = {
                cardsInBox.value = allCards.filter { card -> it.contains(card.id) }
                currentCard.value = getCurrentCard(currentCardIndex)
                numberOfCardsRemaining =
                    if (currentCardIndex == 0) {
                        cardsInBox.value.size - 1
                    } else {
                        numberOfCardsRemaining
                    }
            },
            onFailure = {
                error.value = "Couldnt get card ids for box."
            })
    }

    private fun getCurrentCard(index: Int): CardEntity {
        return cardsInBox.value[index]
    }
}