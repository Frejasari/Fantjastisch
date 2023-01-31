package de.fantjastisch.cards_frontend.learning_object_details

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.glossary.CardsFilters
import de.fantjastisch.cards_frontend.infrastructure.fold
import de.fantjastisch.cards_frontend.learning_box.LearningBoxRepository
import de.fantjastisch.cards_frontend.learning_box.LearningBoxWitNrOfCards
import de.fantjastisch.cards_frontend.learning_object.LearningObjectRepository
import kotlinx.coroutines.launch
import java.util.*

class LearningDetailsViewModel(
    val learningObjectId: UUID,
    private val learningBoxRepository: LearningBoxRepository = LearningBoxRepository(),
    private val learningObjectRepository: LearningObjectRepository = LearningObjectRepository()
) : ViewModel() {

    val learningBoxes = mutableStateOf<List<LearningBoxWitNrOfCards>>(emptyList())
    var learningObjectLabel = ""
    val error = mutableStateOf("")

    fun onPageLoaded() {
        viewModelScope.launch() {
            learningBoxRepository.getAllBoxesForLearningObject(learningObjectId)
                .fold(
                    onSuccess = { learningBoxes.value = it },
                    onValidationError = { error.value = "Fehler bei der Eingabevalidierung." },
                    onUnexpectedError = { error.value = "Ein unbekannter Fehler ist aufgetreten." }
                )
            learningObjectRepository.findById(learningObjectId,
                onSuccess = { learningObjectLabel = it.label },
                onFailure = { error.value = "Ein Netzwerkfehler ist aufgetreten." }
            )
        }
    }
}