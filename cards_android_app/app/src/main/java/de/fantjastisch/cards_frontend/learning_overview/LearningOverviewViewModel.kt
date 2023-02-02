package de.fantjastisch.cards_frontend.learning_overview

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.infrastructure.fold
import de.fantjastisch.cards_frontend.learning_object.LearningObject
import kotlinx.coroutines.launch

class LearningOverviewViewModel(
    private val model: LearningOverviewModel = LearningOverviewModel()
) : ViewModel() {

    val learningObjects = mutableStateOf<List<LearningObject>>(emptyList())
    val error = mutableStateOf("")

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