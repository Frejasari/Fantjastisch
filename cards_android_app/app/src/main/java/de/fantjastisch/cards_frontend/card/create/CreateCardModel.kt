package de.fantjastisch.cards_frontend.card.create

import de.fantjastisch.cards_frontend.card.CardRepository
import de.fantjastisch.cards_frontend.card.CardSelectItem
import de.fantjastisch.cards_frontend.category.CategoryRepository
import de.fantjastisch.cards_frontend.category.CategorySelectItem
import de.fantjastisch.cards_frontend.util.RepoResult
import de.fantjastisch.cards_frontend.util.RepoResult.ServerError
import de.fantjastisch.cards_frontend.util.RepoResult.UnexpectedErrorType.*
import de.fantjastisch.cards_frontend.util.RepoResult.Success
import de.fantjastisch.cards_frontend.util.isNetworkError
import de.fantjastisch.cards_frontend.util.toUnselectedCategorySelectItems
import kotlinx.coroutines.*
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

    /**
     * Hält Daten über alle Karten, die das Ziel einer Verlinkung sein können,
     * und über alle Kategorien, die eine Karte untergeordnet werden kann.
     *
     * @property cards
     * @property allCategories
     */
    data class CreateCard(
        val cards: List<CardSelectItem>,
        val allCategories: List<CategorySelectItem>,
    )

    /**
     * Holt alle Karten und Kategorien.
     * Alle Karten werden für die Verlinkung auf isChecked = false gesetzt.
     * Alle Kategorien werden auf isChecked = false gesetzt.
     *
     * @return RepoResult<CreateCard> OnSuccess: ein Entität der [CreateCard]
     */
    @Suppress("UNCHECKED_CAST")
    suspend fun initializePage(): RepoResult<CreateCard> = coroutineScope {

        val (categoryResult, allCardsResult) = awaitAll(
            async { categoryRepository.getPage() },
            async { cardRepository.getPage(null, null, null, false) }
        )

        when {
            allCardsResult is Success
                    && categoryResult is Success -> {
                val cards = allCardsResult.result as List<CardEntity>
                val categories = categoryResult.result as List<CategoryEntity>
                val cardSelectItems = cards
                    .map { card ->
                        CardSelectItem(
                            card = card,
                            isChecked = false
                        )
                    }
                val categorySelectItems = categories.toUnselectedCategorySelectItems()

                Success(
                    CreateCard(
                        allCategories = categorySelectItems,
                        cards = cardSelectItems
                    )
                )
            }
            allCardsResult.isNetworkError() || categoryResult.isNetworkError()
            -> ServerError(NETWORK_ERROR)
            else -> ServerError(UNEXPECTED_ERROR)
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
    ): RepoResult<String> = cardRepository.createCard(
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
