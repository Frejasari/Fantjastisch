package de.fantjastisch.cards_frontend.learning_object_details

import de.fantjastisch.cards_frontend.learning_box.LearningBoxRepository
import de.fantjastisch.cards_frontend.learning_box.LearningBoxWithNrOfCards
import de.fantjastisch.cards_frontend.learning_object.LearningObject
import de.fantjastisch.cards_frontend.learning_object.LearningObjectRepository
import de.fantjastisch.cards_frontend.util.RepoResult
import de.fantjastisch.cards_frontend.util.RepoResult.*
import de.fantjastisch.cards_frontend.util.RepoResult.UnexpectedErrorType.*
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import java.util.*

/**
 * Kapselt die Logik für [LearningDetailsViewModel]
 * Fungiert als Vermittler zwischen Repository und ViewModel
 *
 * @property learningBoxRepository Lernbox Repository
 * @property learningObjectRepository Lernobjekt Repository
 *
 * @author Semjon Nirmann,  Freja Sender
 */
class LearningDetailsModel(
    private val learningBoxRepository: LearningBoxRepository = LearningBoxRepository(),
    private val learningObjectRepository: LearningObjectRepository = LearningObjectRepository()
) {

    /**
     * Hält die interne Daten eines Lernobjekts
     *
     * @property learningBoxes Liste der Anzahl an Karten pro Lernbox
     * @property learningObjectLabel Die Bezeichnung des Lernobjekts
     */
    data class LearningDetails(
        val learningBoxes: List<LearningBoxWithNrOfCards>,
        val learningObjectLabel: String
    )

    /**
     * Holt das entsprechende Lernobjekt und dessen Lernboxen.
     *
     * @param learningObjectId Die UUID des Lernobjekts, das geholt werden muss.
     * @return RepoResult<LearningDetails> OnSuccess: Das Lernobjekt als [LearningDetails]
     */
    @Suppress("UNCHECKED_CAST")
    suspend fun initializePage(learningObjectId: UUID): RepoResult<LearningDetails> =
        coroutineScope {
            val (learningBoxes, learningObject) = awaitAll(
                async { learningBoxRepository.getAllBoxesForLearningObject(learningObjectId) },
                async { learningObjectRepository.findById(learningObjectId) }
            )

            when {
                learningBoxes is Success &&
                        learningObject is Success -> {
                    val learningDetails = LearningDetails(
                        learningBoxes = learningBoxes.result as List<LearningBoxWithNrOfCards>,
                        learningObjectLabel = (learningObject.result as LearningObject).label
                    )
                    Success(learningDetails)
                }
                else -> ServerError(UNEXPECTED_ERROR)
            }
        }
}