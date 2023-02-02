package de.fantjastisch.cards_frontend.learning_overview

import de.fantjastisch.cards_frontend.infrastructure.RepoResult
import de.fantjastisch.cards_frontend.learning_object.LearningObject
import de.fantjastisch.cards_frontend.learning_object.LearningObjectRepository

class LearningOverviewModel(
    private val learningObjectRepository: LearningObjectRepository = LearningObjectRepository()
) {
    suspend fun initializePage(): RepoResult<List<LearningObject>> =
        learningObjectRepository.getAll()
}