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
 * @property cardModel Das zugehörige Model, welches die Logik kapselt.
 * @param id Id, der zu bearbeitende Karte
 *
 * @author Freja Sender, Tamari Bayer, Jessica Repty
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

    fun setCardQuestion(value: String) {
        cardQuestion.value = value
    }

    fun setCardAnswer(value: String) {
        cardAnswer.value = value
    }

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
