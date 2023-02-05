package de.fantjastisch.cards_frontend.card.create

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.card.update_and_create.CreateAndUpdateViewModel
import de.fantjastisch.cards_frontend.util.ErrorsEnum
import de.fantjastisch.cards_frontend.util.fold
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
    val cardId = mutableStateOf<UUID?>(null)
    val isFinished = mutableStateOf(false)

    init {
        viewModelScope.launch {
            createCardModel.initializePage()
                .fold(
                    onSuccess = {
                        cards.value = it.cards
                        categories.value = it.allCategories
                    }
                )
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
            createCardModel.createCard(
                question = cardQuestion.value,
                answer = cardAnswer.value,
                tag = cardTag.value.replaceFirstChar { letter -> letter.uppercaseChar() },
                links = cardLinks.value,
                categories = categories.value
            ).fold(
                onSuccess = { isFinished.value = true },
                onValidationError = { errors ->
                    if (errors.map { it.field }.contains("categories")) {
                        error.value = ErrorsEnum.CARD_MUST_HAVE_CATEGORY_ERROR
                    } else {
                        setValidationErrors(errors)
                    }
                }
            )
        }
    }
}