package de.fantjastisch.cards_frontend.learning_mode

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.card.update_and_create.ErrorHandlingViewModel
import de.fantjastisch.cards_frontend.infrastructure.fold
import de.fantjastisch.cards_frontend.learning_box.LearningBoxWitNrOfCards
import kotlinx.coroutines.launch
import org.openapitools.client.models.CardEntity
import java.util.*

/**
 * Stellt die Daten für die [LearningModeView] bereit und nimmt seine Anfragen entgegen.
 *
 * @property learningObjectId Id, des ausgewählten Lernobjektes.
 * @property learningBoxId Id, der ausgewählten Lernbox.
 * @property sort True, wenn Karten alphabetisch sortiert.
 * @property model Das zugehörige Model, welches die Logik kapselt.
 *
 * @author Jessica Repty, Freja Sender, Semjon Nirmann
 */
class LearningModeViewModel(
    private val learningObjectId: UUID,
    private val learningBoxId: UUID,
    private val sort: Boolean,
    private val model: LearningModeModel = LearningModeModel()
) : ErrorHandlingViewModel() {

    val isFinished = mutableStateOf(false)
    val isShowingAnswer = mutableStateOf(false)
    val currentCard = mutableStateOf<CardEntity?>(null)
    val isLoading = mutableStateOf(true)
    var numberOfCardsRemaining = mutableStateOf(0)
    private var nextCards: Queue<CardEntity> = LinkedList()

    val learningBox = mutableStateOf<LearningBoxWitNrOfCards?>(null)
    var isLastBox = false
    var isFirstBox = false
    private var learningBoxesInObject: List<LearningBoxWitNrOfCards> = listOf()

    /**
     * Initialisiert viewModel Felder basierend auf der Rückmeldung des Models. Die Verarbeitung der Rückgabe vom Model
     * erfolgt mit Hilfe von Callbacks (onSuccess, ...), basierend auf der entsprechenden Rückgabe: Erfolg oder Fehler.
     *
     */
    fun onPageLoaded() {
        viewModelScope.launch {
            model.initializePage(
                learningObjectId = learningObjectId,
                learningBoxId = learningBoxId,
                sort = sort
            ).fold(
                onSuccess = {
                    learningBoxesInObject = it.learningBoxesInObject
                    isFirstBox = it.isFirstBox
                    isLastBox = it.isLastBox
                    nextCards = it.nextCards
                    learningBox.value = it.learningBox
                    nextCard()
                    isLoading.value = false
                },
                onValidationError = ::setValidationErrors,
                onUnexpectedError = ::setUnexpectedErrors,
            )
        }
    }

    /**
     * Zeige andere Seite der Karte.
     *
     */
    fun onFlipCardClicked() {
        isShowingAnswer.value = !isShowingAnswer.value
    }

    /**
     * Karte wird nicht verschoben und nächste Karte anzeigen.
     *
     */
    fun onCardStaysInBoxClicked() {
        nextCard()
    }

    /**
     * Karte wird in die nächste Lernbox des Lernobjektes geschoben, dann wird
     * die nächste Karte angezeigt.
     *
     */
    fun onCardGoesToNextBoxClicked() {
        isShowingAnswer.value = false

        val nextBoxNum = learningBox.value!!.boxNumber + 1
        if (nextBoxNum < learningBoxesInObject.size) {
            val nextBoxId = learningBoxesInObject[nextBoxNum].id

            viewModelScope.launch {
                model.moveCard(
                    fromBoxId = learningBoxId,
                    toBoxId = nextBoxId,
                    currentCardId = currentCard.value!!.id
                ).fold(
                    onSuccess = { nextCard() },
                    onValidationError = ::setValidationErrors,
                    onUnexpectedError = ::setUnexpectedErrors,
                )
            }
        }
    }

    /**
     * Karte wird in die vorherige Lernbox des Lernobjektes geschoben, dann wird
     * die nächste Karte angezeigt.
     *
     */
    fun onCardGoesToPreviousBoxClicked() {
        isShowingAnswer.value = false
        val previousBoxNr = learningBox.value!!.boxNumber - 1
        if (previousBoxNr >= 0) {
            val nextBoxId = learningBoxesInObject[previousBoxNr].id

            viewModelScope.launch {
                model.moveCard(
                    fromBoxId = learningBoxId,
                    toBoxId = nextBoxId,
                    currentCardId = currentCard.value!!.id
                ).fold(
                    onSuccess = { nextCard() },
                    onValidationError = ::setValidationErrors,
                    onUnexpectedError = ::setUnexpectedErrors,
                )
            }
        }
    }

    /**
     * Nächste zu lernende Karte wird in [currentCard] gespeichert.
     *
     */
    private fun nextCard() {
        if (nextCards.size > 0) {
            isShowingAnswer.value = false
            currentCard.value = nextCards.remove()
            numberOfCardsRemaining.value = nextCards.size + 1
        } else if (!isFinished.value) {
            isFinished.value = true
        }
    }
}