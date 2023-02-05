package de.fantjastisch.cards_frontend.learning_mode

import de.fantjastisch.cards_frontend.card.CardRepository
import de.fantjastisch.cards_frontend.learning_box.LearningBoxRepository
import de.fantjastisch.cards_frontend.learning_box.LearningBoxWitNrOfCards
import de.fantjastisch.cards_frontend.learning_box.card_to_learning_box.CardToLearningBoxRepository
import de.fantjastisch.cards_frontend.util.RepoResult
import de.fantjastisch.cards_frontend.util.RepoResult.*
import de.fantjastisch.cards_frontend.util.RepoResult.UnexpectedErrorType.*
import de.fantjastisch.cards_frontend.util.isNetworkError
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.openapitools.client.models.CardEntity
import java.util.*

/**
 * Kapselt die Logik für das [LearningModeViewModel].
 * Fungiert als Vermittler zwischen Repository und ViewModel.
 *
 * @property cardToLearningBoxRepository Repository Karten in Lernboxen.
 * @property learningBoxRepository Repository Lernboxen.
 * @property cardRepository Repository Karten.
 *
 * @author Semjon Nirmann, Jessica Repty
 */
class LearningModeModel(
    private val cardToLearningBoxRepository: CardToLearningBoxRepository = CardToLearningBoxRepository(),
    private val learningBoxRepository: LearningBoxRepository = LearningBoxRepository(),
    private val cardRepository: CardRepository = CardRepository()
) {

    data class LearningMode(
        val learningBoxesInObject: List<LearningBoxWitNrOfCards>,
        val isFirstBox: Boolean,
        val isLastBox: Boolean,
        val nextCards: Queue<CardEntity>,
        val learningBox: LearningBoxWitNrOfCards
    )

    /**
     * Holt durch Anfragen an das [learningBoxRepository], [cardRepository] und [cardToLearningBoxRepository]
     * die notwendigen Karten in gewünschter Reihenfolge der Lernbox.
     *
     * @param learningObjectId Id, des Lernobjektes, welches gelernt wird.
     * @param learningBoxId Id, der Lernbox, welche gelernt werden soll.
     * @param sort True, wenn Karten in alphabetisch sortierter Reihenfolge.
     * @return [LearningMode]-Entität
     */
    @Suppress("UNCHECKED_CAST")
    suspend fun initializePage(
        learningObjectId: UUID,
        learningBoxId: UUID,
        sort: Boolean
    ): RepoResult<LearningMode> = coroutineScope {
        val (cardResult, learningBoxResult, cardsInLearningBoxResult) = awaitAll(
            async {
                cardRepository.getPage(
                    categoryIds = null,
                    search = null,
                    tag = null,
                    sort = sort
                )
            },
            async {
                learningBoxRepository.getAllBoxesForLearningObject(
                    learningObjectId = learningObjectId
                )
            },
            async {
                cardToLearningBoxRepository
                    .getCardIdsForBox(learningBoxId = learningBoxId)
            }
        )

        when {
            cardResult is Success
                    && learningBoxResult is Success
                    && cardsInLearningBoxResult is Success -> {
                val allCards =
                    (if (!sort) cardResult.result.shuffled() else cardResult.result) as List<CardEntity>
                val learningBoxesInObject =
                    learningBoxResult.result as List<LearningBoxWitNrOfCards>
                val cardsInLearningBox = cardsInLearningBoxResult.result as List<UUID>

                val box = learningBoxesInObject.first { it.id == learningBoxId }


                val learningMode = LearningMode(
                    learningBoxesInObject = learningBoxesInObject,
                    isFirstBox = box.boxNumber == 0,
                    isLastBox = box.boxNumber == learningBoxesInObject.size - 1,
                    nextCards = LinkedList(allCards.filter { card ->
                        cardsInLearningBox.contains(card.id)
                    }),
                    learningBox = box
                )

                Success(learningMode)
            }
            cardResult.isNetworkError() || learningBoxResult.isNetworkError() || cardsInLearningBoxResult.isNetworkError() -> ServerError(
                NETWORK_ERROR
            )
            else -> ServerError(UNEXPECTED_ERROR)
        }
    }


    /**
     * Sendet eine Anfrage an [cardToLearningBoxRepository] um die übergebenen Karten-Id's
     * von eine Lernbox in eine andere Lernbox zu schieben.
     *
     * @param fromBoxId Id, der Lernbox aus welcher die Karten kommen.
     * @param toBoxId Id, der Lernbox in welche die Karten verschoben werden.
     * @param currentCardId Liste an Karten-Id's, welche verschoben werden sollen.
     * @return RepoResponse<Unit> (OnSuccess, OnUnexpectedError, ...)
     */
    suspend fun moveCard(fromBoxId: UUID, toBoxId: UUID, currentCardId: UUID): RepoResult<Unit> =
        cardToLearningBoxRepository.moveCards(
            from = fromBoxId,
            to = toBoxId,
            cardIds = listOf(currentCardId)
        )

}