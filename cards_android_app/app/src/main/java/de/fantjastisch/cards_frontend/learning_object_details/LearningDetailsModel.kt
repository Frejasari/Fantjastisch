package de.fantjastisch.cards_frontend.learning_object_details

import androidx.lifecycle.ViewModel
import de.fantjastisch.cards_frontend.infrastructure.RepoResult
import de.fantjastisch.cards_frontend.learning_box.LearningBoxRepository
import de.fantjastisch.cards_frontend.learning_box.LearningBoxWitNrOfCards
import de.fantjastisch.cards_frontend.learning_object.LearningObject
import de.fantjastisch.cards_frontend.learning_object.LearningObjectRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import java.util.*

class LearningDetailsModel(
    private val learningBoxRepository: LearningBoxRepository = LearningBoxRepository(),
    private val learningObjectRepository: LearningObjectRepository = LearningObjectRepository()
) : ViewModel() {

    data class LearningDetails(
        val learningBoxes: List<LearningBoxWitNrOfCards>,
        val learningObjectLabel: String
    )

    @Suppress("UNCHECKED_CAST")
    suspend fun initializePage(learningObjectId: UUID): RepoResult<LearningDetails> =
        coroutineScope {
            val (learningBoxes, learningObject) = awaitAll(
                async { learningBoxRepository.getAllBoxesForLearningObject(learningObjectId) },
                async { learningObjectRepository.findById(learningObjectId) }
            )

            when {
                learningBoxes is RepoResult.Success &&
                        learningObject is RepoResult.Success -> {
                    val learningDetails = LearningDetails(
                        learningBoxes = learningBoxes.result as List<LearningBoxWitNrOfCards>,
                        learningObjectLabel = (learningObject.result as LearningObject).label
                    )
                    RepoResult.Success(learningDetails)
                }
                else -> RepoResult.ServerError()
            }
        }
}