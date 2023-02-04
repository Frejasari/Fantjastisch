package de.fantjastisch.cards_frontend.learning_overview.learning_object_component

import de.fantjastisch.cards_frontend.infrastructure.RepoResult
import de.fantjastisch.cards_frontend.learning_box.LearningBoxRepository
import de.fantjastisch.cards_frontend.learning_system.LearningSystemRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.openapitools.client.models.LearningSystemEntity
import java.util.*
import kotlin.math.roundToInt

/**
 * Kapselt die Logik für das [LearningObjectComponentViewModel]
 * Fungiert als Vermittler zwischen Repository und ViewModel
 *
 * @property learningSystemRepository Lernsystem Repository
 * @property learningBoxRepository Lernbox Repository
 *
 * @author
 */
class LearningObjectComponentModel(
    private val learningSystemRepository: LearningSystemRepository = LearningSystemRepository(),
    private val learningBoxRepository: LearningBoxRepository = LearningBoxRepository()
) {
    /**
     * Hält die Informationen für die Darstellung eines Lernobjekts
     *
     * @property progress
     * @property learningSystemLabel Die Bezeichnung des Lernsystems, welches das LearningObject hat
     */
    data class LearningObjectComponent(
        val progress: Int,
        val learningSystemLabel: String
    )

    /**
     * Stellt ein [LearningObjectComponent] zusammen, in dem ein Lernsystem aus der Datenbank
     * und die Anzahl an Karten in Boxen eines Lernobjekts geholt werden.
     * Setzt den Lernfortschritt eines Lernobjekts.
     *
     * @param learningSystemId UUID des Lernsystems, welches zu dem [LearningObject] gehört.
     * @param learningObjectId UUID des zugrundeliegende Lernobjekts für [LearningObjectComponent]
     * @return RepoResult<LearningObjectComponent> OnSuccess: der neu erstellte [LearningObjectComponent]
     */
    @Suppress("UNCHECKED_CAST")
    suspend fun initializePage(
        learningSystemId: UUID,
        learningObjectId: UUID
    ): RepoResult<LearningObjectComponent> = coroutineScope {
        // Runs coroutines in parallel and waits until all of them are done
        val (learningSystemResult, progressResult) = awaitAll(
            async { learningSystemRepository.getLearningSystem(learningSystemId) },
            async {
                learningBoxRepository.getCardsFromLearningBoxInLearningObject(
                    learningObjectId
                )
            }
        )
        when {
            learningSystemResult is RepoResult.Success &&
                    progressResult is RepoResult.Success -> {
                val listOfCardAmountsInBoxes = progressResult.result as List<Int>
                val progress =
                    calculateProgress(listOfCardAmountsInBoxes = listOfCardAmountsInBoxes)
                val learningSystem = learningSystemResult.result as LearningSystemEntity
                RepoResult.Success(
                    LearningObjectComponent(
                        progress = progress,
                        learningSystemLabel = learningSystem.label
                    )
                )
            }
            else -> RepoResult.ServerError()
        }
    }

    /**
     * Berechnet den Lernfortschritt eines Lernobjekts.
     *
     * @param listOfCardAmountsInBoxes Die Liste von Anzahl der Karten in jeder box eines Lernobjekts.
     * @return Prozentuale Angabe des Lernfortschritts
     */
    private fun calculateProgress(listOfCardAmountsInBoxes: List<Int>): Int {
        var progressInternal = 0
        val countOfCards = listOfCardAmountsInBoxes.sum()
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
        return progressInternal
    }
}