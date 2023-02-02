package de.fantjastisch.cards_frontend.card.create

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.card.CardSelectItem
import de.fantjastisch.cards_frontend.card.update_and_create.CreateAndUpdateViewModel
import de.fantjastisch.cards_frontend.category.CategorySelectItem
import de.fantjastisch.cards_frontend.infrastructure.ErrorsEnum
import de.fantjastisch.cards_frontend.infrastructure.RepoResult
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

    // states, die vom view gelesen werden können -> automatisches Update vom View.
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
     * Setzt die Frage der Karte auf den übergebenen Wert.
     *
     * @param value Neue Frage der Karte.
     */
    fun setCardQuestion(value: String) {
        cardQuestion.value = value
    }

    /**
     * Setzt die Antwort der Karte auf den übergebenen Wert.
     *
     * @param value Neue Antwort der Karte.
     */
    fun setCardAnswer(value: String) {
        cardAnswer.value = value
    }

    /**
     * Setzt das Schlagwort der Karte auf den übergebenen Wert.
     *
     * @param value Neues Schlagwort der Karte.
     */
    fun setCardTag(value: String) {
        cardTag.value = value
    }

    /**
     * Weist der Karte die übergebene Kategorie zu.
     *
     * @param id Id der Kategorie, welche ausgewählt wurde.
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
     * Wenn Karte gespeichert wird -> [CreateCardModel] erstellt die Karte und sendet eine
     * Anfrage an die Datenbank.
     */
    fun onCreateCardClicked() {
        errors.value = emptyList()

        // check for categories -> if no then wait till yes
        if (!cardCategories.value.none { cat -> cat.isChecked }) {
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
                    is RepoResult.Error -> errors.value = result.errors
                    is RepoResult.ServerError -> error.value = ErrorsEnum.NETWORK
                }
            }
        }
    }
}