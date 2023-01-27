package de.fantjastisch.cards_frontend.learning_object

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.card.CardRepository
import de.fantjastisch.cards_frontend.card.CardSelectItem
import de.fantjastisch.cards_frontend.category.CategoryRepository
import de.fantjastisch.cards_frontend.category.CategorySelectItem
import de.fantjastisch.cards_frontend.components.SingleSelectItem
import de.fantjastisch.cards_frontend.infrastructure.RepoResult
import de.fantjastisch.cards_frontend.infrastructure.fold
import de.fantjastisch.cards_frontend.learning_box.LearningBox
import de.fantjastisch.cards_frontend.learning_box.LearningBoxRepository
import de.fantjastisch.cards_frontend.learning_box.card_to_learning_box.CardToLearningBoxRepository
import de.fantjastisch.cards_frontend.learning_system.LearningSystemRepository
import kotlinx.coroutines.launch
import org.openapitools.client.models.CardEntity
import org.openapitools.client.models.LearningSystemEntity
import java.util.*

class CreateLearningObjectViewModel(
    private val learningObjectRepository: LearningObjectRepository = LearningObjectRepository(),
    private val learningSystemRepository: LearningSystemRepository = LearningSystemRepository(),
    private val learningBoxRepository: LearningBoxRepository = LearningBoxRepository(),
    private val cardToLearningBoxRepository: CardToLearningBoxRepository = CardToLearningBoxRepository(),
    private val categoryRepository: CategoryRepository = CategoryRepository(),
    private val cardRepository: CardRepository = CardRepository(),
//= extends ViewModel
) : ViewModel() {

    // states, die vom view gelesen werden können -> automatisches Update vom View.
    // val learningObjects = mutableStateOf(listOf<>())

    val categories = mutableStateOf(listOf<CategorySelectItem>())
    val cards = mutableStateOf(listOf<CardSelectItem>())
    val cardIds = mutableStateOf(mutableListOf<UUID>())
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
                errors.value = "Ein Netzwerkfehler ist aufgetreten."
            },
        )
        viewModelScope.launch {
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
                    errors.value = "Ein Netzwerkfehler ist aufgetreten."
                },
            )
        }

        viewModelScope.launch {
            val result = cardRepository.getPage(
                categoryIds = null,
                search = null,
                tag = null,
                sort = null
            )
            when (result) {
                is RepoResult.Success -> {
                    errors.value = null
                    cards.value = result.result.map { card ->
                        CardSelectItem(
                            card = card,
                            isChecked = false
                        )
                    }
                }

                is RepoResult.Error,
                is RepoResult.ServerError -> {
                    errors.value = "Ein Netzwerkfehler ist aufgetreten."
                }
            }
        }
    }

    fun onCardSelected(id: UUID) {
        cards.value = cards.value.map {
            if (it.card.id == id) {
                it.copy(isChecked = !it.isChecked)
            } else {
                it
            }
        }
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
        val learningObject =
            LearningObject(label = learningObjectLabel.value, learningSystemId = learningSystemId)
        viewModelScope.launch {
            learningObjectRepository.insert(
                learningObject = learningObject,
                onSuccess = {
                    getLearningSystemFromInput(learningObject)
                },
                onFailure = {
                    errors.value = "Ein Netzwerkfehler ist aufgetreten."
                }
            )
        }
    }

    private fun getLearningSystemFromInput(learningObject: LearningObject) {
        viewModelScope.launch {
            learningSystemRepository.getLearningSystem(selectedSystem.value!!.id)
                .fold(
                    onSuccess = {
                        errors.value = null
                        val learningSystem = it
                        getCardsFromCategories(learningSystem, learningObject)
                    },
                    onValidationError = { errors.value = "Fehler bei der Eingabevalidierung." },
                    onUnexpectedError = {
                        errors.value = "Ein unbekannter Fehler ist aufgetreten."
                    })
        }
    }

    private fun getCardsFromCategories(
        learningSystem: LearningSystemEntity,
        learningObject: LearningObject
    ) {
        val checkedCategories =
            categories.value.filter { category -> category.isChecked }.map { it.id }
        if (checkedCategories == emptyList<UUID>()) {
            createLearningBoxesFromCards(emptyList(), learningSystem, learningObject)
        } else {
            viewModelScope.launch {
                cardRepository.getPage(
                    categoryIds = checkedCategories,
                    search = null,
                    tag = null,
                    sort = null,
                ).fold(
                    onSuccess = {
                        createLearningBoxesFromCards(it, learningSystem, learningObject)
                    },
                    onValidationError = { errors.value = "Fehler bei der Eingabevalidierung." },
                    onUnexpectedError = { errors.value = "Ein unbekannter Fehler ist aufgetreten." }
                )
            }
        }

    }

    private fun createLearningBoxesFromCards(
        cardsFromCategories: List<CardEntity>,
        learningSystem: LearningSystemEntity,
        learningObject: LearningObject
    ) {
        val cardsFromCategoriesAsIds = cardsFromCategories.map { it.id }
        this.cardIds.value.addAll(cardsFromCategoriesAsIds) // cardIds from categories
        this.cardIds.value.addAll(cards.value.filter { card -> card.isChecked }
            .map { card -> card.card.id })
        // cardIds from cardSelectItems
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
        viewModelScope.launch {
            learningBoxRepository.insert(
                learningBox = learningBox,
                onSuccess = {
                    if (index == 0) {
                        viewModelScope.launch {
                            cardToLearningBoxRepository.insertCards(
                                cardIds.value,
                                learningBox.id
                            ).fold(
                                onSuccess = {
                                    errors.value = null
                                    isFinished.value = true
                                },
                                onValidationError = {
                                    errors.value = "Fehler bei der Eingabevalidierung."
                                },
                                onUnexpectedError = {
                                    errors.value = "Ein unbekannter Fehler ist aufgetreten."
                                })
                        }
                    }
                },
                onFailure = {
                    errors.value = "Ein Netzwerkfehler ist aufgetreten."
                }
            )
        }
    }
}