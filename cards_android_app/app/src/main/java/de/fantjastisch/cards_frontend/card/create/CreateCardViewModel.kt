package de.fantjastisch.cards_frontend.card.create

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.card.CardSelectItem
import de.fantjastisch.cards_frontend.card.update_and_create.CreateAndUpdateViewModel
import de.fantjastisch.cards_frontend.category.CategorySelectItem

import de.fantjastisch.cards_frontend.infrastructure.RepoResult
import de.fantjastisch.cards_frontend.util.ErrorsEnum
import kotlinx.coroutines.launch
import java.util.*

/**
 * Stellt die Daten für die [CreateCardView] bereit und nimmt seine Anfragen entgegen.
 *
 * @property createCardModel Das zugehörige Model, welches die Logik kapselt.
 *
 * @author Tamari Bayer, Freja Sender, Jessica Repty, Semjon Nirmann
 */
class CreateCardViewModel(
    private val createCardModel: CreateCardModel = CreateCardModel()
) : CreateAndUpdateViewModel() {

    // states, die vom View gelesen werden können -> automatisches Update vom View.
    val card = mutableStateOf(listOf<CardSelectItem>())
    val cardId = mutableStateOf<UUID?>(null)
    val isFinished = mutableStateOf(false)

    val cardQuestion = mutableStateOf("")
    val cardAnswer = mutableStateOf("")
    val cardTag = mutableStateOf("")
    val cardCategories = mutableStateOf(listOf<CategorySelectItem>())

    init {
        viewModelScope.launch {
            val result = createCardModel.getCategories()
            val resultCards = createCardModel.getCards()

            if (result == null || resultCards == null) {
                error.value = ErrorsEnum.NETWORK
            } else {
                errors.value = emptyList()
                cardCategories.value = result
                cards.value = resultCards
            }
        }
    }

    /**
     * Speichert die übergebene Frage der Karte in [cardQuestion].
     *
     * @param value Neue Frage der Karte.
     */
    fun setCardQuestion(value: String) {
        cardQuestion.value = value
    }

    /**
     * Speichert die übergebene Antwort der Karte in [cardAnswer].
     *
     * @param value Neue Antwort der Karte.
     */
    fun setCardAnswer(value: String) {
        cardAnswer.value = value
    }

    /**
     * Speichert das eingegebene Schlagwort der Karte in [cardTag].
     *
     * @param value Neues Schlagwort der Karte.
     */
    fun setCardTag(value: String) {
        cardTag.value = value
    }

    /**
     * Speichert die ausgewählten Kategorien als isChecked = true in [cardCategories].
     *
     * @param id Id der Kategorie, welche neu ausgewählt wurde.
     */
    fun onCategorySelected(id: UUID) {
        cardCategories.value = cardCategories.value.map {
            if (it.id == id) {
                it.copy(isChecked = !it.isChecked)
            } else {
                it
            }
        }
    }

    /**
     * Wenn Karte gespeichert wird -> [CreateCardModel] erstellt die Karte mit den in den Variablen
     * gespeicherten Daten und sendet eine Anfrage an die Datenbank.
     *
     */
    fun onCreateCardClicked() {
        errors.value = emptyList()

        // check for categories -> if no then wait till yes
        viewModelScope.launch {
            val result = createCardModel.createCard(
                question = cardQuestion.value,
                answer = cardAnswer.value,
                tag = cardTag.value.replaceFirstChar { letter -> letter.uppercaseChar() },
                links = cardLinks.value,
                categories = cardCategories.value
            )

            when (result) {
                is RepoResult.Success -> isFinished.value = true
                is RepoResult.Error -> {
                    errors.value = result.errors
                    if (result.errors.map { it.field }.contains("categories")) {
                        error.value = ErrorsEnum.CATEGORY_NOT_EMPTY_ERROR
                    } else {
                        error.value = ErrorsEnum.CHECK_INPUT
                    }
                }
                is RepoResult.ServerError -> error.value = ErrorsEnum.NETWORK
            }
        }
    }
}