package de.fantjastisch.cards_frontend.learning_overview.learning_object_component

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.learning_box.LearningBoxRepository
import de.fantjastisch.cards_frontend.learning_system.LearningSystemRepository
import kotlinx.coroutines.launch
import java.util.*
import kotlin.math.roundToInt

class LearningObjectComponentViewModel(
    private val learningSystemRepository: LearningSystemRepository = LearningSystemRepository(),
    private val learningBoxRepository: LearningBoxRepository = LearningBoxRepository(),
    private val learningSystemId: UUID,
    private val learningObjectId: UUID
) : ViewModel() {

    var countOfCards = 0;
    var learningSystemlabel = ""
    val error = mutableStateOf("")

    val progress = mutableStateOf<Int>(0)
    val learningSystemLabel = mutableStateOf("")

    init {
        onPageLoaded()
    }

    fun onPageLoaded() {
        initLearningSystemLabel()
        loadProgressFromLearningObject()
    }

    // TODO antipattern! AND it wont necessarily work like this! -> cannot just return after an asynchronous call. Set a state insetad
    private fun initLearningSystemLabel(): String {
        viewModelScope.launch {

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
    private fun loadProgressFromLearningObject() {

        viewModelScope.launch {
            learningBoxRepository.getCardsFromLearningBoxInLearningObject(learningObjectId,
                onSuccess = { listOfCardAmountsInBoxes ->
                    var progressInternal = 0
                    countOfCards = listOfCardAmountsInBoxes.sum()
                    val numBoxes = listOfCardAmountsInBoxes.size
                    if (numBoxes == 1) {
                        progressInternal = 100
                    } else if (countOfCards > 0) {
                        listOfCardAmountsInBoxes.forEachIndexed { boxIndex, numberOfCardsInBox ->
                            val ratioOfBoxCardsToTotalCards =
                                (numberOfCardsInBox * 1.0 / countOfCards)
                            val progressPercentageForBox = (boxIndex * 1.0 / (numBoxes - 1))
                            progressInternal += (ratioOfBoxCardsToTotalCards * progressPercentageForBox * 100).roundToInt()

                        }
                    }
                    progress.value = progressInternal
                },
                onFailure = { error.value = "Fehler" }
            )
        }
    }
}