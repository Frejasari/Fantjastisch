package de.fantjastisch.cards_frontend.card.content_overview

import de.fantjastisch.cards_frontend.card.CardRepository
import de.fantjastisch.cards_frontend.card.update.UpdateCardModel
import de.fantjastisch.cards_frontend.category.CategoryRepository
import de.fantjastisch.cards_frontend.infrastructure.RepoResult
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.openapitools.client.models.CardEntity
import org.openapitools.client.models.CategoryEntity
import org.openapitools.client.models.CategoryOfCardEntity
import java.util.*

class CardContentModel(
    private val cardRepository: CardRepository = CardRepository(),
    private val categoryRepository: CategoryRepository = CategoryRepository(),
    private val id: UUID
) {

    suspend fun getCard(
        id: UUID,
    ) = cardRepository.getCard(id = id)


    data class Card(
        val id: UUID,
        val answer: String,
        val question: String,
        val tag: String,
        val allCategories: List<CategoryEntity>,
        val categoriesOfCard: List<CategoryOfCardEntity>
    )


    @Suppress("UNCHECKED_CAST")
    suspend fun initializePage(): RepoResult<CardContentModel.Card> = coroutineScope {
        // Runs coroutines in parallel and waits until all of them are done
        val (cardResult, categoryResult) = awaitAll(
            async { cardRepository.getCard(id = id) },
            async { categoryRepository.getPage() }
        )

        when {
            cardResult is RepoResult.Success
                    && categoryResult is RepoResult.Success -> {
                val card = cardResult.result as CardEntity
                val categories = categoryResult.result as List<CategoryEntity>
                RepoResult.Success(
                    CardContentModel.Card(
                        id = card.id,
                        question = card.question,
                        answer = card.answer,
                        allCategories = categories,
                        categoriesOfCard = card.categories,
                        tag = card.tag
                    )
                )
            }
            else -> RepoResult.Error(emptyList())
        }

    }
}