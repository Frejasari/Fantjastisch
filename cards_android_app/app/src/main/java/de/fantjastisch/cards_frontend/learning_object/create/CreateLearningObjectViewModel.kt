package de.fantjastisch.cards_frontend.learning_object.create

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.card.CardSelectItem
import de.fantjastisch.cards_frontend.category.CategorySelectItem
import de.fantjastisch.cards_frontend.components.SingleSelectItem
import de.fantjastisch.cards_frontend.infrastructure.RepoResult
import de.fantjastisch.cards_frontend.infrastructure.fold
import kotlinx.coroutines.launch
import org.openapitools.client.models.ErrorEntryEntity
import java.util.*

/**
 * Stellt die Daten für die [LearningObjectView] bereit und nimmt seine Anfragen entgegen.
 *
 * @property model Das zugehörige Model, welches die Logik kapselt.
 *
 * @author Semjon Nirmann, Jessica Repty, Freja Sender
 */
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
    val toast = mutableStateOf(false)
    val learningObjectLabel = mutableStateOf("")
    val errors = mutableStateOf<List<ErrorEntryEntity>>(emptyList())

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
        toast.value = false
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
        toast.value = false
    }

    fun onAddLearningObjectClicked() {
        viewModelScope.launch {
            val response = model.addLearningObject(
                learningObjectLabel = learningObjectLabel.value,
                selectedSystem = selectedSystem.value,
                categories = allCategories.value,
                cards = allCards.value
            )

            when (response) {
                is RepoResult.Success -> isFinished.value = true
                is RepoResult.Error -> errors.value = response.errors
                is RepoResult.ServerError -> error.value = "Ein Netzwerkfehler ist aufgetreten."
            }
        }

    }
}