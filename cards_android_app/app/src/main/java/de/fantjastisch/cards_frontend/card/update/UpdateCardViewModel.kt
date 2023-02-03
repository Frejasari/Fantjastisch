package de.fantjastisch.cards_frontend.card.update

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.card.create.CreateCardModel
import de.fantjastisch.cards_frontend.card.update_and_create.CreateAndUpdateViewModel
import de.fantjastisch.cards_frontend.category.CategorySelectItem
import de.fantjastisch.cards_frontend.infrastructure.fold
import de.fantjastisch.cards_frontend.util.ErrorsEnum
import kotlinx.coroutines.launch
import org.openapitools.client.models.LinkEntity
import java.util.*

/**
 * Stellt die Daten für die [UpdateCardView] bereit und nimmt seine Anfragen entgegen.
 *
 * @author Freja Sender, Tamari Bayer, Jessica Repty
 *
 * @property cardModel Das zugehörige Model, welches die Logik kapselt.
 * @param id Id, der zu bearbeitende Karte
 */
class UpdateCardViewModel(
    id: UUID,
    private val cardModel: UpdateCardModel = UpdateCardModel(id = id),
) : CreateAndUpdateViewModel() {

    val isFinished = mutableStateOf(false)

    val cardQuestion = mutableStateOf("")
    val cardAnswer = mutableStateOf("")
    val cardTag = mutableStateOf("")
    val cardCategories = mutableStateOf(listOf<CategorySelectItem>())

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

    init {
        viewModelScope.launch {
            cardModel
                .initializePage()
                .fold(
                    onSuccess = { card ->
                        errors.value = emptyList()
                        cardAnswer.value = card.answer
                        cardQuestion.value = card.question
                        cardTag.value = card.tag
                        cardCategories.value = card.allCategories.map { cat ->
                            CategorySelectItem(
                                id = cat.id,
                                label = cat.label,
                                isChecked = card.categoriesOfCard.firstOrNull { categoryOfCard -> categoryOfCard.id == cat.id } != null
                            )
                        }
                        cardLinks.value = card.links as ArrayList<LinkEntity>
                    },
                    onValidationError = { errorResult ->
                        errors.value = errorResult
                    },
                    onUnexpectedError = { error.value = ErrorsEnum.NETWORK },
                )

            val resultCards = cardModel.getCards()

            if (resultCards == null) {
                error.value = ErrorsEnum.NETWORK
            } else {
                errors.value = emptyList()
                cards.value = resultCards
            }
        }
    }

    /**
     * Wird aufgerufen, wenn auf den Speichern-Button geklickt wurde.
     * -> [UpdateCardModel] updated die Karte und sendet eine
     * Anfrage an die Datenbank.
     *
     */
    fun onUpdateCardClicked() {
        errors.value = emptyList()

        viewModelScope.launch {
            cardModel.update(
                question = cardQuestion.value,
                answer = cardAnswer.value,
                tag = cardTag.value,
                categories = cardCategories.value,
                links = cardLinks.value
            ).fold(
                onSuccess = { isFinished.value = true },
                onValidationError = { errors.value = it },
                onUnexpectedError = { error.value = ErrorsEnum.UNEXPECTED }
            )
        }
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

}
