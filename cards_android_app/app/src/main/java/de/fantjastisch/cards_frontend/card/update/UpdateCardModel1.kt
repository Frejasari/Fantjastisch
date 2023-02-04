package de.fantjastisch.cards_frontend.card.update

import de.fantjastisch.cards_frontend.card.CardRepository
import de.fantjastisch.cards_frontend.card.CardSelectItem
import de.fantjastisch.cards_frontend.category.CategoryRepository
import de.fantjastisch.cards_frontend.category.CategorySelectItem
import de.fantjastisch.cards_frontend.infrastructure.RepoResult
import kotlinx.coroutines.*
import org.openapitools.client.models.*
import java.util.*

/**
 * Kapselt die Logik für das [UpdateCardViewModel].
 * Fungiert als Vermittler zwischen Repository und ViewModel.
 *
 * @property id Id, der Karte die bearbeitet wird.
 * @property cardRepository Karten Repository.
 * @property categoryRepository Kategorie Repository.
 *
 * @author Freja Sender, Tamari Bayer
 */
class UpdateCardModel(
    private val id: UUID,
    private val cardRepository: CardRepository = CardRepository(),
    private val categoryRepository: CategoryRepository = CategoryRepository()
) {

    /**
     * Sendet eine Anfrage für das Updaten einer bestehenden Karte an das Backend-Repository für Karten.
     *
     * @param answer Neuer Inhalt der Antwort der zu erstellenden Karte.
     * @param question Neuer Inhalt der Frage der zu erstellenden Karte.
     * @param tag Neuer Inhalt des Schlagwortes der zu erstellenden Karte.
     * @param categories Neue Zugehörigen Kategorien der zu erstellenden Karte.
     * @param links Neue Links der zu erstellenden Karte.
     */
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
        val categoriesOfCard: List<CategoryOfCardEntity>,
        val links: List<LinkEntity>,
        val cards: List<CardSelectItem>
    )

    /**
     * Stellt die Daten bereit, die das viewModel beim Laden der Ansicht benötigt.
     * Die Daten werden asynchron, also parallel eingeholt. Sobald alle Aufrufe ein RepoResult.Success
     * liefern, also erfolgreich geladen wurden, werden die Daten weiterverarbeitet, gesammelt, und im
     * Model-eigenen Transportobjekt an das viewModel zurückgegeben. Sonst wird ein RepoResult.Error
     * zurückgegeben.
     *
     * @return Ein parametrisiertes RepoResult Objekt, welches darstellt, ob die benötigten Daten
     * erfolgreich geladen werden konnten, oder nicht.
     */
    @Suppress("UNCHECKED_CAST")
    suspend fun initializePage(): RepoResult<UpdateCard> = coroutineScope {
        // Runs coroutines in parallel and waits until all of them are done
        val (cardResult, categoryResult, allCardsResult) = awaitAll(
            async { cardRepository.getCard(id = id) },
            async { categoryRepository.getPage() },
            async { cardRepository.getPage(null, null, null, false) }
        )

        when {
            cardResult is RepoResult.Success
                    && categoryResult is RepoResult.Success
                    && allCardsResult is RepoResult.Success -> {
                val card = cardResult.result as CardEntity
                val categories = categoryResult.result as List<CategoryEntity>
                val cards = allCardsResult.result as List<CardEntity>
                val cardSelectItems = cards
                    .filter { card -> card.id != id }
                    .map { card ->
                        CardSelectItem(
                            card = card,
                            isChecked = false
                        )
                    }
                RepoResult.Success(
                    UpdateCard(
                        id = card.id,
                        question = card.question,
                        answer = card.answer,
                        allCategories = categories,
                        categoriesOfCard = card.categories,
                        tag = card.tag,
                        links = card.links,
                        cards = cardSelectItems
                    )
                )
            }
            else -> RepoResult.Error(emptyList())
        }
    }
}
