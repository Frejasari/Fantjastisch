package de.fantjastisch.cards_frontend.learning_system

import org.openapitools.client.models.CreateLearningSystemEntity

/**
 * Kapselt die Logik für das [CreateLearningSystemViewModel].
 * Fungiert als Vermittler zwischen Repository und ViewModel.
 *
 * @property learningSystemRepository Lernsystem Repository
 *
 * @author  Semjon Nirmann, Freja Sender, Tamari Bayer
 */
class CreateLearningSystemModel(
    private val learningSystemRepository: LearningSystemRepository = LearningSystemRepository()
) {

    /**
     * Sendet eine Anfrage an das [LearningSystemRepository] für das Erstellen eines Lernsystems.
     *
     * @param learningSystemLabel Die Bezeichnung des Lernsystems.
     * @param learningSystemBoxLabels Die Bezeichnungen der Lernboxen innerhalb des Lernsystems
     * @return
     */
    suspend fun addLearningSystem(
        learningSystemLabel: String,
        learningSystemBoxLabels: List<String>
    ) = learningSystemRepository.createLearningsystem(
        learningSystem = CreateLearningSystemEntity(
            label = learningSystemLabel,
            boxLabels = learningSystemBoxLabels
        )
    )

    /**
     * Überprüft ob die eingegebene Anzahl von Lernboxen zu eienm Lernsystem zugelassen ist.
     * Die Anzahl > 10 -> nicht zugelassen. Die Anzahl wird auf 10 gesetzt.
     * Die Anzahl <= 10 -> zugelassen
     *
     * @param numString die übergebene Zahl als String
     * @return wenn [numString] <= 10 dann [numString] als ein Int
     *         sonst 10
     */
    fun getNumOfBoxes(numString: String): Int {
        val pattern = Regex("^\\d+\$")

        var numBoxes = 0
        if (numString.isNotEmpty() && numString.matches(pattern)) {
            numBoxes = numString.toInt().coerceAtMost(10)
        }
        return numBoxes
    }
}