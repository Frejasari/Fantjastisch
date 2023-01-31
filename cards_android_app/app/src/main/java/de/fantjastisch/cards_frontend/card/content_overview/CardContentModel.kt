package de.fantjastisch.cards_frontend.card.content_overview

import de.fantjastisch.cards_frontend.card.CardRepository
import de.fantjastisch.cards_frontend.category.CategoryRepository
import de.fantjastisch.cards_frontend.infrastructure.RepoResult
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.openapitools.client.models.CardEntity
import org.openapitools.client.models.CategoryEntity
import org.openapitools.client.models.CategoryOfCardEntity
import org.openapitools.client.models.LinkEntity
import java.util.*

class CardContentModel(
    private val cardRepository: CardRepository = CardRepository(),
    private val categoryRepository: CategoryRepository = CategoryRepository(),
    private val id: UUID
) {
    data class Card(
        val id: UUID,
        val answer: String,
        val question: String,
        val tag: String,
        val allCategories: List<CategoryEntity>,
        val categoriesOfCard: List<CategoryOfCardEntity>,
        val links: List<LinkEntity>
    )


    @Suppress("UNCHECKED_CAST")
    suspend fun initializePage(): RepoResult<Card> = coroutineScope {
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
                    Card(
                        id = card.id,
                        question = card.question,
                        answer = card.answer,
                        allCategories = categories,
                        categoriesOfCard = card.categories,
                        tag = card.tag,
                        links = card.links
                    )
                )
            }
            else -> RepoResult.Error(emptyList())
        }

    }
}