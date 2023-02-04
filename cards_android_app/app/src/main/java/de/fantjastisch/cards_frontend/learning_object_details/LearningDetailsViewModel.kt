package de.fantjastisch.cards_frontend.learning_object_details

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.card.update_and_create.ErrorHandlingViewModel
import de.fantjastisch.cards_frontend.infrastructure.fold
import de.fantjastisch.cards_frontend.learning_box.LearningBoxWitNrOfCards
import kotlinx.coroutines.launch
import java.util.*

class LearningDetailsViewModel(
    val learningObjectId: UUID,
    val model: LearningDetailsModel = LearningDetailsModel()
) : ErrorHandlingViewModel() {
    val learningBoxes = mutableStateOf<List<LearningBoxWitNrOfCards>>(emptyList())
    var learningObjectLabel = ""


    fun onPageLoaded() {
        viewModelScope.launch() {
            model.initializePage(learningObjectId = learningObjectId).fold(
                onSuccess = {
                    learningBoxes.value = it.learningBoxes
                    learningObjectLabel = it.learningObjectLabel
                },
                onValidationError = ::setValidationErrors,
                onUnexpectedError = ::setUnexpectedErrors,
            )
        }
    }
}