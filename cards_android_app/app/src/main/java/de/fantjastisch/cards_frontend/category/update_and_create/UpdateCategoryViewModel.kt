package de.fantjastisch.cards_frontend.category.update_and_create

import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.category.CategoryRepository
import de.fantjastisch.cards_frontend.category.CategorySelectItem
import de.fantjastisch.cards_frontend.category.CategoryViewModel
import de.fantjastisch.cards_frontend.infrastructure.fold
import kotlinx.coroutines.launch
import org.openapitools.client.models.UpdateCategoryEntity
import java.util.*

class UpdateCategoryViewModel(
    val id: UUID,
    private val categoryRepository: CategoryRepository = CategoryRepository()
) : CategoryViewModel() {


    private lateinit var subCategoriesOfCategory: List<UUID>

    init {
        viewModelScope.launch {
            categoryRepository.getCategory(id = id).fold(
                onSuccess = {
                    errors.value = emptyList()
                    catId.value = it.id
                    categoryLabel.value = it.label
                    subCategoriesOfCategory = it.subCategories
                },
                onValidationError = { error.value = "Fehler bei der Eingabevalidierung." },
                onUnexpectedError = { error.value = "Ein unbekannter Fehler ist aufgetreten." })
            categoryRepository.getPage().fold(
                onSuccess = {
                    errors.value = emptyList()
                    val newCategories = it
                        .filter { category -> id != category.id }
                        .map { category ->
                            CategorySelectItem(
                                id = category.id,
                                label = category.label,
                                isChecked = subCategoriesOfCategory.contains(category.id),
                            )
                        }
                    subcategories.value = newCategories
                },
                onValidationError = { error.value = "Fehler bei der Eingabevalidierung." },
                onUnexpectedError = { error.value = "Ein unbekannter Fehler ist aufgetreten." })
        }
    }

    override fun save() {
        errors.value = emptyList()
        viewModelScope.launch {
            categoryRepository.updateCategory(
                category = UpdateCategoryEntity(
                    id = catId.value!!,
                    label = categoryLabel.value,
                    subCategories = subcategories.value.filter { it.isChecked }.map { it.id }
                )).fold(
                onSuccess = {
                    isFinished.value = true
                },
                onValidationError = { error.value = "Fehler bei der Eingabevalidierung." },
                onUnexpectedError = { error.value = "Ein unbekannter Fehler ist aufgetreten." }
            )
        }
    }
}


