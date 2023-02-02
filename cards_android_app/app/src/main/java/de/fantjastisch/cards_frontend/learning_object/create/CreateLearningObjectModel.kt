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

class CreateLearningObjectModel(
    private val learningObjectRepository: LearningObjectRepository = LearningObjectRepository(),
    private val learningSystemRepository: LearningSystemRepository = LearningSystemRepository(),
    private val learningBoxRepository: LearningBoxRepository = LearningBoxRepository(),
    private val cardToLearningBoxRepository: CardToLearningBoxRepository = CardToLearningBoxRepository(),
    private val categoryRepository: CategoryRepository = CategoryRepository(),
    private val cardRepository: CardRepository = CardRepository(),
) {

    data class CreateLearningObject(
        val cardSelectItems: List<CardSelectItem>,
        val categorySelectItems: List<CategorySelectItem>,
        val learningSystems: List<SingleSelectItem>
    )

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

    private suspend fun getLearningSystemFromInput(selectedSystemId: UUID): LearningSystemEntity? {
        return when (val response = learningSystemRepository.getLearningSystem(selectedSystemId)) {
            is RepoResult.Success -> response.result
            is RepoResult.Error, is RepoResult.ServerError -> null // TODO
        }
    }

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

    private suspend fun insertLearningBox(learningBox: LearningBox): RepoResult<Unit> =
        learningBoxRepository.insert(learningBox = learningBox)

    private suspend fun insertCardsIntoBox(
        learningBox: LearningBox, cardIds: MutableList<UUID>
    ): RepoResult<Unit> = cardToLearningBoxRepository.insertCards(cardIds, learningBox.id)

    private suspend fun insertLearningObject(
        learningObject: LearningObject,
    ): RepoResult<Unit> = learningObjectRepository.insert(learningObject = learningObject)

    suspend fun addLearningObject(
        learningObject: LearningObject,
        selectedSystemId: UUID,
        categories: List<CategorySelectItem>,
        cards: List<CardSelectItem>
    ): RepoResult<Unit> {
        // Hole Lernsystem aus DB
        val learningSystem = getLearningSystemFromInput(selectedSystemId)
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