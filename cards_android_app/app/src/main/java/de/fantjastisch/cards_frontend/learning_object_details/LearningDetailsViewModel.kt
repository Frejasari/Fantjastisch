package de.fantjastisch.cards_frontend.learning_object_details

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.learning_box.LearningBox
import de.fantjastisch.cards_frontend.learning_box.LearningBoxRepository
import de.fantjastisch.cards_frontend.learning_box.card_to_learning_box.CardToLearningBoxRepository
import de.fantjastisch.cards_frontend.learning_object.LearningObjectRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class LearningDetailsViewModel(
    val learningObjectId: UUID,
    private val cardToLearningBoxRepository: CardToLearningBoxRepository = CardToLearningBoxRepository(),
    private val learningBoxRepository: LearningBoxRepository = LearningBoxRepository(),
    private val learningObjectRepository: LearningObjectRepository = LearningObjectRepository()
) : ViewModel() {

    val learningBoxes = mutableStateOf<List<LearningBox>>(emptyList())
    var learningObjectLabel = ""
    val error = mutableStateOf("")

    init {

        viewModelScope.launch(Dispatchers.IO) {
            learningBoxRepository.getAllBoxesForLearningObject(learningObjectId,
                onSuccess = { learningBoxes.value = it },
                onFailure = { error.value = "whoops" }
            )
            learningObjectRepository.findById(learningObjectId,
                onSuccess = { learningObjectLabel = it.label },
                onFailure = { error.value = "Konnte Lernobjekt nicht einholen" }
            )
        }
    }

    // TODO antipattern! So geht das nicht, das ist ein asynchroner call, ausserdem setzte das viewmodel state und diesen liest der view dann.
    fun getNumOfCardsFromLearningBox(learningBoxId: UUID): Int {
        // TODO und bitte programmieren in englisch :D
        var anzahl = 0;
        viewModelScope.launch(Dispatchers.IO) {
            cardToLearningBoxRepository.getNumOfCardsFromLearningBoxId(learningBoxId,
                onSuccess = { anzahl = it },
                onFailure = { error.value = "Konnte Anzahl Karten von Lernbox nicht einholen" })
        }
        return anzahl
    }
}