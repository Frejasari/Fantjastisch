package de.fantjastisch.cards_frontend.category

import androidx.compose.runtime.mutableStateOf
import de.fantjastisch.cards_frontend.category.CategoryRepository
import de.fantjastisch.cards_frontend.category.CategorySelectItem
import org.openapitools.client.models.CategoryEntity
import org.openapitools.client.models.UpdateCardEntity
import org.openapitools.client.models.UpdateCategoryEntity
import java.util.*

class UpdateCategoryViewModel(
    id: UUID,
    private val categoryRepository: CategoryRepository = CategoryRepository()
) : CategoryViewModel(
    id = id,
   // cardRepository = cardRepository,
    categoryRepository = categoryRepository
) {

    lateinit var newCat : CategorySelectItem
    init {
        categoryRepository.getCategory(id = id,
            onSuccess = {
                errors.value = emptyList()
                catId.value = it.id
                catLabel.value = it.label
                if (subcategories.value?.isEmpty() == true) {
                    subcategories.value = it.subCategories.map { cat ->
                        categoryRepository.getCategory(cat,
                        onSuccess = {newCat = CategorySelectItem(label = it.label, id = cat, isChecked = false)},
                        onFailure = {})
                        newCat
                    }
                } else {
                    subcategories.value = subcategories.value
                        ?.filter {
                        category ->
                            category.label != it.label
                        }?.map { category ->
                        if (it.subCategories.firstOrNull { cat -> category.id == cat } != null) {
                            CategorySelectItem(
                                label = category.label,
                                id = category.id,
                                isChecked = true
                            )
                        } else {
                            category
                        }
                    }
                }
            },
            onFailure = {
                error.value = "Check network connection"
            })
    }

    override fun save() {
        errors.value = emptyList()
        categoryRepository.updateCategory(
            category = UpdateCategoryEntity(
                id = catId.value!!,
                label = catLabel.value,
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


