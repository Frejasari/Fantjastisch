package de.fantjastisch.cards_frontend.card.update_and_create

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.category.CategorySelectItem
import de.fantjastisch.cards_frontend.infrastructure.RepoResult
import kotlinx.coroutines.launch
import org.openapitools.client.models.ErrorEntryEntity
import org.openapitools.client.models.UpdateCardEntity
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
            val card = cardModel.initializePage()
            if (card == null) {
                error.value = "Something is wrong"
            } else {

                errors.value = emptyList()
                cardAnswer.value = card.answer
                cardQuestion.value = card.question
                cardTag.value = card.tag
                cardCategories.value = card.categories
            }
        }
    }


    fun onUpdateCardClicked() {
        errors.value = emptyList()
        viewModelScope.launch {
            val updateResult = cardModel.update(
                question = cardQuestion.value,
                answer = cardAnswer.value,
                tag = cardTag.value,
                categories = cardCategories.value,
                links = emptyMap()
            )
            when (updateResult) {
                is RepoResult.Success -> isFinished.value = true
                is RepoResult.Error -> errors.value = updateResult.errors.errors
                is RepoResult.ServerError -> error.value = "Irgendwas ist schief gelaufen"
            }
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

