package de.fantjastisch.cards_frontend.learning_object_details

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import de.fantjastisch.cards_frontend.learning_box.LearningBox
import de.fantjastisch.cards_frontend.learning_box.LearningBoxRepository
import de.fantjastisch.cards_frontend.learning_box.card_to_learning_box.CardToLearningBoxRepository
import de.fantjastisch.cards_frontend.learning_object.LearningObjectRepository
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
        learningBoxRepository.getAllBoxesForLearningObject(learningObjectId,
            onSuccess = { learningBoxes.value = it },
            onFailure = { error.value = "whoops" }
        )
        learningObjectRepository.findById(learningObjectId,
            onSuccess = { learningObjectLabel = it.label },
            onFailure = { error.value = "Konnte Lernobjekt nicht einholen" }
        )
    }

    fun getNumOfCardsFromLearningBox(learningBoxId: UUID): Int {
        var anzahl = 0;
        cardToLearningBoxRepository.getNumOfCardsFromLearningBoxId(learningBoxId,
            onSuccess = { anzahl = it },
            onFailure = { error.value = "Konnte Anzahl Karten von Lernbox nicht einholen" })
        return anzahl
    }
}