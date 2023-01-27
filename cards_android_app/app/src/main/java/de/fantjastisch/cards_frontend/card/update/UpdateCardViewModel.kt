package de.fantjastisch.cards_frontend.card.update

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.category.CategorySelectItem
import de.fantjastisch.cards_frontend.infrastructure.fold
import kotlinx.coroutines.launch
import org.openapitools.client.models.ErrorEntryEntity
import java.util.*

class UpdateCardViewModel(
    id: UUID,
    private val cardModel: UpdateCardModel = UpdateCardModel(id = id)
) : ViewModel() {

    val errors = mutableStateOf<List<ErrorEntryEntity>>(emptyList())
    val error = mutableStateOf<String?>(null)
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
                    },
                    onValidationError = { error.value = "Fehler bei der Eingabevalidierung." },
                    onUnexpectedError = { error.value = "Ein unbekannter Fehler ist aufgetreten." },
                )
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
                links = emptyMap()
            ).fold(
                onSuccess = { isFinished.value = true },
                onValidationError = { errors.value = it },
                onUnexpectedError = { error.value = "Ein unbekannter Fehler ist aufgetreten." }
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

