package de.fantjastisch.cards_frontend.learning_object

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.card.CardSelectItem
import de.fantjastisch.cards_frontend.category.CategorySelectItem
import de.fantjastisch.cards_frontend.components.SingleSelectItem
import de.fantjastisch.cards_frontend.infrastructure.RepoResult
import de.fantjastisch.cards_frontend.infrastructure.fold
import kotlinx.coroutines.launch
import java.util.*

class CreateLearningObjectViewModel(
    private val model: CreateLearningObjectModel = CreateLearningObjectModel()
//= extends ViewModel
) : ViewModel() {

    // states, die vom view gelesen werden können -> automatisches Update vom View.
    val allCategories = mutableStateOf(listOf<CategorySelectItem>())
    val allCards = mutableStateOf(listOf<CardSelectItem>())
    val learningSystems = mutableStateOf(listOf<SingleSelectItem>())
    val selectedSystem = mutableStateOf<SingleSelectItem?>(null)
    val error = mutableStateOf<String?>(null)
    val isFinished = mutableStateOf(false)

    val learningObjectLabel = mutableStateOf("")

    // constructor (wird ganz am Anfang aufgerufen)
    init {
        viewModelScope.launch {
            model
                .initializePage()
                .fold(
                    onSuccess = { learningObject ->
                        error.value = ""
                        allCategories.value = learningObject.categorySelectItems
                        allCards.value = learningObject.cardSelectItems
                        learningSystems.value = learningObject.learningSystems
                    },
                    onValidationError = { error.value = "Fehler bei der Eingabevalidierung." },
                    onUnexpectedError = { error.value = "Ein unbekannter Fehler ist aufgetreten." },
                )
        }
    }

    fun setLearningObjectLabel(value: String) {
        learningObjectLabel.value = value
    }

    fun onCardSelected(id: UUID) {
        allCards.value = allCards.value.map {
            if (it.card.id == id) {
                it.copy(isChecked = !it.isChecked)
            } else {
                it
            }
        }
    }

    fun onCategorySelected(id: UUID) {
        allCategories.value = allCategories.value.map {
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
        val learningSystemId = selectedSystem.value!!.id
        val learningObject =
            LearningObject(label = learningObjectLabel.value, learningSystemId = learningSystemId)
        viewModelScope.launch {
            val response = model.addLearningObject(
                learningObject = learningObject,
                selectedSystemId = selectedSystem.value!!.id,
                categories = allCategories.value,
                cards = allCards.value
            )

            when (response) {
                is RepoResult.Success -> isFinished.value = true
                is RepoResult.Error, is RepoResult.ServerError -> null
            }
        }

    }
}