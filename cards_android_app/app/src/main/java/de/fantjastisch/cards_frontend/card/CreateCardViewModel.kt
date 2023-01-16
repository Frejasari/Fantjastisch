package de.fantjastisch.cards_frontend.card

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import de.fantjastisch.cards_frontend.category.CategoryRepository
import de.fantjastisch.cards_frontend.category.CategorySelectItem
import org.openapitools.client.models.CreateCardEntity
import org.openapitools.client.models.ErrorEntryEntity
import java.util.*

class CreateCardViewModel(
    private val cardRepository: CardRepository = CardRepository(),
    private val categoryRepository: CategoryRepository = CategoryRepository()
) : ViewModel() {

    // states, die vom view gelesen werden können -> automatisches Update vom View.
    val card = mutableStateOf(listOf<CardSelectItem>())
    val errors = mutableStateOf<List<ErrorEntryEntity>>(emptyList())
    val error = mutableStateOf<String?>(null)
    val isFinished = mutableStateOf(false)

    val cardQuestion = mutableStateOf("")
    val cardAnswer = mutableStateOf("")
    val cardTag = mutableStateOf("")
    val cardCategories = mutableStateOf(listOf<CategorySelectItem>())

    // constructor (wird ganz am Anfang aufgerufen)
    init {
        categoryRepository.getPage(
            onSuccess = {
                errors.value = emptyList()
                cardCategories.value = it.map { category ->
                    CategorySelectItem(
                        id = category.id,
                        label = category.label,
                        isChecked = false,
                    )
                }
            },
            onFailure = {
                error.value = "Check network connection"
            },
        )
    }

    fun onAddCardClicked() {
        error.value = null
        errors.value = emptyList()

        cardRepository.createCard(
            card = CreateCardEntity(
                question = cardQuestion.value,
                answer = cardAnswer.value,
                tag = cardTag.value,
                categories = cardCategories.value.filter { it.isChecked }
                    .map { it.id }//cardCategories.value,
            ),
            onSuccess = {
                isFinished.value = true
                // on Success -> dialog schliessen, zur Card  übersicht?
            },
            onFailure = {
                if (it == null) {
                    error.value = "Irgendwas ist schief gelaufen"
                } else {
                    errors.value = it.errors
                }
                // Fehler anzeigen:
                error.value = "There is an error"
            }
        )
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