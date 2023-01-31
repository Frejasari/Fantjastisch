package de.fantjastisch.cards_frontend.learning_object_details


import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel


class LearningModeSortViewModel() : ViewModel() {

    val isFinished = mutableStateOf(false)
    val sort = mutableStateOf(false)


    fun onDismissClicked() {
        isFinished.value = true
    }
}
