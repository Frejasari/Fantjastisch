package de.fantjastisch.cards_frontend.category.update_and_create

import de.fantjastisch.cards_frontend.category.CategoryRepository
import de.fantjastisch.cards_frontend.category.CategorySelectItem
import de.fantjastisch.cards_frontend.category.CategoryViewModel
import org.openapitools.client.models.UpdateCategoryEntity
import java.util.*

class UpdateCategoryViewModel(
    val id: UUID,
    private val categoryRepository: CategoryRepository = CategoryRepository()
) : CategoryViewModel() {


    private lateinit var subCategoriesOfCategory: List<UUID>

    init {
        categoryRepository.getCategory(id = id,
            onSuccess = {
                errors.value = emptyList()
                catId.value = it.id
                categoryLabel.value = it.label
                subCategoriesOfCategory = it.subCategories
                loadAllCategories()
            },
            onFailure = {
                error.value = "Check network connection"
            })
    }

    private fun loadAllCategories() {
        categoryRepository.getPage(
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
            onFailure = {
                error.value = "Check network connection"
            },
        )
    }

    override fun save() {
        errors.value = emptyList()
        categoryRepository.updateCategory(
            category = UpdateCategoryEntity(
                id = catId.value!!,
                label = categoryLabel.value,
                subCategories = subcategories.value?.filter { it.isChecked }!!.map { it.id }
            ),
            onSuccess = {
                isFinished.value = true
            },
            onFailure = {
                if (it == null) {
                    error.value = "Irgendwas ist schief gelaufen"
                } else {
                    errors.value = it.errors
                }
                error.value = "There is an error"
            }
        )
    }
}


