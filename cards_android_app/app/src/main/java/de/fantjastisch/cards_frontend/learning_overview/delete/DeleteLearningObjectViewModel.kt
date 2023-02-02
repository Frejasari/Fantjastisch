package de.fantjastisch.cards_frontend.learning_overview.delete

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.infrastructure.fold
import de.fantjastisch.cards_frontend.learning_object.LearningObjectRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class DeleteLearningObjectViewModel(
    private val learningObjectId: UUID,
    private val model: DeleteLearningObjectModel = DeleteLearningObjectModel()
) : ViewModel() {

    val error = mutableStateOf<String?>(null)
    val isFinished = mutableStateOf(false)

    fun onDeleteClicked() {
        error.value = null
        viewModelScope.launch {
            model.deleteLearningObject(learningObjectId = learningObjectId).fold(
                onSuccess = {
                    isFinished.value = true
                },
                onValidationError = { error.value = "Fehler bei der Eingabevalidierung." },
                onUnexpectedError = { error.value = "Ein unbekannter Fehler ist aufgetreten." }
            )
        }
    }
}