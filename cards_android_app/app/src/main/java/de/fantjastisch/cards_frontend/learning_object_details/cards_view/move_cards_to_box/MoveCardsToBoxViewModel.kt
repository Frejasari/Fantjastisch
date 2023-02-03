package de.fantjastisch.cards_frontend.learning_object_details.cards_view.move_cards_to_box

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.card.CardSelectItem
import de.fantjastisch.cards_frontend.infrastructure.fold
import de.fantjastisch.cards_frontend.learning_box.LearningBoxWitNrOfCards
import kotlinx.coroutines.launch
import org.openapitools.client.models.ErrorEntryEntity
import java.util.*

/**
 * Stellt die Daten für die [MoveCardsToBoxView] bereit und nimmt seine Anfragen entgegen.
 *
 * @property learningBoxId Die UUID der Lernbox, von der die Karten verschoben werden sollen.
 * @property learningObjectId Die UUID des Lernobjekts, zu dem die Lernbox gehört.
 * @property model Das zugehörige Model, welches die Logik kapselt.
 *
 * @author
 */
class MoveCardsToBoxViewModel(
    private val learningBoxId: UUID,
    private val learningObjectId: UUID,
    private val model: MoveCardsToBoxModel = MoveCardsToBoxModel()
) : ViewModel() {

    val cards = mutableStateOf<List<CardSelectItem>>(mutableListOf())
    val errors = mutableStateOf<List<ErrorEntryEntity>>(emptyList())
    val error = mutableStateOf<String?>(null)
    val isFinished = mutableStateOf(false)
    private val learningBoxesInObject = mutableStateOf<List<LearningBoxWitNrOfCards>>(mutableListOf())
    val learningBoxNum = mutableStateOf(-1)
    var isLastBox = false
    var isFirstBox = false
    val isLoading = mutableStateOf(true)

    init {
        onPageLoaded()
    }

    /**
     * Speichert alle Karten, die verschoben werden können in [cards]
     * Speichert alle Lernboxen eines Lernobjekts in [learningBoxesInObject]
     * Speichert den Index der Lernbox, deren Karten verschoben werden soll in [learningBoxNum].
     * Speichert, ob die Lernbox die erste (in [isFirstBox]) oder die letzte (in [isLastBox]) ist.
     *
     */
    fun onPageLoaded() {
        viewModelScope.launch {
            model.initializePage(learningBoxId = learningBoxId, learningObjectId = learningObjectId)
                .fold(
                    onSuccess = {
                        cards.value = it.cards
                        learningBoxesInObject.value = it.learningBoxes
                        learningBoxNum.value = it.learningBoxNum
                        isFirstBox = it.isFirstBox
                        isLastBox = it.isLastBox
                    },
                    onValidationError = { error.value = "Fehler bei der Eingabevalidierung." },
                    onUnexpectedError = { error.value = "Ein unbekannter Fehler ist aufgetreten." }
                )
            isLoading.value = false
        }
    }

    /**
     * Speichert die ausgewählten Karten als isChecked = true in [cards].
     *
     * @param id Id der Kategorie, welche neu ausgewählt wurde.
     */
    fun onCardSelected(id: UUID) {
        cards.value = cards.value.map {
            if (it.card.id == id) {
                it.copy(isChecked = !it.isChecked)
            } else {
                it
            }
        }
    }

    /**
     * Verschiebt Karten in die vorherige Lernbox und aktualisiert die Karten
     * der Lernbox, von der die Karten verschoben wurden.
     * Für das Verschieben wird die Anfrage an das Repository weitergeleitet.
     *
     */
    fun onMoveToPreviousBox() {
        val previousBoxId = learningBoxesInObject.value[learningBoxNum.value - 1].id

        viewModelScope.launch {
            model.moveToPreviousBox(
                cards = cards.value,
                previousBoxId = previousBoxId,
                learningBoxId = learningBoxId
            ).fold(
                onSuccess = { onPageLoaded() },
                onValidationError = { error.value = "Fehler bei der Eingabevalidierung." },
                onUnexpectedError = {
                    error.value = "Ein unbekannter Fehler ist aufgetreten."
                })
        }
    }

    /**
     * Verschiebt Karten in die nächste Lernbox und aktualisiert die Karten
     * der Lernbox, von der die Karten verschoben wurden.
     * Für das Verschieben wird die Anfrage an das Repository weitergeleitet.
     *
     */
    fun onMoveToNextBox() {
        val nextBoxId = learningBoxesInObject.value[learningBoxNum.value + 1].id
        viewModelScope.launch {
            model.moveToNextBox(
                cards = cards.value,
                nextBoxId = nextBoxId,
                learningBoxId = learningBoxId
            ).fold(
                onSuccess = { onPageLoaded() },
                onValidationError = { error.value = "Fehler bei der Eingabevalidierung." },
                onUnexpectedError = {
                    error.value = "Ein unbekannter Fehler ist aufgetreten."
                })
        }
    }
}