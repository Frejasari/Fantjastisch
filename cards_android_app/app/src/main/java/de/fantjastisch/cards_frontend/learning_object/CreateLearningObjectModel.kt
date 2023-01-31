package de.fantjastisch.cards_frontend.learning_object

import de.fantjastisch.cards_frontend.card.CardRepository
import de.fantjastisch.cards_frontend.card.CardSelectItem
import de.fantjastisch.cards_frontend.category.CategoryRepository
import de.fantjastisch.cards_frontend.category.CategorySelectItem
import de.fantjastisch.cards_frontend.components.SingleSelectItem
import de.fantjastisch.cards_frontend.infrastructure.RepoResult
import de.fantjastisch.cards_frontend.learning_box.LearningBox
import de.fantjastisch.cards_frontend.learning_box.LearningBoxRepository
import de.fantjastisch.cards_frontend.learning_box.card_to_learning_box.CardToLearningBoxRepository
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
        val allCategories: List<CategorySelectItem>,
        val learningSystems: List<SingleSelectItem>
    )

    @Suppress("UNCHECKED_CAST")
    suspend fun initializePage(): RepoResult<CreateLearningObject> = coroutineScope {
        // Runs coroutines in parallel and waits until all of them are done
        val (cardsResult, categoriesResult, learningSystemsResult) = awaitAll(
            async {
                cardRepository.getPage(
                    null,
                    null,
                    null,
                    false
                )
            },
            async { categoryRepository.getPage() },
            async { learningSystemRepository.getPage() })

        when {
            cardsResult is RepoResult.Success &&
                    categoriesResult is RepoResult.Success &&
                    learningSystemsResult is RepoResult.Success -> {
                val cards = (cardsResult.result) as List<CardEntity>
                val cardSelectItems = cards.map { card ->
                    CardSelectItem(
                        card = card, isChecked = false
                    )
                }

                val categories = categoriesResult.result as List<CategoryEntity>
                val categorySelectItems = categories.map { category ->
                    CategorySelectItem(
                        id = category.id,
                        label = category.label,
                        isChecked = false,
                    )
                }

                val learningSystems = learningSystemsResult.result as List<LearningSystemEntity>
                val learningSystemSelectItems = learningSystems.map { learningSystem ->
                    SingleSelectItem(
                        id = learningSystem.id,
                        label = learningSystem.label,
                    )
                }
                RepoResult.Success(
                    CreateLearningObject(
                        cardSelectItems = cardSelectItems,
                        allCategories = categorySelectItems,
                        learningSystems = learningSystemSelectItems
                    )
                )
            }
            else -> RepoResult.Error(emptyList())
        }
    }

    suspend fun getLearningSystemFromInput(selectedSystemId: UUID): LearningSystemEntity? {
        return when (val response = learningSystemRepository.getLearningSystem(selectedSystemId)) {
            is RepoResult.Success -> response.result
            is RepoResult.Error, is RepoResult.ServerError -> null // TODO
        }
    }

    suspend fun getCardsFromCategories(
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

    suspend fun createLearningBoxesFromCards(
        cardsFromCategories: List<CardEntity>,
        learningSystem: LearningSystemEntity,
        learningObject: LearningObject,
        cardIds: MutableList<UUID>,
        cards: List<CardSelectItem>
    ) {
        val cardsFromCategoriesAsIds = cardsFromCategories.map { it.id }
        cardIds.addAll(cardsFromCategoriesAsIds) // cardIds from categories
        cardIds.addAll(cards.filter { card -> card.isChecked }.map { card -> card.card.id })
        // cardIds from cardSelectItems
        learningSystem.boxLabels.forEachIndexed { index, label ->
            val learningBox = LearningBox(
                learningObjectId = learningObject.id, boxNumber = index, label = label
            )
            val result = insertLearningBox(learningBox = learningBox)

            if (result is RepoResult.Success) {
                insertCardsIntoBox(
                    learningBox = learningBox, cardIds = if (index == 0) cardIds else mutableListOf()
                )
            } else {
                // TODO: ????
            }

        }
    }

    suspend fun insertLearningBox(learningBox: LearningBox): RepoResult<Unit> =
        learningBoxRepository.insert(learningBox = learningBox)

    suspend fun insertCardsIntoBox(
        learningBox: LearningBox, cardIds: MutableList<UUID>
    ): RepoResult<Unit> = cardToLearningBoxRepository.insertCards(cardIds, learningBox.id)

    suspend fun insertLearningObject(
        learningObject: LearningObject,
    ): RepoResult<Unit> = learningObjectRepository.insert(learningObject = learningObject)
}
