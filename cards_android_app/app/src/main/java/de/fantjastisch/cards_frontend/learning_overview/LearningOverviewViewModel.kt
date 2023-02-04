package de.fantjastisch.cards_frontend.learning_overview

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.card.update_and_create.ErrorHandlingViewModel
import de.fantjastisch.cards_frontend.infrastructure.fold
import de.fantjastisch.cards_frontend.learning_object.LearningObject
import kotlinx.coroutines.launch

/**
 * Stellt die Daten für die [LearningOverviewView] bereit und nimmt seine Anfragen entgegen.
 *
 * @property model Das dazugehörige Model, welches die Logik kapselt.
 *
 * @author Freja Sender, Semjon Nirmann, Jessica Repty
 */
class LearningOverviewViewModel(
    private val model: LearningOverviewModel = LearningOverviewModel()
) : ErrorHandlingViewModel() {

    val learningObjects = mutableStateOf<List<LearningObject>>(emptyList())

    /**
     * Lädt alle existierende Lernobjekte, indem [LearningOverviewModel] angefragt wird.
     *
     */
    fun onPageLoaded() {
        viewModelScope.launch {
            model.initializePage().fold(
                onSuccess = { learningObjects.value = it },
                onValidationError = ::setValidationErrors,
                onUnexpectedError = ::setUnexpectedErrors,
            )
        }
    }
}