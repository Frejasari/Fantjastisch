package de.fantjastisch.cards_frontend.learning_object_details

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.card.update_and_create.ErrorHandlingViewModel
import de.fantjastisch.cards_frontend.infrastructure.fold
import de.fantjastisch.cards_frontend.learning_box.LearningBoxWitNrOfCards
import kotlinx.coroutines.launch
import java.util.*

/**
 * Stellt die Daten für die [LearningDetailsView] bereit und nimmt seine Anfragen entgegen.
 *
 * @property learningObjectId Die UUID des Lernobjekts, welches dargestellt werden soll.
 * @property model Das dazugehörige Model, welches die Logik kapselt.
 *
 * @author Jessica Repty, Semjon Nirmann, Freja Sender
 */
class LearningDetailsViewModel(
    val learningObjectId: UUID,
    val model: LearningDetailsModel = LearningDetailsModel()
) : ErrorHandlingViewModel() {
    val learningBoxes = mutableStateOf<List<LearningBoxWitNrOfCards>>(emptyList())
    var learningObjectLabel = ""


    /**
     * Lädt die internen Daten (Liste von Lernboxen mit Anzahl an Karten und die Bezeichnung)
     * eines Lernobjekts.
     *
     */
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