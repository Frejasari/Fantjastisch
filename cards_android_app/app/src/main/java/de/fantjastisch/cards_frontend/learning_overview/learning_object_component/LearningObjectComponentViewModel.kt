package de.fantjastisch.cards_frontend.learning_overview.learning_object_component

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.learning_box.LearningBoxRepository
import de.fantjastisch.cards_frontend.learning_system.LearningSystemRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Integer.min
import java.util.*
import kotlin.math.roundToInt

class LearningObjectComponentViewModel(
    private val learningSystemRepository: LearningSystemRepository = LearningSystemRepository(),
    private val learningBoxRepository: LearningBoxRepository = LearningBoxRepository(),
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

    // TODO antipattern! AND it wont necessarily work like this! -> cannot just return after an asynchronous call. Set a state insetad
    fun initLearningSystemLabel(learningSystemId: UUID): String {
        viewModelScope.launch(Dispatchers.IO) {

            learningSystemRepository.getLearningSystem(learningSystemId,
                onSuccess = {
                    learningSystemlabel = it.label
                },
                onFailure = { error.value = "Konnte kein Lernsystemlabel einholen." }
            )
        }
        return learningSystemlabel
    }

    // TODO antipattern! AND it wont necessarily work like this! -> cannot just return after an asynchronous call. Set a state insetad
    fun getProgressFromLearningObject(learningObjectId: UUID): Int {
        var progress = 0
        viewModelScope.launch(Dispatchers.IO) {
            learningBoxRepository.getCardsFromLearningBoxInLearningObject(learningObjectId,
                onSuccess = {
                    countOfCards = it.sum()
                    val numBoxes = it.size
                    if (numBoxes == 1) {
                        progress = 100
                    } else if (countOfCards > 0) {
                        it.forEachIndexed { boxIndex, numberOfCardsInBox ->
                            progress += ((1.0 / countOfCards) * (100 * (boxIndex / (numBoxes - 1)))).roundToInt()
                        }
                    }
                },
                onFailure = { error.value = "Fehler" }
            )
        }
        return min(progress, 100)
    }
}