package de.fantjastisch.cards_frontend.learning_overview.delete

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import de.fantjastisch.cards_frontend.learning_object.LearningObjectRepository
import java.util.*

class DeleteLearningObjectViewModel(
    private val learningObjectRepository: LearningObjectRepository = LearningObjectRepository(),
    private val learningObjectId: UUID
) : ViewModel() {

    val error = mutableStateOf<String?>(null)
    val isFinished = mutableStateOf(false)

    fun onDeleteClicked() {
        error.value = null
        learningObjectRepository.delete(
            id = learningObjectId,
            onSuccess = {
                isFinished.value = true
            },
            onFailure = {
                // Fehler anzeigen:
                error.value = "Irgendwas ist schief gelaufen"

            }
        )
    }

}