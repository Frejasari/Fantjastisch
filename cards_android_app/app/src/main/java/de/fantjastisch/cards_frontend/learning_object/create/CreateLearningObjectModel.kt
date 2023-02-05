package de.fantjastisch.cards_frontend.learning_object.create

import de.fantjastisch.cards_frontend.card.CardRepository
import de.fantjastisch.cards_frontend.card.CardSelectItem
import de.fantjastisch.cards_frontend.category.CategoryRepository
import de.fantjastisch.cards_frontend.category.CategorySelectItem
import de.fantjastisch.cards_frontend.components.SingleSelectItem
import de.fantjastisch.cards_frontend.glossary.CardsFilters
import de.fantjastisch.cards_frontend.learning_box.LearningBoxRepository
import de.fantjastisch.cards_frontend.learning_box.card_to_learning_box.CardToLearningBoxRepository
import de.fantjastisch.cards_frontend.learning_object.LearningObject
import de.fantjastisch.cards_frontend.learning_object.LearningObjectRepository
import de.fantjastisch.cards_frontend.learning_system.LearningSystemRepository
import de.fantjastisch.cards_frontend.util.*
import de.fantjastisch.cards_frontend.util.RepoResult.*
import de.fantjastisch.cards_frontend.util.RepoResult.UnexpectedErrorType.*
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.openapitools.client.models.*
import java.util.*

/**
 * Kapselt die Logik für das [CreateLearningObjectViewModel].
 * Fungiert als Vermittler zwischen Repository und ViewModel.
 *
 * @property learningObjectRepository Repository Lernobjekte.
 * @property learningSystemRepository Repository Lernsysteme.
 * @property learningBoxRepository Repository Lernboxen.
 * @property cardToLearningBoxRepository Repository Karten in Lernboxen.
 * @property categoryRepository Repository Kategorien.
 * @property cardRepository Repository Karten.
 * @property validator Validator, zum validieren der Eingabewerte.
 *
 * @author Semjon Nirmann, Jessica Repty, Freja Sender
 */
class CreateLearningObjectModel(
    private val learningObjectRepository: LearningObjectRepository = LearningObjectRepository(),
    private val learningSystemRepository: LearningSystemRepository = LearningSystemRepository(),
    private val learningBoxRepository: LearningBoxRepository = LearningBoxRepository(),
    private val cardToLearningBoxRepository: CardToLearningBoxRepository = CardToLearningBoxRepository(),
    private val categoryRepository: CategoryRepository = CategoryRepository(),
    private val cardRepository: CardRepository = CardRepository(),
    private val validator: CreateLearningObjectValidator = CreateLearningObjectValidator()
) {

    var learningSystems: List<LearningSystemEntity> = emptyList()

    /**
     * Hält Daten für das hinzufügende Lernobjekt.
     *
     * @property cardSelectItems Alle Karten.
     * @property categorySelectItems Alle Kategorien.
     * @property learningSystems Alle Lernsysteme.
     */
    data class CreateLearningObject(
        val categorySelectItems: List<CategorySelectItem>,
        val learningSystems: List<SingleSelectItem>
    )

    /**
     * Erstellt eine Instanz der [CreateLearningObject].
     *
     * @return RepoResult<CreateLearningObject> OnSuccess: Eine Instanz der [CreateLearningObject],
     *  die alle Karten, Kategorien und Lernsysteme hält.
     */
    @Suppress("UNCHECKED_CAST")
    suspend fun initializePage(): RepoResult<CreateLearningObject> = coroutineScope {
        // Runs coroutines in parallel and waits until all of them are done
        val (categoriesResult, learningSystemsResult) = awaitAll(
            async { categoryRepository.getPage() },
            async { learningSystemRepository.getPage() }
        )

        // Wenn alle Resultate da
        when {
            categoriesResult is Success
                    && learningSystemsResult is Success -> {
                // Verarbeite
                val categories = categoriesResult.result as List<CategoryEntity>
                learningSystems = learningSystemsResult.result as List<LearningSystemEntity>


                val categorySelectItems = categories.toUnselectedCategorySelectItems()

                val learningSystemSelectItems = learningSystems.map { learningSystem ->
                    SingleSelectItem(
                        id = learningSystem.id,
                        label = learningSystem.label,
                    )
                }

                // Objekt für Rückgabe füllen
                Success(
                    CreateLearningObject(
                        categorySelectItems = categorySelectItems,
                        learningSystems = learningSystemSelectItems
                    )
                )
            }
            categoriesResult.isNetworkError() || learningSystemsResult.isNetworkError()
            -> ServerError(NETWORK_ERROR)
            else -> ServerError(UNEXPECTED_ERROR)
        }
    }

    /**
     * Funktion die Karten lädt.
     *
     * @return RepoResult mit einer Liste von Karten.
     */
    suspend fun loadCards(): RepoResult<List<CardEntity>> = coroutineScope {
        cardRepository.getPage(
            categoryIds = CardsFilters.filters.value.categories.ifEmpty { null },
            search = CardsFilters.filters.value.search.ifEmpty { null },
            tag = CardsFilters.filters.value.tag.ifEmpty { null },
            sort = CardsFilters.filters.value.sort
        )
    }

    /**
     * Holt alle Karten, die zu der asugewählten Kategorien gehören, indem die Anfrage,
     * an das Repository weitergeleitet wird.
     *
     * @param categories Kategorien, deren Karten geholt werden.
     * @return RepoResult<List<CardEntity>> Ein parametrisiertes Objekt, das darstellt, ob alle Persistenzoperationen erfolgreich durchgeführt
     * werden konnten, oder nicht.
     */
    private suspend fun getCardsFromCategories(
        categories: List<CategorySelectItem>
    ): RepoResult<List<CardEntity>> {
        val checkedCategories =
            categories.filter { category -> category.isChecked }.map { it.id }
        return if (checkedCategories == emptyList<UUID>()) {
            Success(emptyList())
        } else {
            return cardRepository.getPage(
                categoryIds = checkedCategories,
                search = null,
                tag = null,
                sort = null,
            )
        }
    }

    /**
     * Sammelt Daten für ein Lernobjekt und fügt dies in die Datenbank ein.
     *
     * @param learningObjectLabel Die Bezeichnung des Lernobjekts.
     * @param selectedSystem Das ausgewählte Lernsystem.
     * @param categories Die Liste von [CategorySelectItem]. Wenn isChecked = true, dann werden die Karten der Kategorie hinzugefügt.
     * @param cards Die Liste von [CardSelectItem]. Wenn isChecked = true, dann werden die Karten hinzugefügt.
     * @return RepoResult<Unit> Ein parametrisiertes Objekt, das darstellt, ob alle Persistenzoperationen erfolgreich durchgeführt
     * werden konnten, oder nicht.
     */
    suspend fun addLearningObject(
        learningObjectLabel: String,
        selectedSystem: SingleSelectItem?,
        categories: List<CategorySelectItem>,
        cards: List<CardSelectItem>
    ): RepoResult<Unit> {

        val errors = validator.validate(
            selectedSystem = selectedSystem,
            learningObjectLabel = learningObjectLabel
        )

        if (errors.isNotEmpty()) {
            return Error(errors = errors)
        }

        val learningSystem = learningSystems.find { it.id == selectedSystem?.id }!!

        val cardsFromSelectedCategories = getCardsFromCategories(categories = categories)

        // Hole Lernsystem und Karten aus angekreuzten Kategorien aus DB
        return when {
            cardsFromSelectedCategories is Success -> {
                // Sammle einzeln hinzugefügte Karten
                val cardsToInsert: MutableList<UUID> =
                    cards
                        .filter { card -> card.isChecked }
                        .map { card -> card.card.id }
                        .toMutableList()

                // Füge Karten aus angekreuzten Kategorien hinzu zu allen einzusetzenden Karten
                cardsToInsert.addAll(cardsFromSelectedCategories.result.map { it.id })

                val learningObject =
                    LearningObject(
                        label = learningObjectLabel,
                        learningSystemId = selectedSystem!!.id
                    )
                // Füge Lernobjekt in DB ein
                return learningObjectRepository.insert(
                    learningObject = learningObject,
                    boxLabels = learningSystem.boxLabels,
                    cards = cardsToInsert
                )

            }
            cardsFromSelectedCategories.isNetworkError() -> ServerError(NETWORK_ERROR)
            else -> ServerError(UNEXPECTED_ERROR)
        }
    }
}