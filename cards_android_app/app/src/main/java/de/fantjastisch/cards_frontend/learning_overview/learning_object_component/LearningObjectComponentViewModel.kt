package de.fantjastisch.cards_frontend.learning_overview.learning_object_component

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import de.fantjastisch.cards_frontend.config.AppDatabase
import de.fantjastisch.cards_frontend.learning_box.InternalLearningBoxRepository
import de.fantjastisch.cards_frontend.learning_box.LearningBoxRepository
import de.fantjastisch.cards_frontend.learning_system.LearningSystemRepository
import java.lang.Integer.max
import java.lang.Integer.min
import java.util.*
import kotlin.math.floor
import kotlin.math.roundToInt

class LearningObjectComponentViewModel(
    private val learningSystemRepository: LearningSystemRepository = LearningSystemRepository(),
    private val learningBoxRepository: LearningBoxRepository = LearningBoxRepository(
        InternalLearningBoxRepository(AppDatabase.database.learningBoxDao())
    ),
    private val learningSystemId: UUID
) : ViewModel() {

    var countOfCards = 0;
    var learningSystemlabel = ""
    val error = mutableStateOf("")

    init {
        onPageLoaded()
    }

    fun onPageLoaded() {
        initLearningSystemLabel(learningSystemId = learningSystemId)
    }

    fun initLearningSystemLabel(learningSystemId: UUID): String {
        learningSystemRepository.getLearningSystem(learningSystemId,
            onSuccess = {
                learningSystemlabel = it.label
            },
            onFailure = { error.value = "Konnte kein Lernsystemlabel einholen." }
        )
        return learningSystemlabel
    }

    fun getProgressFromLearningObject(learningObjectId: UUID) : Int {
        var progress = 0
        learningBoxRepository.getCardsFromLearningBoxInLearningObject(learningObjectId,
            onSuccess = {
                countOfCards = it.sum()
                val numBoxes = it.size
                if (numBoxes == 1) {
                    progress = 100
                } else if (countOfCards > 0 ) {
                    it.forEachIndexed { boxIndex, numberOfCardsInBox ->
                        progress += ((1.0 / countOfCards) * (100 * (boxIndex / (numBoxes - 1)))).roundToInt()
                    }
                }
            },
            onFailure = { error.value = "Fehler" }
        )
        return min(progress, 100)
    }
}