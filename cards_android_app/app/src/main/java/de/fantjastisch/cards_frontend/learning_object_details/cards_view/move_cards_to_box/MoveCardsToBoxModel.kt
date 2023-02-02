package de.fantjastisch.cards_frontend.learning_object_details.cards_view.move_cards_to_box

import androidx.lifecycle.ViewModel
import de.fantjastisch.cards_frontend.card.CardRepository
import de.fantjastisch.cards_frontend.card.CardSelectItem
import de.fantjastisch.cards_frontend.infrastructure.RepoResult
import de.fantjastisch.cards_frontend.learning_box.LearningBoxRepository
import de.fantjastisch.cards_frontend.learning_box.LearningBoxWitNrOfCards
import de.fantjastisch.cards_frontend.learning_box.card_to_learning_box.CardToLearningBoxRepository
import kotlinx.coroutines.*
import org.openapitools.client.models.CardEntity
import java.util.*

class MoveCardsToBoxModel(
    private val cardRepository: CardRepository = CardRepository(),
    private val cardToLearningBoxRepository: CardToLearningBoxRepository = CardToLearningBoxRepository(),
    private val learningBoxRepository: LearningBoxRepository = LearningBoxRepository(),
) : ViewModel() {

    data class MoveCardsToBox(
        val learningBoxes: List<LearningBoxWitNrOfCards>,
        val learningBoxNum: Int,
        var isLastBox: Boolean,
        var isFirstBox: Boolean,
        val cards: List<CardSelectItem>
    )

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
            learningBoxes is RepoResult.Success &&
                    cards is RepoResult.Success -> {
                val learningBoxesInObject = learningBoxes.result as List<LearningBoxWitNrOfCards>
                val learningBoxNum = getLearningBoxNum(
                    learningBoxes = learningBoxesInObject,
                    learningBoxId = learningBoxId
                )
                val isFirstBox = learningBoxNum == 0
                val isLastBox = learningBoxNum == learningBoxes.result.size - 1

                when (val contained = getContainedCards(
                    cards.result as List<CardEntity>,
                    learningBoxId = learningBoxId
                )) {
                    is RepoResult.Success -> {
                        val moveCardsToBox = MoveCardsToBox(
                            learningBoxes = learningBoxesInObject,
                            learningBoxNum = learningBoxNum,
                            isLastBox = isLastBox,
                            isFirstBox = isFirstBox,
                            cards = contained.result
                        )
                        RepoResult.Success(moveCardsToBox)
                    }
                    is RepoResult.Error,
                    is RepoResult.ServerError -> RepoResult.ServerError()
                }
            }
            else -> RepoResult.ServerError()
        }
    }

    private suspend fun getAllLearningBoxes(learningObjectId: UUID): RepoResult<List<LearningBoxWitNrOfCards>> =
        learningBoxRepository.getAllBoxesForLearningObject(learningObjectId = learningObjectId)

    private fun getLearningBoxNum(learningBoxes: List<LearningBoxWitNrOfCards>, learningBoxId: UUID): Int {
        return learningBoxes.first { box -> box.id == learningBoxId }.boxNumber
    }

    private suspend fun getContainedCards(
        allCards: List<CardEntity>,
        learningBoxId: UUID
    ): RepoResult<List<CardSelectItem>> {
        return when (val result = cardToLearningBoxRepository.getCardIdsForBox(learningBoxId = learningBoxId)) {
            is RepoResult.Success -> {
                val selectItems = allCards
                    .filter { card -> result.result.contains(card.id) }
                    .map { card ->
                        CardSelectItem(
                            card = card,
                            isChecked = false
                        )
                    }
                RepoResult.Success(selectItems)
            }
            is RepoResult.Error,
            is RepoResult.ServerError -> RepoResult.ServerError()
        }
    }

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