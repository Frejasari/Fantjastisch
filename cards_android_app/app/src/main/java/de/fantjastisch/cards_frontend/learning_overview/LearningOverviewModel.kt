package de.fantjastisch.cards_frontend.glossary

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.infrastructure.fold
import de.fantjastisch.cards_frontend.learning_object.LearningObject
import de.fantjastisch.cards_frontend.learning_object.LearningObjectRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LearningOverviewModel(
    private val learningObjectRepository: LearningObjectRepository = LearningObjectRepository()
) : ViewModel() {

    val learningObjects = mutableStateOf<List<LearningObject>>(emptyList())
    val error = mutableStateOf("")

    fun onPageLoaded() {
        viewModelScope.launch {

            learningObjectRepository.getAll().fold(
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