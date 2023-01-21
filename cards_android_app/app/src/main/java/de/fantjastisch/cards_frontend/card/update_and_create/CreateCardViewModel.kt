package de.fantjastisch.cards_frontend.card.update_and_create

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import de.fantjastisch.cards_frontend.card.CardRepository
import de.fantjastisch.cards_frontend.card.CardSelectItem
import de.fantjastisch.cards_frontend.category.CategoryRepository
import de.fantjastisch.cards_frontend.category.CategorySelectItem
import org.openapitools.client.models.CreateCardEntity
import org.openapitools.client.models.ErrorEntryEntity
import java.util.*

class CreateCardViewModel(
    private val cardRepository: CardRepository = CardRepository(),
    categoryRepository: CategoryRepository = CategoryRepository()
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

    init {
        categoryRepository.getPage(
            onSuccess = {
                errors.value = emptyList()

                if (cardCategories.value.isEmpty()) {
                    cardCategories.value = it.map { category ->
                        CategorySelectItem(
                            id = category.id,
                            label = category.label,
                            isChecked = false,
                        )
                    }
                } else {
                    val categoriesOfCard = cardCategories.value
                    val newCategories = it.map { category ->
                        CategorySelectItem(
                            id = category.id,
                            label = category.label,
                            isChecked = categoriesOfCard
                                .firstOrNull() { cat -> cat.id == category.id } != null,
                        )
                    }
                    cardCategories.value = newCategories
                }

            },
            onFailure = {
                error.value = "Check network connection"
            },
        )
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

        cardRepository.createCard(
            card = CreateCardEntity(
                question = cardQuestion.value,
                answer = cardAnswer.value,
                tag = cardTag.value,
                categories = cardCategories.value.filter { it.isChecked }
                    .map { it.id }
            ),
            onSuccess = {
                isFinished.value = true
            },
            onFailure = {
                if (it == null) {
                    // Fehler anzeigen:
                    error.value = "Irgendwas ist schief gelaufen"
                } else {
                    errors.value = it.errors
                }
            }
        )
    }
}