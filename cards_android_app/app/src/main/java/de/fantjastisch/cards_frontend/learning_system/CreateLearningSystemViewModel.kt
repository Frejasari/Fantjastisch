package de.fantjastisch.cards_frontend.learning_system

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.card.update_and_create.ErrorHandlingViewModel
import de.fantjastisch.cards_frontend.category.CategorySelectItem
import de.fantjastisch.cards_frontend.infrastructure.fold
import kotlinx.coroutines.launch


/**
 * Stellt die Daten für die [CreateLearningSystemView] bereit und nimmt seine Anfragen entgegen.
 *
 * @property model Das zugehörige Model, welches die Logik kapselt.
 *
 * @author
 */
class CreateLearningSystemViewModel(
    private val model: CreateLearningSystemModel = CreateLearningSystemModel()
) : ErrorHandlingViewModel() {

    // states, die vom view gelesen werden können -> automatisches Update vom View.

    val categories = mutableStateOf(listOf<CategorySelectItem>())
    val isFinished = mutableStateOf(false)

    val learningSystemLabel = mutableStateOf("")
    val learningSystemBoxLabels = mutableStateOf<List<String>>(listOf())
    val numBoxes = mutableStateOf(0)

    /**
     * Wenn Lernsystem gespeichert wird -> [CreateLearningSystemModel] erstellt das Lernsystem
     * mit den in den Variablen gespeicherten Daten und sendet die Anfrage an die Datenbank.
     *
     */
    fun onAddLearningSystemClicked() {
        viewModelScope.launch {
            model.addLearningSystem(
                learningSystemLabel = learningSystemLabel.value,
                learningSystemBoxLabels = learningSystemBoxLabels.value
            ).fold(
                onSuccess = {
                    isFinished.value = true
                },
                onValidationError = ::setValidationErrors,
                onUnexpectedError = ::setUnexpectedErrors,
            )
        }
    }

    /**
     * Ermittelt die Anzahl von Lernboxen eines Lernsystems und erzeugt eine leere Liste gleicher
     * Länge für die Bezeichnungen dieser.
     *
     * @param numString die Anzahl der Lernboxen als String
     */
    fun onBoxesSelected(numString: String) {
        numBoxes.value = model.getNumOfBoxes(numString)
        learningSystemBoxLabels.value = List(numBoxes.value) { "" }
    }

    /**
     * Speichert den übergebenen String als die Bezeichnung einer Lernbox.
     *
     * @param index Der Index der Lernbox innerhalb des Lernsystems
     * @param element Neue Bezeichnung der Lernbox.
     */
    fun onBoxLabelChanged(index: Int, element: String) {
        this.learningSystemBoxLabels.value = learningSystemBoxLabels.value.mapIndexed { i, value ->
            if (i == index) {
                element
            } else {
                value
            }
        }.toMutableList()
    }

}