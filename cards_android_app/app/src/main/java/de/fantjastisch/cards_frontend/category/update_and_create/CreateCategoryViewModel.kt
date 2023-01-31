package de.fantjastisch.cards_frontend.category.update_and_create

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.category.CategoryRepository
import de.fantjastisch.cards_frontend.category.CategorySelectItem
import de.fantjastisch.cards_frontend.category.CategoryViewModel
import de.fantjastisch.cards_frontend.infrastructure.fold
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.openapitools.client.models.CreateCategoryEntity

class CreateCategoryViewModel(
    private val categoryRepository: CategoryRepository = CategoryRepository(),
) : CategoryViewModel() {

    val category = mutableStateOf(listOf<CategorySelectItem>())
    val categories = mutableStateOf(listOf<CategorySelectItem>())

    init {
        viewModelScope.launch {
            categoryRepository.getPage().fold(
                onSuccess = {
                    errors.value = emptyList()

                    subcategories.value = it.map { category ->
                        CategorySelectItem(
                            id = category.id,
                            label = category.label,
                            isChecked = false,
                        )
                    }

                },
                onValidationError = { error.value = "Fehler bei der Eingabevalidierung." },
                onUnexpectedError = { error.value = "Ein unbekannter Fehler ist aufgetreten." }
            )
        }
    }


    override fun save() {
        error.value = null
        errors.value = emptyList()

        subcategories.value
            .filter { it.isChecked }
            .let {
                CreateCategoryEntity(
                    label = categoryLabel.value,
                    subCategories = it
                        .map { it.id },
                )
            }
            .let {
                viewModelScope.launch {
                    categoryRepository.createCategory(
                        category = it
                    ).fold(
                        onSuccess = {
                            isFinished.value = true
                        },
                        onValidationError = { error.value = "Fehler bei der Eingabevalidierung." },
                        onUnexpectedError = {
                            error.value = "Ein unbekannter Fehler ist aufgetreten."
                        }
                    )
                }
            }
    }
}
