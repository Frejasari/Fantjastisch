package de.fantjastisch.cards_frontend.learning_overview

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.infrastructure.fold
import de.fantjastisch.cards_frontend.learning_object.LearningObject
import kotlinx.coroutines.launch

/**
 * Stellt die Daten für die [LearningOverview] bereit und nimmt seine Anfragen entgegen.
 *
 * @property model Das dazugehörige Model, welches die Logik kapselt.
 *
 * @author
 */
class LearningOverviewViewModel(
    private val model: LearningOverviewModel = LearningOverviewModel()
) : ViewModel() {

    val learningObjects = mutableStateOf<List<LearningObject>>(emptyList())
    val error = mutableStateOf("")

    /**
     * Lädt alle existierende Lernobjekte, indem [LearningOverviewModel] angefragt wird.
     *
     */
    fun onPageLoaded() {
        viewModelScope.launch {
            model.initializePage().fold(
                onSuccess = { learningObjects.value = it },
                onValidationError = { error.value = "Fehler bei der Eingabevalidierung." },
                onUnexpectedError = { error.value = "Ein unbekannter Fehler ist aufgetreten." }
            )
        }
    }

    init {
        onPageLoaded()
    }
}