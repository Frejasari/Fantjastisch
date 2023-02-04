package de.fantjastisch.cards_frontend.learning_object_details.cards_view.move_cards_to_box

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.card.CardSelectItem
import de.fantjastisch.cards_frontend.card.update_and_create.ErrorHandlingViewModel
import de.fantjastisch.cards_frontend.infrastructure.fold
import de.fantjastisch.cards_frontend.learning_box.LearningBoxWitNrOfCards
import kotlinx.coroutines.launch
import java.util.*

class MoveCardsToBoxViewModel(
    private val learningBoxId: UUID,
    private val learningObjectId: UUID,
    private val model: MoveCardsToBoxModel = MoveCardsToBoxModel()
) : ErrorHandlingViewModel() {

    val cards = mutableStateOf<List<CardSelectItem>>(mutableListOf())
    val isFinished = mutableStateOf(false)
    private val learningBoxesInObject =
        mutableStateOf<List<LearningBoxWitNrOfCards>>(mutableListOf())
    val learningBoxNum = mutableStateOf(-1)
    var isLastBox = mutableStateOf(false)
    var isFirstBox = mutableStateOf(false)
    val isLoading = mutableStateOf(true)

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
                        isFirstBox.value = it.isFirstBox
                        isLastBox.value = it.isLastBox
                    },
                    onValidationError = ::setValidationErrors,
                    onUnexpectedError = ::setUnexpectedErrors
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
                onValidationError = ::setValidationErrors,
                onUnexpectedError = ::setUnexpectedErrors
            )
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
                onValidationError = ::setValidationErrors,
                onUnexpectedError = ::setUnexpectedErrors,
            )
        }
    }
}