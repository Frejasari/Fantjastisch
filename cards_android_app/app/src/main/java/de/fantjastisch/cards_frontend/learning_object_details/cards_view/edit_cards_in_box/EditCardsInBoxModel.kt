package de.fantjastisch.cards_frontend.learning_object_details.cards_view.edit_cards_in_box

import de.fantjastisch.cards_frontend.card.CardRepository
import de.fantjastisch.cards_frontend.card.CardSelectItem
import de.fantjastisch.cards_frontend.infrastructure.RepoResult
import de.fantjastisch.cards_frontend.learning_box.card_to_learning_box.CardToLearningBoxRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.openapitools.client.models.*
import java.util.*

/**
 * Kapselt die Logik für [EditCardsInBoxViewModel]
 * Fungiert als Vermittler zwischen Repository und ViewModel
 *
 * @property cardRepository Karte Repository
 * @property cardToLearningBoxRepository Karte zu Lernbox Repository
 *
 * @author Semjon Nirmann, Freja Sender
 */
class EditCardsInBoxModel(
    private val cardRepository: CardRepository = CardRepository(),
    private val cardToLearningBoxRepository: CardToLearningBoxRepository = CardToLearningBoxRepository()
) {

    /**
     * Holt alle Karten, alle Karten einer Lernbox und alle Karten eines Lernobjekts,
     * indem das Repository angefragt wird.
     *
     * @param learningBoxId Die UUID der Lernbox, deren Karten geholt werden.
     * @param learningObjectId Die UUID des Lernobjekts, dessen Karten geholt werden.
     * @return RepoResult<List<CardSelectItem>> OnSuccess: Die Liste der Karten, die entweder
     *   in der Box sind oder noch hinzugefügt werden können.
     */
    @Suppress("UNCHECKED_CAST")
    suspend fun initializePage(
        learningBoxId: UUID,
        learningObjectId: UUID
    ): RepoResult<List<CardSelectItem>> = coroutineScope {
        val (cardsResult, cardsInLearningBoxResult, cardsInLearningObjectResult) = awaitAll(
            async {
                cardRepository.getPage(
                    categoryIds = null,
                    search = null,
                    tag = null,
                    sort = false
                )
            },
            async {
                cardToLearningBoxRepository.getCardIdsForBox(
                    learningBoxId = learningBoxId
                )
            },
            async {
                cardToLearningBoxRepository.getAllCardsForLearningObject(
                    learningObjectId = learningObjectId
                )
            }
        )
        when {
            cardsResult is RepoResult.Success
                    && cardsInLearningBoxResult is RepoResult.Success
                    && cardsInLearningObjectResult is RepoResult.Success -> {
                val cards = (cardsResult.result) as List<CardEntity>
                val cardsInLearningBox = (cardsInLearningBoxResult.result) as List<UUID>
                val cardsInLearningObject = (cardsInLearningObjectResult.result) as List<UUID>
                val containedCards = filterContainedAndSelectableCards(
                    listOfCardIdsInBox = cardsInLearningBox,
                    listOfCardIdsInObject = cardsInLearningObject,
                    allCards = cards
                )
                RepoResult.Success(containedCards)
            }
            else -> RepoResult.Error(emptyList())
        }
    }

    /**
     * Ermittelt alle Karten, die entweder in der Box schon vorhanden sind, oder noch hinzugefügt werden können.
     * Karten können zu einer Lernbox hinzugefügt werden, wenn sie in keiner anderen Box des Lernobjekts sind, zu
     * der die Lernbox gehört.
     *
     * @param listOfCardIdsInBox Die Liste der UUIDs von Karten, die sich in einer Lernbox gehören.
     * @param listOfCardIdsInObject Die Liste der UUIDs von Karten, die zu einem Lernobjekt gehören.
     * @param allCards Die Liste aller Karten.
     * @return List<CardSelectItem> Die Menge aller Karten, die zu einer Lernbox hinzugefügt werden können.
     */
    private fun filterContainedAndSelectableCards(
        listOfCardIdsInBox: List<UUID>,
        listOfCardIdsInObject: List<UUID>,
        allCards: List<CardEntity>
    ): List<CardSelectItem> {
        // Welche Karten aus allen Karten eines Lernobjekts gibt es in meiner Box nicht?
        val cardsPresentInOtherBoxes =
            listOfCardIdsInObject.filter { id ->
                !listOfCardIdsInBox.contains(id)
            }
        // karten, die entweder in der Box sind oder nicht in anderen Boxen sind
        val selectableCardsInBox = allCards.filter { card ->
            listOfCardIdsInBox.contains(card.id) || !cardsPresentInOtherBoxes.contains(card.id)
        }.map { card ->
            CardSelectItem(
                card = card,
                // Karten, die in der Box sing sind checked, die die noch hinzugefügt werden können nicht
                isChecked = listOfCardIdsInBox.contains(card.id)
            )
        }.sortedByDescending { it.isChecked }
        return selectableCardsInBox
    }

    /**
     * Aktualisiert die Menge an Karten einer Lernbox.
     *
     * @param selectedCardsIds Die Liste der UUIDs von aktualisierten Karten.
     * @param learningBoxId Die UUID der Lernbox, deren Karten aktualisiert werden sollen.
     * @return RepoResult<Unit> Ein parametrisiertes Objekt, das darstellt, ob alle Persistenzoperationen erfolgreich durchgeführt
     * werden konnten, oder nicht.
     */
    suspend fun updateCardsInBox(
        selectedCardsIds: List<UUID>,
        learningBoxId: UUID
    ): RepoResult<Unit> {
        return cardToLearningBoxRepository.updateBoxCards(
            selected = selectedCardsIds,
            learningBoxId = learningBoxId,
        )
    }
}