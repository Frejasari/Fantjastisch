package de.fantjastisch.cards_frontend.card

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import de.fantjastisch.cards_frontend.category.CategoryRepository
import de.fantjastisch.cards_frontend.category.CategorySelectItem
import org.openapitools.client.models.ErrorEntryEntity
import java.util.*

abstract class CardViewModel(
    private val id: UUID? = null,
    private val cardRepository: CardRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

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

    fun onCategorySelected(id: UUID) {
        cardCategories.value = cardCategories.value.map {
            if (it.id == id) {
                it.copy(isChecked = !it.isChecked)
            } else {
                it
            }
        }
    }

    abstract fun save()
}