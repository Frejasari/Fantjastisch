package de.fantjastisch.cards_frontend.learning_overview.delete

import androidx.lifecycle.ViewModel
import de.fantjastisch.cards_frontend.infrastructure.RepoResult
import de.fantjastisch.cards_frontend.learning_object.LearningObjectRepository
import java.util.*

class DeleteLearningObjectModel(
    private val learningObjectRepository: LearningObjectRepository = LearningObjectRepository(),
) : ViewModel() {

    suspend fun deleteLearningObject(learningObjectId: UUID): RepoResult<Unit> =
        learningObjectRepository.delete(id = learningObjectId)
}