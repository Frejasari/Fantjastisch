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
    var isLoading = mutableStateOf(true)

    init {
        onPageLoaded()
    }

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