package de.fantjastisch.cards_frontend.learning_system

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.card.update_and_create.ErrorHandlingViewModel
import de.fantjastisch.cards_frontend.category.CategorySelectItem
import de.fantjastisch.cards_frontend.infrastructure.fold
import kotlinx.coroutines.launch

class CreateLearningSystemViewModel(
    private val model: CreateLearningSystemModel = CreateLearningSystemModel()
) : ErrorHandlingViewModel() {

    // states, die vom view gelesen werden können -> automatisches Update vom View.

    val categories = mutableStateOf(listOf<CategorySelectItem>())
    val isFinished = mutableStateOf(false)

    val learningSystemLabel = mutableStateOf("")
    val learningSystemBoxLabels = mutableStateOf<List<String>>(listOf())
    val numBoxes = mutableStateOf(0)

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

    fun onBoxesSelected(numString: String) {
        numBoxes.value = model.getNumOfBoxes(numString)
        learningSystemBoxLabels.value = List(numBoxes.value) { "" }
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