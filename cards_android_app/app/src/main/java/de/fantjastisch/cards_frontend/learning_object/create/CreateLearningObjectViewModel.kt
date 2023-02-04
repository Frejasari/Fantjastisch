package de.fantjastisch.cards_frontend.learning_object.create

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.card.CardSelectItem
import de.fantjastisch.cards_frontend.card.update_and_create.ErrorHandlingViewModel
import de.fantjastisch.cards_frontend.category.CategorySelectItem
import de.fantjastisch.cards_frontend.components.SingleSelectItem
import de.fantjastisch.cards_frontend.util.ErrorsEnum
import de.fantjastisch.cards_frontend.util.RepoResult
import de.fantjastisch.cards_frontend.util.fold
import kotlinx.coroutines.launch
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
) : ErrorHandlingViewModel() {

    // states, die vom view gelesen werden können -> automatisches Update vom View.
    val allCategories = mutableStateOf(listOf<CategorySelectItem>())
    val allCards = mutableStateOf(listOf<CardSelectItem>())
    val learningSystems = mutableStateOf(listOf<SingleSelectItem>())
    val selectedSystem = mutableStateOf<SingleSelectItem?>(null)
    val isFinished = mutableStateOf(false)
    val learningObjectLabel = mutableStateOf("")

    // constructor (wird ganz am Anfang aufgerufen)
    init {
        viewModelScope.launch {
            model
                .initializePage()
                .fold(
                    onSuccess = { learningObject ->
                        allCategories.value = learningObject.categorySelectItems
                        allCards.value = learningObject.cardSelectItems
                        learningSystems.value = learningObject.learningSystems
                    },
                    onValidationError = ::setValidationErrors,
                    onUnexpectedError = ::setUnexpectedError,
                )
        }
    }

    /**
     * Speichert die übergebene Bezeichnung des Lernobjekts in [learningObjectLabel].
     *
     * @param value Neue Bezeichnung des Lernobjekts.
     */
    fun setLearningObjectLabel(value: String) {
        learningObjectLabel.value = value
    }

    /**
     * Speichert die ausgewählten Karten als isChecked = true in [allCards].
     *
     * @param id Id der Karte, welche ausgewählt wurde.
     */
    fun onCardSelected(id: UUID) {
        allCards.value = allCards.value.map {
            if (it.card.id == id) {
                it.copy(isChecked = !it.isChecked)
            } else {
                it
            }
        }
    }

    /**
     * Speichert die ausgewählten Kategorien als isChecked = true in [allCategories].
     *
     * @param id Id der Kategorie, welche ausgewählt wurde.
     */
    fun onCategorySelected(id: UUID) {
        allCategories.value = allCategories.value.map {
            if (it.id == id) {
                it.copy(isChecked = !it.isChecked)
            } else {
                it
            }
        }
    }

    /**
     * Speichert das ausgewählte Lernsystem als isChecked = true in [selectedSystem].
     *
     * @param id Id des Lernsystems, welches ausgewählt wurde.
     */
    fun onLearningSystemSelected(id: UUID) {
        selectedSystem.value = learningSystems.value.first { it.id == id }
    }

    /**
     * Wenn Lernobjekt gespeichert wird-> [CreateLearningObjectModel] erstellt ein Lernobjekt mit den
     * in den Variablen gespeicherten Daten und sendet eine Anfrage an die Datenbank.
     *
     */
    fun onAddLearningObjectClicked() {
        viewModelScope.launch {
            val response = model.addLearningObject(
                learningObjectLabel = learningObjectLabel.value.trim(),
                selectedSystem = selectedSystem.value,
                categories = allCategories.value,
                cards = allCards.value
            )

            when (response) {
                is RepoResult.Success -> isFinished.value = true
                is RepoResult.Error -> errors.value = response.errors
                is RepoResult.ServerError -> error.value = ErrorsEnum.NETWORK
            }
        }

    }
}