package de.fantjastisch.cards_frontend.card.create

import androidx.lifecycle.ViewModel
import de.fantjastisch.cards_frontend.card.CardRepository
import de.fantjastisch.cards_frontend.card.CardSelectItem
import de.fantjastisch.cards_frontend.category.CategoryRepository
import de.fantjastisch.cards_frontend.category.CategorySelectItem
import de.fantjastisch.cards_frontend.infrastructure.RepoResult
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
) : ViewModel() {

    /**
     * Sendet eine Anfrage an das [CategoryRepository] und kriegt alle Kategorien zurück.
     *
     * @return Eine Liste aller Kategorien.
     */
    suspend fun getCategories(): List<CategorySelectItem>? {
        val result = categoryRepository.getPage()
        return when (result) {
            is RepoResult.Success -> result.result.map { cat ->
                CategorySelectItem(
                    id = cat.id,
                    label = cat.label,
                    isChecked = false
                )
            }
            is RepoResult.Error,
            is RepoResult.ServerError -> null // TODO

        }
    }

    /**
     * Sendet eine Anfrage an das [CardRepository] und kriegt alle Karten zurück.
     *
     * @return Eine Liste aller Karten.
     */
    suspend fun getCards(): List<CardSelectItem>? {
        return when (val result = cardRepository.getPage(null,null,null,false)) {
            is RepoResult.Success -> result.result.map { card ->
                CardSelectItem(
                    card = card,
                    isChecked = false
                )
            }
            is RepoResult.Error,
            is RepoResult.ServerError -> null // TODO
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
            question = question,
            answer = answer,
            tag = tag,
            links = links,
            categories = categories
                .filter { it.isChecked }
                .map { it.id }
        )
    )
}
