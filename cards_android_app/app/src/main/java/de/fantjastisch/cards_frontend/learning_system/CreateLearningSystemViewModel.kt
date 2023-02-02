package de.fantjastisch.cards_frontend.learning_system

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.category.CategorySelectItem
import de.fantjastisch.cards_frontend.infrastructure.fold
import de.fantjastisch.cards_frontend.util.ErrorsEnum
import kotlinx.coroutines.launch
import org.openapitools.client.models.ErrorEntryEntity

class CreateLearningSystemViewModel(
    private val model: CreateLearningSystemModel = CreateLearningSystemModel()
) : ViewModel() {

    // states, die vom view gelesen werden können -> automatisches Update vom View.

    val categories = mutableStateOf(listOf<CategorySelectItem>())
    val error = mutableStateOf(ErrorsEnum.NO_ERROR)
    val errors = mutableStateOf<List<ErrorEntryEntity>>(mutableListOf())
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
                onValidationError = { resultErrors -> errors.value = resultErrors },
                onUnexpectedError = { error.value = ErrorsEnum.UNEXPECTED },
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