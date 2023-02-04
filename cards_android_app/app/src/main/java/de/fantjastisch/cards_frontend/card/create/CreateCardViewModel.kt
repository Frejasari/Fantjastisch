package de.fantjastisch.cards_frontend.card.create

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.card.update_and_create.CreateAndUpdateViewModel
import de.fantjastisch.cards_frontend.util.ErrorsEnum
import de.fantjastisch.cards_frontend.util.RepoResult
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

            when (val result = createCardModel.initializePage()) {
                is RepoResult.Success -> {
                    cards.value = result.result.cards
                    categories.value = result.result.allCategories
                }

                is RepoResult.Error -> setValidationErrors(result.errors)
                is RepoResult.ServerError -> setUnexpectedError()
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
                categories = categories.value
            )

            when (result) {
                is RepoResult.Success -> isFinished.value = true
                is RepoResult.Error -> {
                    errors.value = result.errors
                    if (result.errors.map { it.field }.contains("categories")) {
                        error.value = ErrorsEnum.CARD_MUST_HAVE_CATEGORY_ERROR
                    } else {
                        setValidationErrors(result.errors)
                    }
                }
                is RepoResult.ServerError -> setUnexpectedError()
            }
        }
    }
}