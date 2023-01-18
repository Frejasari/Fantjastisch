package de.fantjastisch.cards_frontend.category

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import de.fantjastisch.cards_frontend.card.CardSelectItem
import de.fantjastisch.cards_frontend.card.CardViewModel
import org.openapitools.client.models.CreateCardEntity
import org.openapitools.client.models.CreateCategoryEntity
import org.openapitools.client.models.ErrorEntryEntity
import java.util.*

class CreateCategoryViewModel(
    private val categoryRepository: CategoryRepository = CategoryRepository(),
) : CategoryViewModel(
    categoryRepository = categoryRepository) {

    val category = mutableStateOf(listOf<CategorySelectItem>())
    val categories = mutableStateOf(listOf<CategorySelectItem>())
    val categoryLabel = mutableStateOf("")

    override fun save() {
        error.value = null
        errors.value = emptyList()

        subcategories.value?.filter { it.isChecked }?.let {
            CreateCategoryEntity(
                label = catLabel.value,
                subCategories = it
                    .map { it.id },

                )
        }?.let {
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

    fun onAddCategoryClicked() {
        errors.value = emptyList()
        error.value = null
        categoryRepository.createCategory(
            category = CreateCategoryEntity(
                label = categoryLabel.value,
                subCategories = categories.value.filter { it.isChecked }.map { it.id }
            ),
            onSuccess = {
                isFinished.value = true
                // on Success -> dialog schliessen, zur Category  übersicht?
            },
            onFailure = {
                // Fehler anzeigen:
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