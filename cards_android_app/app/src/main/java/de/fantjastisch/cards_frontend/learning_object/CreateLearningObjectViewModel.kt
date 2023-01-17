package de.fantjastisch.cards_frontend.learning_object

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import de.fantjastisch.cards_frontend.category.CategoryRepository
import de.fantjastisch.cards_frontend.category.CategorySelectItem
import de.fantjastisch.cards_frontend.config.AppDatabase
import de.fantjastisch.cards_frontend.learning_system.LearningSystemRepository
import de.fantjastisch.cards_frontend.learning_system.SingleSelectItem
import java.util.*

class CreateLearningObjectViewModel(
        private val learningObjectRepository: LearningObjectRepository = LearningObjectRepository(InternalLearningObjectRepository(AppDatabase.database.learningObjectDao())),
        private val learningSystemRepository: LearningSystemRepository = LearningSystemRepository(),
        private val categoryRepository: CategoryRepository = CategoryRepository(),
//= extends ViewModel
) : ViewModel() {

    // states, die vom view gelesen werden können -> automatisches Update vom View.
//    val learningObjects = mutableStateOf(listOf<>())

    val categories = mutableStateOf(listOf<CategorySelectItem>())
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
        learningObjectRepository.insert(
                learningObject = LearningObject(label=learningObjectLabel.value),
                onSuccess = {
                    isFinished.value = true
                },
                onFailure = {
                    errors.value = "Could not insert learning object into DB"
                }
        )
    }
}