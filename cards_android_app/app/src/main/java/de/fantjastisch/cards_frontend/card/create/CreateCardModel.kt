package de.fantjastisch.cards_frontend.card.create

import de.fantjastisch.cards_frontend.card.CardRepository
import de.fantjastisch.cards_frontend.card.CardSelectItem
import de.fantjastisch.cards_frontend.category.CategoryRepository
import de.fantjastisch.cards_frontend.category.CategorySelectItem
import de.fantjastisch.cards_frontend.infrastructure.RepoResult
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.openapitools.client.models.CardEntity
import org.openapitools.client.models.CategoryEntity
import org.openapitools.client.models.CreateCardEntity
import org.openapitools.client.models.LinkEntity

/**
 * Kapselt die Logik für das [CreateCardViewModel].
 * Fungiert als Vermittler zwischen Repository und ViewModel.
 *
 * @property cardRepository Karten Repository
 * @property categoryRepository Kategorie Repository
 *
 * @author Freja Sender, Tamari Bayer
 */
class CreateCardModel(
    private val cardRepository: CardRepository = CardRepository(),
    private val categoryRepository: CategoryRepository = CategoryRepository()
) {

    data class CreateCard(
        val cards: List<CardSelectItem>,
        val allCategories: List<CategorySelectItem>,
    )

    suspend fun initializePage() = coroutineScope {

        val (categoryResult, allCardsResult) = awaitAll(
            async { categoryRepository.getPage() },
            async { cardRepository.getPage(null, null, null, false) }
        )

        when {
            allCardsResult is RepoResult.Success
                    && categoryResult is RepoResult.Success -> {
                val cards = allCardsResult.result as List<CardEntity>
                val categories = categoryResult.result as List<CategoryEntity>
                val cardSelectItems = cards
                    .map { card ->
                        CardSelectItem(
                            card = card,
                            isChecked = false
                        )
                    }
                val categorySelectItems = categories
                    .map { categoryEntity ->
                        CategorySelectItem(
                            label = categoryEntity.label,
                            id = categoryEntity.id,
                            isChecked = false
                        )
                    }
                RepoResult.Success(
                    CreateCard(
                        allCategories = categorySelectItems,
                        cards = cardSelectItems
                    )
                )
            }
            else -> RepoResult.ServerError()
        }
    }

    /**
     * Sendet eine Anfrage an das [CardRepository] für das Erstellen einer Karte.
     *
     * @param answer Antwort der zu erstellenden Karte.
     * @param question Frage der zu erstellenden Karte.
     * @param tag Schlagwortes der zu erstellenden Karte.
     * @param categories Zugehörige Kategorien der zu erstellenden Karte.
     * @param links Links der zu erstellenden Karte.
     */
    suspend fun createCard(
        answer: String,
        question: String,
        tag: String,
        categories: List<CategorySelectItem>,
        links: List<LinkEntity>
    ) = cardRepository.createCard(
        card = CreateCardEntity(
            question = question.trim(),
            answer = answer.trim(),
            tag = tag.trim(),
            links = links,
            categories = categories
                .filter { it.isChecked }
                .map { it.id }
        )
    )
}
