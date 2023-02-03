package de.fantjastisch.cards_frontend.learning_object.create

import de.fantjastisch.cards_frontend.card.CardRepository
import de.fantjastisch.cards_frontend.card.CardSelectItem
import de.fantjastisch.cards_frontend.category.CategoryRepository
import de.fantjastisch.cards_frontend.category.CategorySelectItem
import de.fantjastisch.cards_frontend.components.SingleSelectItem
import de.fantjastisch.cards_frontend.infrastructure.RepoResult
import de.fantjastisch.cards_frontend.learning_box.LearningBox
import de.fantjastisch.cards_frontend.learning_box.LearningBoxRepository
import de.fantjastisch.cards_frontend.learning_box.card_to_learning_box.CardToLearningBoxRepository
import de.fantjastisch.cards_frontend.learning_object.LearningObject
import de.fantjastisch.cards_frontend.learning_object.LearningObjectRepository
import de.fantjastisch.cards_frontend.learning_system.LearningSystemRepository
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

    /**
     * Hält Daten für das hinzufügende Lernobjekt.
     *
     * @property cardSelectItems Alle Karten.
     * @property categorySelectItems Alle Kategorien.
     * @property learningSystems Alle Lernsysteme.
     */
    data class CreateLearningObject(
        val cardSelectItems: List<CardSelectItem>,
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
        val (cardsResult, categoriesResult, learningSystemsResult) = awaitAll(
            async {
                cardRepository.getPage(
                    categoryIds = null,
                    search = null,
                    tag = null,
                    sort = false
                )
            },
            async { categoryRepository.getPage() },
            async { learningSystemRepository.getPage() })

        when {
            // Wenn alle Resultate da
            cardsResult is RepoResult.Success &&
                    categoriesResult is RepoResult.Success &&
                    learningSystemsResult is RepoResult.Success -> {
                // Verarbeite
                val cards = (cardsResult.result) as List<CardEntity>
                val categories = categoriesResult.result as List<CategoryEntity>
                val learningSystems = learningSystemsResult.result as List<LearningSystemEntity>

                val cardSelectItems = cards.map { card ->
                    CardSelectItem(
                        card = card, isChecked = false
                    )
                }

                val categorySelectItems = categories.map { category ->
                    CategorySelectItem(
                        id = category.id,
                        label = category.label,
                        isChecked = false,
                    )
                }

                val learningSystemSelectItems = learningSystems.map { learningSystem ->
                    SingleSelectItem(
                        id = learningSystem.id,
                        label = learningSystem.label,
                    )
                }

                // Objekt für Rückgabe füllen
                RepoResult.Success(
                    CreateLearningObject(
                        cardSelectItems = cardSelectItems,
                        categorySelectItems = categorySelectItems,
                        learningSystems = learningSystemSelectItems
                    )
                )
            }
            // Sonst Fehler
            else -> RepoResult.ServerError()
        }
    }

    /**
     * Ermittelt, ob das ausgewählte Lernsystem in der Datenbank vorhanden ist.
     *
     * @param selectedSystemId Die UUID des Lernsystems, das ausgewählt wurde.
     * @return LearningSystemEntity? Wenn die Eingabe valide ist dann [LearningSystemEntity]-Entität, sonst null
     */
    private suspend fun getLearningSystemFromInput(selectedSystemId: UUID): LearningSystemEntity? {
        return when (val response = learningSystemRepository.getLearningSystem(selectedSystemId)) {
            is RepoResult.Success -> response.result
            is RepoResult.Error, is RepoResult.ServerError -> null // TODO
        }
    }

    /**
     * Holt alle Karten, die zu der asugewählten Kategorien gehören, indem die Anfrage,
     * an das Repository weitergeleitet wird.
     *
     * @param categories Kategorien, deren Karten geholt werden.
     * @return List<CardEntity>? Wenn es Karten zu Kategorien gibt dann eine Liste von [CardEntity]
     *  sonst null
     */
    private suspend fun getCardsFromCategories(
        categories: List<CategorySelectItem>
    ): List<CardEntity>? {
        val checkedCategories = categories.filter { category -> category.isChecked }.map { it.id }
        if (checkedCategories == emptyList<UUID>()) {
            return emptyList()
        } else {
            return when (val response = cardRepository.getPage(
                categoryIds = checkedCategories,
                search = null,
                tag = null,
                sort = null,
            )) {
                is RepoResult.Success -> response.result
                is RepoResult.Error, is RepoResult.ServerError -> null // TODO
            }
        }
    }

    /**
     * Fügt die Lernboxen und die ausgewählte Karten in die
     * entsprechenden Datenbanken ein.
     *
     * @param learningSystem Das Lernsystem des zu erstellenden Lernobjekts.
     * @param learningObject Das Lernobjekt, zu dem die Lernboxen und Karten gehören.
     * @param cardsToInsert Die Karten, die eingefügt werden sollen.
     * @return RepoResult<Unit> TODO
     */
    private suspend fun insertLearningBoxesWithCards(
        learningSystem: LearningSystemEntity,
        learningObject: LearningObject,
        cardsToInsert: MutableList<UUID>,
    ): RepoResult<Unit> {
        learningSystem.boxLabels.forEachIndexed { index, label ->
            // Erstelle die Lernbox
            val learningBox = LearningBox(
                learningObjectId = learningObject.id, boxNumber = index, label = label
            )
            // Füge sie in die DB ein
            val result = insertLearningBox(learningBox = learningBox)

            if (result is RepoResult.Success) {
                // Setze gewählte Karten in die erste Lernbox ein, sonst eine leere Liste
                val cardInsertionResponse = insertCardsIntoBox(
                    learningBox = learningBox,
                    cardIds = if (index == 0) cardsToInsert else mutableListOf()
                )
                if (cardInsertionResponse is RepoResult.ServerError ||
                    cardInsertionResponse is RepoResult.Error
                ) {
                    return RepoResult.ServerError() // Konnte Karten nicht in Lernbox einsetzen
                }
            } else {
                return RepoResult.ServerError() // Konnte Lernbox nicht einsetzen
            }
        }
        return RepoResult.Success(Unit)
    }

    /**
     * Fügt eine Lernbox in die Datenbank ein, indem die Anfrage an das Repository weitergeleitet wird.
     *
     * @param learningBox Die hinzufügende Lernbox.
     * @return RepoResult<Unit> TODO
     */
    private suspend fun insertLearningBox(learningBox: LearningBox): RepoResult<Unit> =
        learningBoxRepository.insert(learningBox = learningBox)

    /**
     * Fügt Karten zu einer Lernbox hinzu, indem die Anfrage an das Repository weitergeleitet wird.
     *
     * @param learningBox Die Lernbox, zu der die Karten hinzugefügt werden sollen.
     * @param cardIds Die Liste der UUIDs von Karten, die zu einer Lernbox hinzugefügt werden sollen.
     * @return RepoResult<Unit> TODO
     */
    private suspend fun insertCardsIntoBox(
        learningBox: LearningBox, cardIds: MutableList<UUID>
    ): RepoResult<Unit> = cardToLearningBoxRepository.insertCards(cardIds, learningBox.id)


    /**
     * Fügt ein Lernobjekt in die Datenbank ein, indem die Anfrage an das Repository weitergeleitet wird.
     *
     * @param learningObject Das Lernobjekt, welches hinzugefügt wird.
     * @return RepoResult<Unit> TODO
     */
    private suspend fun insertLearningObject(
        learningObject: LearningObject,
    ): RepoResult<Unit> = learningObjectRepository.insert(learningObject = learningObject)

    /**
     * Sammelt Daten für ein Lernobjekt und fügt dies in die Datenbank ein.
     *
     * @param learningObjectLabel Die Bezeichnung des Lernobjekts.
     * @param selectedSystem Das ausgewählte Lernsystem.
     * @param categories Die Liste von [CategorySelectItem]. Wenn isChecked = true, dann werden die Karten der Kategorie hinzugefügt.
     * @param cards Die Liste von [CardSelectItem]. Wenn isChecked = true, dann werden die Karten hinzugefügt.
     * @return RepoResult<Unit> TODO
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
            return RepoResult.Error(errors = errors)
        }

        // Hole Lernsystem aus DB
        val learningSystem = getLearningSystemFromInput(selectedSystem!!.id)
            ?: // Konnte Lernsystem nicht finden, kann Lernboxen nicht einfügen.
            return RepoResult.ServerError()

        // Sammle einzeln hinzugefügte Karten
        val cardsToInsert: MutableList<UUID> =
            cards.filter { card -> card.isChecked }.map { card -> card.card.id }.toMutableList()

        // Hole Karten aus angekreuzten Kategorien und füge sie hinzu
        val cardsFromSelectedCategories = getCardsFromCategories(categories = categories)
        if (cardsFromSelectedCategories != null) {
            cardsToInsert.addAll(cardsFromSelectedCategories.map { it.id })
        }

        val learningObject =
            LearningObject(label = learningObjectLabel, learningSystemId = selectedSystem.id)
        // Füge Lernobjekt in DB ein
        val insertLearningObjectResponse = insertLearningObject(learningObject)

        if (insertLearningObjectResponse is RepoResult.Error ||
            insertLearningObjectResponse is RepoResult.ServerError
        ) {
            // Konnte Lernobjekt nicht in DB einsetzen
            return RepoResult.ServerError()
        }

        return insertLearningBoxesWithCards(
            learningSystem = learningSystem,
            learningObject = learningObject,
            cardsToInsert = cardsToInsert
        )
    }
}