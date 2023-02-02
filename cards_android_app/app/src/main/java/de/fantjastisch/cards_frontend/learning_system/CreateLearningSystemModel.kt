package de.fantjastisch.cards_frontend.learning_system

import org.openapitools.client.models.CreateLearningSystemEntity

class CreateLearningSystemModel(
    private val learningSystemRepository: LearningSystemRepository = LearningSystemRepository()
) {
    suspend fun addLearningSystem(
        learningSystemLabel: String,
        learningSystemBoxLabels: List<String>
    ) = learningSystemRepository.createLearningsystem(
        learningSystem = CreateLearningSystemEntity(
            label = learningSystemLabel,
            boxLabels = learningSystemBoxLabels
        )
    )

    fun getNumOfBoxes(numString: String): Int {
        val pattern = Regex("^\\d+\$")

        var numBoxes = 0
        if (numString.isNotEmpty() && numString.matches(pattern)) {
            numBoxes = numString.toInt().coerceAtMost(10)
        }
        return numBoxes
    }
}