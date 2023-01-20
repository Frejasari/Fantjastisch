package de.fantjastisch.cards_frontend.glossary

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import de.fantjastisch.cards_frontend.components.SingleSelectItem
import de.fantjastisch.cards_frontend.config.AppDatabase
import de.fantjastisch.cards_frontend.learning_box.InternalLearningBoxRepository
import de.fantjastisch.cards_frontend.learning_box.LearningBox
import de.fantjastisch.cards_frontend.learning_box.LearningBoxRepository
import de.fantjastisch.cards_frontend.learning_box.card_to_learning_box.CardToLearningBoxRepository
import de.fantjastisch.cards_frontend.learning_box.card_to_learning_box.InternalCardToLearningBoxRepository
import de.fantjastisch.cards_frontend.learning_object.InternalLearningObjectRepository
import de.fantjastisch.cards_frontend.learning_object.LearningObject
import de.fantjastisch.cards_frontend.learning_object.LearningObjectRepository
import de.fantjastisch.cards_frontend.learning_system.LearningSystemRepository
import org.openapitools.client.models.CardEntity
import org.openapitools.client.models.LearningSystemEntity
import java.util.*

class LearningOverviewModel(
    private val learningObjectRepository: LearningObjectRepository = LearningObjectRepository(
        InternalLearningObjectRepository(AppDatabase.database.learningObjectDao())
    ),
    private val learningSystemRepository: LearningSystemRepository = LearningSystemRepository(),
    private val cardToLearningBoxRepository: CardToLearningBoxRepository = CardToLearningBoxRepository(
        InternalCardToLearningBoxRepository(AppDatabase.database.cardToLearningBoxDao())
    ),
    private val learningBoxRepository: LearningBoxRepository = LearningBoxRepository(
        InternalLearningBoxRepository(AppDatabase.database.learningBoxDao())
    )
) : ViewModel() {

    val learningObjects = mutableStateOf<List<LearningObject>>(emptyList())
    var progress = 0;
    var countCards = 0;
    var learningSystemlabel = ""
    val error = mutableStateOf("")

    fun onPageLoaded() {
        learningObjectRepository.getAll(
            onSuccess = { learningObjects.value = it },
            onFailure = { error.value = "whoops" }
        )
    }

    fun getLearningSystemLabel(learningSystemId: UUID) :String {
        learningSystemRepository.getLearningSystem(learningSystemId,
            onSuccess = { learningSystemlabel = it.label },
            onFailure = { error.value = "Konnte kein Lernsystemlabel einholen."}
        )
        return learningSystemlabel
    }

    fun getProgressFromLearningObject(learningObjectId: UUID) :Int {
        learningBoxRepository.getCardsFromLearningBoxInLearningObject(learningObjectId,
                    onSuccess = {
                        it.forEachIndexed { index, numberOfCards ->
                            if (index == 0) {
                                progress = 0
                                countCards += numberOfCards
                            }
                            else {
                                progress += (numberOfCards * ((index - 1) / (it.size - 1)) * 100)
                                countCards += numberOfCards
                            }}},
                    onFailure = { error.value = "Fehler" }
        )
        return (progress/countCards)
    }
    init {
        onPageLoaded()
    }
}