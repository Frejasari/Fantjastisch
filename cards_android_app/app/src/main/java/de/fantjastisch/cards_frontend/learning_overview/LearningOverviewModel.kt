package de.fantjastisch.cards_frontend.glossary

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import de.fantjastisch.cards_frontend.learning_object.LearningObject
import de.fantjastisch.cards_frontend.learning_object.LearningObjectRepository

class LearningOverviewModel(
    private val learningObjectRepository: LearningObjectRepository = LearningObjectRepository()
) : ViewModel() {

    val learningObjects = mutableStateOf<List<LearningObject>>(emptyList())
    val error = mutableStateOf("")

    fun onPageLoaded() {
        learningObjectRepository.getAll(
            onSuccess = { learningObjects.value = it },
            onFailure = { error.value = "whoops" }
        )
    }

    init {
        onPageLoaded()
    }
}