package de.fantjastisch.cards_frontend.learning_object_details.sort_dialog


import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel


class LearningModeSortViewModel() : ViewModel() {

    val isFinished = mutableStateOf(false)
    val sort = mutableStateOf(false)
    val label = mutableStateOf("Zufällig")

    fun onDismissClicked() {
        isFinished.value = true
    }

    fun onSliderChange(isAlphabetic: Boolean) {
        sort.value = isAlphabetic
        label.value = if (isAlphabetic) "Alphabetisch" else "Zufällig"
    }
}
