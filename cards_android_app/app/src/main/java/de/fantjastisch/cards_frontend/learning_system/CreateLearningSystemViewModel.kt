package de.fantjastisch.cards_frontend.learning_system

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import de.fantjastisch.cards_frontend.category.CategorySelectItem
import org.openapitools.client.models.CreateLearningSystemEntity

class CreateLearningSystemViewModel(
        private val learningSystemRepository: LearningSystemRepository = LearningSystemRepository()
//= extends ViewModel
) : ViewModel() {

    // states, die vom view gelesen werden können -> automatisches Update vom View.

    val categories = mutableStateOf(listOf<CategorySelectItem>())
    val learningSystems = mutableStateOf(listOf<LearningSystemSelectItem>())
    val errors = mutableStateOf<String?>(null)
    val isFinished = mutableStateOf(false)

    val learningSystemLabel = mutableStateOf("")
    val learningSystemBoxLabels = mutableStateOf(mutableListOf<String>())
    val numBoxes = mutableStateOf(0)

    // constructor (wird ganz am Anfang aufgerufen)
    init {
    }

    fun onAddLearningSystemClicked() {
        errors.value = null
        learningSystemRepository.createLearningsystem(
                learningSystem = CreateLearningSystemEntity(
                        label = learningSystemLabel.value,
                        boxLabels = learningSystemBoxLabels.value,
                ),
                onSuccess = {
                    isFinished.value = true
                    // on Success -> dialog schliessen, zur Category  übersicht?
                },
                onFailure = {
                    // Fehler anzeigen:
                    errors.value = "There is an error"
                })
    }

    fun onBoxesSelected(numString: String) {
        val pattern = Regex("^\\d+\$")
        if (numString.isEmpty()) {
            numBoxes.value = 0
        } else if (numString.matches(pattern)) {
            numBoxes.value = numString.toInt()
            if (numBoxes.value > 10) {
                numBoxes.value = 10
            }

        }
        learningSystemBoxLabels.value = MutableList(numBoxes.value) { "" }
    }

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