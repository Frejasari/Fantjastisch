package de.fantjastisch.cards_frontend.learning_mode

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.card.CardRepository
import de.fantjastisch.cards_frontend.infrastructure.RepoResult
import de.fantjastisch.cards_frontend.infrastructure.fold
import de.fantjastisch.cards_frontend.learning_box.LearningBoxRepository
import de.fantjastisch.cards_frontend.learning_box.LearningBoxWitNrOfCards
import de.fantjastisch.cards_frontend.learning_box.card_to_learning_box.CardToLearningBoxRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
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
    private var nextCards: Queue<CardEntity> = LinkedList()
    private var learningBoxesInObject: List<LearningBoxWitNrOfCards> = listOf()
    val currentCard = mutableStateOf<CardEntity?>(null)
    val learningBox = mutableStateOf<LearningBoxWitNrOfCards?>(null)

    var isLoading = mutableStateOf(true)

    var numberOfCardsRemaining = mutableStateOf(0)
    var isLastBox = false
    var isFirstBox = false

    init {
        onPageLoaded()
    }

    fun onFlipCardClicked() {
        isShowingAnswer.value = !isShowingAnswer.value
    }

    fun onCardStaysInBoxClicked() {
        nextCard()
    }


    fun onCardGoesToNextBoxClicked() {
        isShowingAnswer.value = false
        val nextBoxNum = learningBox.value!!.boxNumber + 1
        if (nextBoxNum < learningBoxesInObject.size) {
            val nextBoxId = learningBoxesInObject[nextBoxNum].id

            viewModelScope.launch {
                cardToLearningBoxRepository.moveCards(
                    from = learningBoxId,
                    to = nextBoxId,
                    cardIds = listOf(currentCard.value!!.id)
                ).fold(
                    onSuccess = { nextCard() },
                    onUnexpectedError = { error.value = "Whoops" },
                    onValidationError = { error.value = "Whoops" })
            }
        }
    }

    fun onCardGoesToPreviousBoxClicked() {
        isShowingAnswer.value = false
        val previousBoxNr = learningBox.value!!.boxNumber - 1
        if (previousBoxNr >= 0) {
            val nextBoxId = learningBoxesInObject[previousBoxNr].id

            viewModelScope.launch {
                cardToLearningBoxRepository.moveCards(
                    from = learningBoxId,
                    to = nextBoxId,
                    cardIds = listOf(currentCard.value!!.id)
                )
                    .fold(
                        onSuccess = { nextCard() },
                        onUnexpectedError = { error.value = "Whoops" },
                        onValidationError = { error.value = "Whoops" })
            }
        }
    }

    private fun onPageLoaded() {

        viewModelScope.launch {
            val (cardResult, learningBoxResult, cardsInLearningBoxResult) = awaitAll(
                async {
                    cardRepository.getPage(
                        categoryIds = null,
                        search = null,
                        tag = null,
                        sort = null
                    )
                },
                async {
                    learningBoxRepository.getAllBoxesForLearningObject(
                        learningObjectId = learningObjectId
                    )
                },
                async {
                    cardToLearningBoxRepository
                        .getCardIdsForBox(learningBoxId = learningBoxId)
                }
            )

            @Suppress("UNCHECKED_CAST")
            when {
                cardResult is RepoResult.Success
                        && learningBoxResult is RepoResult.Success
                        && cardsInLearningBoxResult is RepoResult.Success -> {
                    val allCards = cardResult.result as List<CardEntity>
                    val learningBoxes = learningBoxResult.result as List<LearningBoxWitNrOfCards>
                    val cardsInLearningBox = cardsInLearningBoxResult.result as List<UUID>

                    learningBoxesInObject = learningBoxes

                    val box = learningBoxes.firstOrNull { it.id == learningBoxId }

                    if (box == null) {
                        error.value = "Lernbox konnte nicht eingeholt werden."
                    } else {
                        learningBox.value = box
                        isFirstBox = box.boxNumber == 0
                        isLastBox = box.boxNumber == learningBoxes.size - 1
                        nextCards = LinkedList(allCards.filter { card ->
                            cardsInLearningBox.contains(
                                card.id
                            )
                        })
                        nextCard()
                    }
                    isLoading.value = false
                }
                else -> error.value = "Couldnt fetch cards or learning boxes"
            }
        }
    }

    private fun nextCard() {
        if (nextCards.size > 0) {
            isShowingAnswer.value = false
            currentCard.value = nextCards.remove()
            numberOfCardsRemaining.value = nextCards.size + 1

        } else {
            isFinished.value = true
        }
    }
}