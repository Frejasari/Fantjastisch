package de.fantjastisch.cards_frontend.card.update

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
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
                        categories.value = card.allCategories.map { cat ->
                            CategorySelectItem(
                                id = cat.id,
                                label = cat.label,
                                isChecked = card.categoriesOfCard.firstOrNull { categoryOfCard -> categoryOfCard.id == cat.id } != null
                            )
                        }
                        cardLinks.value = card.links as ArrayList<LinkEntity>
                        cards.value = card.cards
                    },
                    onValidationError = ::setValidationErrors,
                    onUnexpectedError = ::setUnexpectedErrors,
                )
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
                categories = categories.value,
                links = cardLinks.value
            ).fold(
                onSuccess = { isFinished.value = true },
                onValidationError = ::setValidationErrors,
                onUnexpectedError = ::setUnexpectedErrors,
            )
        }
    }

}
