package de.fantjastisch.cards_frontend.learning_overview.learning_object_component

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.infrastructure.fold
import kotlinx.coroutines.launch
import java.util.*

class LearningObjectComponentViewModel(
    private val learningSystemId: UUID,
    private val learningObjectId: UUID,
    private val model: LearningObjectComponentModel = LearningObjectComponentModel()
) : ViewModel() {

    val error = mutableStateOf("")
    val progress = mutableStateOf(0)
    val learningSystemLabel = mutableStateOf("")
    val isLoading = mutableStateOf(true)
    init {
        onPageLoaded()
    }

    private fun onPageLoaded() {
        viewModelScope.launch {
            model.initializePage(
                learningSystemId = learningSystemId,
                learningObjectId = learningObjectId
            ).fold(
                onSuccess = {
                    learningSystemLabel.value = it.learningSystemLabel
                    progress.value = it.progress
                },
                onValidationError = { error.value = "Fehler bei der Eingabevalidierung." },
                onUnexpectedError = { error.value = "Ein unbekannter Fehler ist aufgetreten." }
            )
            isLoading.value = false
        }

    }
}