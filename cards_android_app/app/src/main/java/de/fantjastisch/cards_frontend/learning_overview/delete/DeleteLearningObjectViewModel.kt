package de.fantjastisch.cards_frontend.learning_overview.delete

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.card.update_and_create.ErrorHandlingViewModel
import de.fantjastisch.cards_frontend.infrastructure.fold
import kotlinx.coroutines.launch
import java.util.*

class DeleteLearningObjectViewModel(
    private val learningObjectId: UUID,
    private val model: DeleteLearningObjectModel = DeleteLearningObjectModel()
) : ErrorHandlingViewModel() {

    val isFinished = mutableStateOf(false)

    fun onDeleteClicked() {
        viewModelScope.launch {
            model.deleteLearningObject(learningObjectId = learningObjectId)
                .fold(
                    onSuccess = {
                        isFinished.value = true
                    },
                    onValidationError = ::setValidationErrors,
                    onUnexpectedError = ::setUnexpectedErrors,
                )
        }
    }
}