package de.fantjastisch.cards_frontend.category

import androidx.compose.runtime.mutableStateOf
import org.openapitools.client.models.CreateCategoryEntity

class CreateCategoryViewModel(
    private val categoryRepository: CategoryRepository = CategoryRepository(),
) : CategoryViewModel() {

    val category = mutableStateOf(listOf<CategorySelectItem>())
    val categories = mutableStateOf(listOf<CategorySelectItem>())

    init {
        categoryRepository.getPage(
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
            onFailure = {
                error.value = "Check network connection"
            },
        )
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
                categoryRepository.createCategory(
                    category = it,
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
}