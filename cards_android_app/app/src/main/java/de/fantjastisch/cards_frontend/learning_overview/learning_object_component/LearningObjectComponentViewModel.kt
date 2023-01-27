package de.fantjastisch.cards_frontend.learning_overview.learning_object_component

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.infrastructure.RepoResult
import de.fantjastisch.cards_frontend.learning_box.LearningBoxRepository
import de.fantjastisch.cards_frontend.learning_system.LearningSystemRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import org.openapitools.client.models.LearningSystemEntity
import java.util.*
import kotlin.math.roundToInt

class LearningObjectComponentViewModel(
    private val learningSystemRepository: LearningSystemRepository = LearningSystemRepository(),
    private val learningBoxRepository: LearningBoxRepository = LearningBoxRepository(),
    private val learningSystemId: UUID,
    private val learningObjectId: UUID
) : ViewModel() {

    private var countOfCards = 0;
    val error = mutableStateOf("")
    val progress = mutableStateOf<Int>(0)
    val learningSystemLabel = mutableStateOf("Loading")

    init {
        onPageLoaded()
    }

    private fun onPageLoaded() {
        viewModelScope.launch {

            val (learningSystemResult, progressResult) = awaitAll(
                async { learningSystemRepository.getLearningSystem(learningSystemId) },
                async {
                    learningBoxRepository.getCardsFromLearningBoxInLearningObject(
                        learningObjectId
                    )
                }
            )

            @Suppress("UNCHECKED_CAST")
            when {
                learningSystemResult is RepoResult.Success<*>
                        && progressResult is RepoResult.Success<*> -> {

                    val learningSystem = learningSystemResult.result as LearningSystemEntity
                    val listOfCardAmountsInBoxes = progressResult.result as List<Int>
                    Log.v("LearningSystem", "label: " + learningSystem.label)
                    learningSystemLabel.value = learningSystem.label
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
                }
                else -> {
                    error.value = "Ein Netzwerkfehler ist aufgetreten."
                }
            }
        }
    }

}