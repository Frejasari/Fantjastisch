package de.fantjastisch.cards_frontend.learning_object.create

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.card.CardSelectItem
import de.fantjastisch.cards_frontend.card.update_and_create.ErrorHandlingViewModel
import de.fantjastisch.cards_frontend.category.CategorySelectItem
import de.fantjastisch.cards_frontend.components.SingleSelectItem
import de.fantjastisch.cards_frontend.glossary.CardsFilters
import de.fantjastisch.cards_frontend.util.fold
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*

/**
 * Stellt die Daten für die [CreateLearningObjectView] bereit und nimmt seine Anfragen entgegen.
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

    private var selectedCards = emptyList<UUID>()

    // constructor (wird ganz am Anfang aufgerufen)
    init {
        viewModelScope.launch {
            model
                .initializePage()
                .fold(
                    onSuccess = { learningObject ->
                        allCategories.value = learningObject.categorySelectItems
                        learningSystems.value = learningObject.learningSystems
                    }
                )
        }
        viewModelScope.launch {
            CardsFilters.filters.collectLatest {
                model.loadCards().fold(
                    onSuccess = {
                        val cardSelectItems = it.map { card ->
                            CardSelectItem(
                                card = card,
                                isChecked = selectedCards.contains(card.id)
                            )
                        }
                        allCards.value = cardSelectItems
                    }
                )
            }
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
        selectedCards = if (selectedCards.contains(id)) {
            selectedCards - id
        } else {
            selectedCards + id
        }

        rebuildAllCards()
    }

    private fun rebuildAllCards() {
        allCards.value = allCards.value.map {
            it.copy(isChecked = selectedCards.contains(it.card.id))
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
            model.addLearningObject(
                learningObjectLabel = learningObjectLabel.value.trim(),
                selectedSystem = selectedSystem.value,
                categories = allCategories.value,
                cards = allCards.value
            ).fold(
                onSuccess = { isFinished.value = true }
            )
        }

    }
}