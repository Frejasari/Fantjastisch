package de.fantjastisch.cards_frontend.learning_mode

import androidx.lifecycle.ViewModel
import de.fantjastisch.cards_frontend.card.CardRepository
import de.fantjastisch.cards_frontend.infrastructure.RepoResult
import de.fantjastisch.cards_frontend.learning_box.LearningBoxRepository
import de.fantjastisch.cards_frontend.learning_box.LearningBoxWitNrOfCards
import de.fantjastisch.cards_frontend.learning_box.card_to_learning_box.CardToLearningBoxRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.openapitools.client.models.CardEntity
import java.util.*

class LearningModeModel(
    private val cardToLearningBoxRepository: CardToLearningBoxRepository = CardToLearningBoxRepository(),
    private val learningBoxRepository: LearningBoxRepository = LearningBoxRepository(),
    private val cardRepository: CardRepository = CardRepository()
) : ViewModel() {

    data class LearningMode(
        val learningBoxesInObject: List<LearningBoxWitNrOfCards>,
        val isFirstBox: Boolean,
        val isLastBox: Boolean,
        val nextCards: Queue<CardEntity>,
        val learningBox: LearningBoxWitNrOfCards
    )

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
            cardResult is RepoResult.Success
                    && learningBoxResult is RepoResult.Success
                    && cardsInLearningBoxResult is RepoResult.Success -> {
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

                RepoResult.Success(learningMode)
            }
            else -> RepoResult.ServerError()
        }
    }

    suspend fun moveCard(fromBoxId: UUID, toBoxId: UUID, currentCardId: UUID): RepoResult<Unit> = cardToLearningBoxRepository.moveCards(
        from = fromBoxId,
        to = toBoxId,
        cardIds = listOf(currentCardId)
    )
}