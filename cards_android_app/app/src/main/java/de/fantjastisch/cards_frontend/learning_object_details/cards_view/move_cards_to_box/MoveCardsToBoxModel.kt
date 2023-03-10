package de.fantjastisch.cards_frontend.learning_object_details.cards_view.move_cards_to_box

import de.fantjastisch.cards_frontend.card.CardRepository
import de.fantjastisch.cards_frontend.card.CardSelectItem
import de.fantjastisch.cards_frontend.learning_box.LearningBoxRepository
import de.fantjastisch.cards_frontend.learning_box.LearningBoxWithNrOfCards
import de.fantjastisch.cards_frontend.learning_box.card_to_learning_box.CardToLearningBoxRepository
import de.fantjastisch.cards_frontend.util.RepoResult
import de.fantjastisch.cards_frontend.util.RepoResult.*
import de.fantjastisch.cards_frontend.util.RepoResult.UnexpectedErrorType.*
import de.fantjastisch.cards_frontend.util.isNetworkError
import kotlinx.coroutines.*
import org.openapitools.client.models.CardEntity
import java.util.*

/**
 * Kapselt die Logik für [MoveCardsToBoxViewModel]
 * Fungiert als Vermittler zwischen Repository und ViewModel
 *
 * @property cardRepository Karte Repository
 * @property cardToLearningBoxRepository Karte zu Lernbox Repository
 * @property learningBoxRepository Lernbox Repository
 */
class MoveCardsToBoxModel(
    private val cardRepository: CardRepository = CardRepository(),
    private val cardToLearningBoxRepository: CardToLearningBoxRepository = CardToLearningBoxRepository(),
    private val learningBoxRepository: LearningBoxRepository = LearningBoxRepository(),
) {

    /**
     * Hält Daten über Karten, die in andere Lernbox verschoben werden können.
     * Hält Daten über Lernbox, von der die karten verschoben werden.
     *
     * @property learningBoxes alle Lernboxen eines Lernobjekts.
     * @property learningBoxNum Der Index der Lernbox.
     * @property isLastBox Gibt an, ob ein Lernbox die letzte im Lernobjekt ist.
     * @property isFirstBox Gibt an, ob ein Lernbox die erste im Lernobjekt ist.
     * @property cards alle Karten zu einer Lernbox.
     */
    data class MoveCardsToBox(
        val learningBoxes: List<LearningBoxWithNrOfCards>,
        val learningBoxNum: Int,
        var isLastBox: Boolean,
        var isFirstBox: Boolean,
        val cards: List<CardSelectItem>
    )

    /**
     * Stellt ein Instanz der Klasse [MoveCardsToBox] zusammen, indem die dazugehörige
     * Daten gesammelt werden.
     *
     *
     * @param learningObjectId Die UUID des Lernobjekts, in dem die Karten verschoben werden.
     * @param learningBoxId Die UUID des Lernbox, in die die karten verschoben werden.
     * @return RepoResult<MoveCardsToBox> OnSuccess:
     */
    @Suppress("UNCHECKED_CAST")
    suspend fun initializePage(
        learningObjectId: UUID,
        learningBoxId: UUID
    ): RepoResult<MoveCardsToBox> = coroutineScope {
        val (learningBoxes, cards) = awaitAll(
            async { getAllLearningBoxes(learningObjectId = learningObjectId) },

            async {
                cardRepository.getPage(
                    categoryIds = null,
                    search = null,
                    tag = null,
                    sort = null
                )
            })

        when {
            learningBoxes is Success &&
                    cards is Success -> {
                // alle Lernboxen
                val learningBoxesInObject = learningBoxes.result as List<LearningBoxWithNrOfCards>
                // der Index der AusgangsLernbox
                val learningBoxNum = getLearningBoxNum(
                    learningBoxes = learningBoxesInObject,
                    learningBoxId = learningBoxId
                )
                // ist sie die erste Lernbox?
                val isFirstBox = learningBoxNum == 0
                // ist sie die letzte Lernbox?
                val isLastBox = learningBoxNum == learningBoxes.result.size - 1

                when (val contained = getContainedCards(
                    cards.result as List<CardEntity>,
                    learningBoxId = learningBoxId
                )) {
                    is Success -> {
                        val moveCardsToBox = MoveCardsToBox(
                            learningBoxes = learningBoxesInObject,
                            learningBoxNum = learningBoxNum,
                            isLastBox = isLastBox,
                            isFirstBox = isFirstBox,
                            cards = contained.result
                        )
                        Success(moveCardsToBox)
                    }
                    is Error -> Error(contained.errors)
                    is ServerError -> ServerError(contained.cause)
                }
            }
            learningBoxes.isNetworkError() || cards.isNetworkError() -> ServerError(NETWORK_ERROR)
            else -> ServerError(UNEXPECTED_ERROR)
        }
    }

    /**
     * Holt alle Lernboxen eines Lernobjekts, indem die Anfrage an das Repository weitergeitet wird.
     *
     * @param learningObjectId Die UUID des Lernobjekts, dessen Lernboxes geholt werden.
     * @return
     */
    private suspend fun getAllLearningBoxes(learningObjectId: UUID): RepoResult<List<LearningBoxWithNrOfCards>> =
        learningBoxRepository.getAllBoxesForLearningObject(learningObjectId = learningObjectId)


    /**
     * Bestimmt der Index einer Lernbox in einem Lernobjekt.
     *
     * @param learningBoxes Die Liste aller Lernboxen eines Lernobjekts.
     * @param learningBoxId Die UUID der Lernbox, deren Index gesucht wird.
     * @return Der Index der Lernbox.
     */
    private fun getLearningBoxNum(
        learningBoxes: List<LearningBoxWithNrOfCards>,
        learningBoxId: UUID
    ): Int {
        return learningBoxes.first { box -> box.id == learningBoxId }.boxNumber
    }


    /**
     * Holt alle Karten, die in einer Lernbox sind, bzw. in eine andere Lernbox verschoben werden können.
     *
     * @param allCards Die Liste aller Karten.
     * @param learningBoxId Die UUID der Lernbox, deren Karten geholt wird.
     * @return RepoResult<List<CardSelectItem>> OnSuccess: die Liste der Karten als [CardSelectItem], die in einer Lernbox sind.
     */
    private suspend fun getContainedCards(
        allCards: List<CardEntity>,
        learningBoxId: UUID
    ): RepoResult<List<CardSelectItem>> {
        return when (val result =
            cardToLearningBoxRepository.getCardIdsForBox(learningBoxId = learningBoxId)) {
            is Success -> {
                val selectItems = allCards
                    .filter { card -> result.result.contains(card.id) }
                    .map { card ->
                        CardSelectItem(
                            card = card,
                            isChecked = false
                        )
                    }
                Success(selectItems)
            }
            is Error -> Error(result.errors)
            is ServerError -> ServerError(result.cause)
        }
    }

    /**
     * Verschiebt Karten einer Lernbox in die letzte Lernbox, indem die Anfrage an das Repository
     * weitergeleitet wird.
     *
     * @param learningBoxId Die UUID der Lernbox, deren Karten verschoben werden.
     * @param previousBoxId Die UUID der Lernbox, in die die Karten verschoben werden.
     * @param cards Die Karten, die verschoben werden.
     * @return RepoResult<Unit> Ein parametrisiertes Objekt, das darstellt, ob alle Persistenzoperationen erfolgreich durchgeführt
     * werden konnten, oder nicht.
     */
    suspend fun moveToPreviousBox(
        learningBoxId: UUID,
        previousBoxId: UUID,
        cards: List<CardSelectItem>
    ): RepoResult<Unit> =
        cardToLearningBoxRepository.moveCards(
            from = learningBoxId,
            to = previousBoxId,
            cardIds = cards.filter { card -> card.isChecked }
                .map { card -> card.card.id })

    /**
     * Verschiebt Karten einer Lernbox in die nächste Lernbox, indem die Anfrage an das Repository
     * weitergeleitet wird.
     *
     * @param learningBoxId Die UUID der Lernbox, deren Karten verschoben werden.
     * @param nextBoxId Die UUID der Lernbox, in die die Karten verschoben werden.
     * @param cards Die Karten, die verschoben werden.
     * @return RepoResult<Unit> Ein parametrisiertes Objekt, das darstellt, ob alle Persistenzoperationen erfolgreich durchgeführt
     * werden konnten, oder nicht.
     */
    suspend fun moveToNextBox(
        learningBoxId: UUID,
        nextBoxId: UUID,
        cards: List<CardSelectItem>
    ): RepoResult<Unit> =
        cardToLearningBoxRepository.moveCards(
            from = learningBoxId,
            to = nextBoxId,
            cardIds = cards.filter { card -> card.isChecked }.map { card -> card.card.id })
}