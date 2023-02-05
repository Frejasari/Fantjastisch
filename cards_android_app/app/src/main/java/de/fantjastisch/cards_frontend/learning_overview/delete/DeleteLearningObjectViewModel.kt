package de.fantjastisch.cards_frontend.learning_overview.delete

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.card.update_and_create.ErrorHandlingViewModel
import de.fantjastisch.cards_frontend.util.fold
import kotlinx.coroutines.launch
import java.util.*

/**
 * Stellt die Daten für die [DeleteLearningObjectDialog] bereit und nimmt seine Anfragen entgegen.
 *
 * @property learningObjectId Die UUID des zu löschenden Lernobjekts.
 * @property model Das dazugehörige Model, welches die Logik kapselt.
 */
class DeleteLearningObjectViewModel(
    private val learningObjectId: UUID,
    private val model: DeleteLearningObjectModel = DeleteLearningObjectModel()
) : ErrorHandlingViewModel() {

    val isFinished = mutableStateOf(false)

    /**
     * Löscht ein Lernobjekt, indem [DeleteLearningObjectModel] angefragt wird.
     *
     */
    fun onDeleteClicked() {
        viewModelScope.launch {
            model.deleteLearningObject(learningObjectId = learningObjectId)
                .fold(
                    onSuccess = {
                        isFinished.value = true
                    }
                )
        }
    }
}