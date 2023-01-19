package de.fantjastisch.cards_frontend.learning_object

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import de.fantjastisch.cards_frontend.card.CardRepository
import de.fantjastisch.cards_frontend.category.CategoryRepository
import de.fantjastisch.cards_frontend.category.CategorySelectItem
import de.fantjastisch.cards_frontend.components.SingleSelectItem
import de.fantjastisch.cards_frontend.config.AppDatabase
import de.fantjastisch.cards_frontend.learning_box.*
import de.fantjastisch.cards_frontend.learning_box.card_to_learning_box.CardToLearningBoxRepository
import de.fantjastisch.cards_frontend.learning_box.card_to_learning_box.InternalCardToLearningBoxRepository
import de.fantjastisch.cards_frontend.learning_system.LearningSystemRepository
import org.openapitools.client.models.CardEntity
import org.openapitools.client.models.LearningSystemEntity
import java.util.*

class CreateLearningObjectViewModel(
        private val learningObjectRepository: LearningObjectRepository = LearningObjectRepository(InternalLearningObjectRepository(AppDatabase.database.learningObjectDao())),
        private val learningSystemRepository: LearningSystemRepository = LearningSystemRepository(),
        private val learningBoxRepository: LearningBoxRepository = LearningBoxRepository(InternalLearningBoxRepository(AppDatabase.database.learningBoxDao())),
        private val cardToLearningBoxRepository: CardToLearningBoxRepository = CardToLearningBoxRepository(InternalCardToLearningBoxRepository(AppDatabase.database.cardToLearningBoxDao())),
        private val categoryRepository: CategoryRepository = CategoryRepository(),
        private val cardRepository: CardRepository = CardRepository(),
//= extends ViewModel
) : ViewModel() {

    // states, die vom view gelesen werden können -> automatisches Update vom View.
//    val learningObjects = mutableStateOf(listOf<>())

    val categories = mutableStateOf(listOf<CategorySelectItem>())
    val cards = mutableStateOf(mutableListOf<UUID>())
    val learningSystems = mutableStateOf(listOf<SingleSelectItem>())
    val selectedSystem = mutableStateOf<SingleSelectItem?>(null)
    val errors = mutableStateOf<String?>(null)
    val isFinished = mutableStateOf(false)

    val learningObjectLabel = mutableStateOf("")

    // constructor (wird ganz am Anfang aufgerufen)
    init {
        categoryRepository.getPage(
                onSuccess = {
                    errors.value = null
                    categories.value = it.map { category ->
                        CategorySelectItem(
                                id = category.id,
                                label = category.label,
                                isChecked = false,
                        )
                    }
                },
                onFailure = {
                    errors.value = "Konnte keine Kategorien einholen."
                },
        )
        learningSystemRepository.getPage(
                onSuccess = {
                    errors.value = null
                    learningSystems.value = it.map { learningSystem ->
                        SingleSelectItem(
                                id = learningSystem.id,
                                label = learningSystem.label,
                        )
                    }
                },
                onFailure = {
                    errors.value = "Konnte keine Lernsysteme einholen."
                },
        )
    }

    fun onCategorySelected(id: UUID) {
        categories.value = categories.value.map {
            if (it.id == id) {
                it.copy(isChecked = !it.isChecked)
            } else {
                it
            }
        }
    }

    fun onLearningSystemSelected(id: UUID) {
        selectedSystem.value = learningSystems.value.first { it.id == id }
    }

    fun onAddLearningObjectClicked() {
        errors.value = null
        val learningSystemId = selectedSystem.value!!.id
        val learningObject = LearningObject(label = learningObjectLabel.value, learningSystemId = learningSystemId)
        learningObjectRepository.insert(
                learningObject = learningObject,
                onSuccess = {
                    getLearningSystemFromInput(learningObject)
                    isFinished.value = true
                },
                onFailure = {
                    errors.value = "Could not insert learning object into DB"
                }
        )
    }

    private fun getLearningSystemFromInput(learningObject: LearningObject) {
        learningSystemRepository.getLearningSystem(selectedSystem.value!!.id,
                onSuccess = {
                    errors.value = null
                    val learningSystem = it
                    getCardsFromCategories(learningSystem, learningObject)
                },
                onFailure = {
                    errors.value = "Could not get learning system."
                })
    }

    private fun getCardsFromCategories(
        learningSystem: LearningSystemEntity,
        learningObject: LearningObject
    ) {
        cardRepository.getPage(
            categoryIds = categories.value.map { it.id },
            search = null,
            tag = null,
            sort = null,
            onSuccess = {
                createLearningBoxesFromCards(it, learningSystem, learningObject)
            },
            onFailure = {
                errors.value = "Could not get cards."
            }
        )
    }


    private fun createLearningBoxesFromCards(it: List<CardEntity>, learningSystem: LearningSystemEntity, learningObject: LearningObject) {
        val cardIds = it.map { it.id }
        cards.value.addAll(cardIds)
        learningSystem.boxLabels.forEachIndexed { index, label ->
            val learningBox = LearningBox(
                    learningObjectId = learningObject.id,
                    boxNumber = index,
                    label = label
            )
            insertCardsIntoBox(learningBox, index)
        }
    }

    private fun insertCardsIntoBox(
        learningBox: LearningBox,
        index: Int
    ) {
        learningBoxRepository.insert(
            learningBox = learningBox,
            onSuccess = {
                if (index == 0) {
                    cardToLearningBoxRepository.insertCardsForBox(
                        cards.value,
                        learningBox.id,
                        onSuccess = { errors.value = null },
                        onFailure = {
                            errors.value =
                                "Could not insert relationship from cards to learning box."
                        })
                }
            },
            onFailure = {
                errors.value = "Could not insert learning box."
            }
        )
    }
}