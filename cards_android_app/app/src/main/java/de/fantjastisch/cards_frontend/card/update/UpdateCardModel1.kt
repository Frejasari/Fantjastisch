package de.fantjastisch.cards_frontend.card.update

import de.fantjastisch.cards_frontend.card.CardRepository
import de.fantjastisch.cards_frontend.category.CategoryRepository
import de.fantjastisch.cards_frontend.category.CategorySelectItem
import de.fantjastisch.cards_frontend.infrastructure.RepoResult
import kotlinx.coroutines.*
import org.openapitools.client.models.*
import java.util.*

class UpdateCardModel(
    private val id: UUID,
    private val cardRepository: CardRepository = CardRepository(),
    private val categoryRepository: CategoryRepository = CategoryRepository()
) {

    suspend fun update(
        question: String,
        answer: String,
        categories: List<CategorySelectItem>,
        tag: String,
        links: List<LinkEntity>
    ): RepoResult<Unit> = cardRepository.updateCard(
        UpdateCardEntity(
            id = id,
            question = question,
            answer = answer,
            tag = tag,
            links = links,
            categories = categories.filter { cat -> cat.isChecked }.map { cat -> cat.id }
        )
    )

    data class UpdateCard(
        val id: UUID,
        val answer: String,
        val question: String,
        val tag: String,
        val allCategories: List<CategoryEntity>,
        val categoriesOfCard: List<CategoryOfCardEntity>
    )

    @Suppress("UNCHECKED_CAST")
    suspend fun initializePage(): RepoResult<UpdateCard> = coroutineScope {
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
                    UpdateCard(
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
