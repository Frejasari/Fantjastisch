package de.fantjastisch.cards_frontend.learning_object_details.cards_view.edit_cards_in_box

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.card.CardSelectItem
import de.fantjastisch.cards_frontend.card.update_and_create.ErrorHandlingViewModel
import de.fantjastisch.cards_frontend.util.fold
import kotlinx.coroutines.launch
import java.util.*

/**
 * Stellt die Daten für die [EditCardsInBoxView] bereit und nimmt seine Anfragen entgegen.
 *
 * @property learningBoxId Die UUID der Lernbox, deren Karten editiert werden.
 * @property learningObjectId Die UUID des Lernobjekts, zu dem die Lernbox gehört.
 * @property model Das zugehörige Model, welches die Logik kapselt.
 *
 * @author Jessica Repty, Semjon Nirmann, Freja Sender
 */
class EditCardsInBoxViewModel(
    private val learningBoxId: UUID,
    private val learningObjectId: UUID,
    private val model: EditCardsInBoxModel = EditCardsInBoxModel()
) : ErrorHandlingViewModel() {

    val cards = mutableStateOf<List<CardSelectItem>>(mutableListOf())
    val isFinished = mutableStateOf(false)
    val isLoading = mutableStateOf(true)

    init {
        onPageLoaded()
    }

    /**
     * Lädt alle Karten, die zu einer Lernbox hinzugefügt werden können.
     *
     */
    fun onPageLoaded() {
        viewModelScope.launch {
            model.initializePage(learningBoxId = learningBoxId, learningObjectId = learningObjectId)
                .fold(
                    onSuccess = {
                        cards.value = it
                        isLoading.value = false
                    }
                )
        }
    }

    /**
     * Speichert die ausgewählten Karten als isChecked = true in [cards]
     *
     * @param id Die UUID der Karte, die ausgewählt wurde.
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
     * Wenn Karten zu einer Lernbox hinzugeügt werden -> [EditCardsInBoxModel] aktualisiert die Karten einer Lernbox.
     *
     */
    fun onAddCardsClicked() {
        val selectedCardsIds =
            cards.value
                .filter { card -> card.isChecked }
                .map { card -> card.card.id }

        viewModelScope.launch {
            model.updateCardsInBox(
                selectedCardsIds = selectedCardsIds,
                learningBoxId = learningBoxId
            ).fold(
                onSuccess = { isFinished.value = true }
            )
        }
    }
}