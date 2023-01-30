package de.fantjastisch.cards_frontend.card.create

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.card.CardSelectItem
import de.fantjastisch.cards_frontend.category.CategorySelectItem
import de.fantjastisch.cards_frontend.infrastructure.RepoResult
import kotlinx.coroutines.launch
import org.openapitools.client.models.CardEntity
import org.openapitools.client.models.ErrorEntryEntity
import org.openapitools.client.models.LinkEntity
import java.util.*

class CreateCardViewModel(
    private val createCardModel: CreateCardModel = CreateCardModel()
) : ViewModel() {

    // states, die vom View gelesen werden können -> automatisches Update vom View.
    val card = mutableStateOf(listOf<CardSelectItem>())

    // states, die vom view gelesen werden können -> automatisches Update vom View.
    val cardId = mutableStateOf<UUID?>(null)
    val errors = mutableStateOf<List<ErrorEntryEntity>>(emptyList())
    val error = mutableStateOf<String?>(null)
    val isFinished = mutableStateOf(false)

    val cardQuestion = mutableStateOf("")
    val cardAnswer = mutableStateOf("")
    val cardTag = mutableStateOf("")
    val cardCategories = mutableStateOf(listOf<CategorySelectItem>())
    val cards = mutableStateOf(listOf<CardSelectItem>())
    val linkName = mutableStateOf("")
    val linkTarget = mutableStateOf<UUID?>(null)

    init {
        viewModelScope.launch {
            val result = createCardModel.getCategories()
            val resultCards = createCardModel.getCards()

            if (result == null || resultCards == null) {
                error.value = "Ein Netzwerkfehler ist aufgetreten."
            } else {
                errors.value = emptyList()
                cardCategories.value = result
                cards.value = resultCards
            }
        }
    }

    fun setCardQuestion(value: String) {
        cardQuestion.value = value
    }

    fun setCardAnswer(value: String) {
        cardAnswer.value = value
    }

    fun setCardTag(value: String) {
        cardTag.value = value
    }

    fun setLinkName(value: String) {
        linkName.value = value
    }

    fun onCardSelected(id: UUID) {
        cards.value = cards.value.map {
            if (it.card.id == id) {
                it.copy(isChecked = !it.isChecked)
            } else {
                it
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

    fun onCreateCardClicked() {
        error.value = null
        errors.value = emptyList()

        viewModelScope.launch {
            val result = createCardModel.createCard(
                question = cardQuestion.value,
                answer = cardAnswer.value,
                tag = cardTag.value.replaceFirstChar { letter -> letter.uppercaseChar() },
                links = listOf( LinkEntity(
                    label = linkName.value,
                    target = cards.value[0].card.id
                )),
                categories = cardCategories.value
            )

            when (result) {
                is RepoResult.Success -> isFinished.value = true
                is RepoResult.Error -> errors.value = result.errors
                is RepoResult.ServerError -> error.value = "Ein Netzwerkfehler ist aufgetreten."
            }
        }
    }
}