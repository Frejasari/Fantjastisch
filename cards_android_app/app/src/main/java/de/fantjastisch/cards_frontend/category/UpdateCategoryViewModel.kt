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
                categoryRepository.getPage(
                    onSuccess = {
                        subcategories.value = it.map {
                            cat -> CategorySelectItem(id = cat.id, label = cat.label, isChecked = false)
                        }
                    },
                    onFailure = {}
                )
                if (it.subCategories.isNotEmpty()){
                    val newSub = subcategories.value
                    subcategories.value?.map { cat ->
                        if (cat.id == it.id) {
                            CategorySelectItem(id = cat.id, label = cat.label, isChecked = true)
                        } else {
                            CategorySelectItem(id = cat.id, label = cat.label, isChecked = false)
                        }
                    }
                }
                        },
            onFailure = {error.value = "Check network connection"})
      /*  categoryRepository.getCategory(id = id,
            onSuccess = {
                errors.value = emptyList()
                catId.value = it.id
                catLabel.value = it.label
                //subcategories.value = it.subCategories
                if (subcategories.value?.isEmpty() == true) {
                    subcategories.value = it.subCategories.map { cat ->
                        categoryRepository.getCategory(cat,
                        onSuccess = {newCat = CategorySelectItem(label = it.label, id = cat, isChecked = true)},
                        onFailure = {})
                        newCat
                    }
                } else {
                    subcategories.value = subcategories.value.map { category ->
                        if (it.subCategories.firstOrNull { cat -> category.id == id } != null) {
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
            })*/
    }

    override fun save() {
        errors.value = emptyList()
        subcategories.value?.let {
            UpdateCategoryEntity(
                id = catId.value!!,
                label = catLabel.value,
                subCategories = it.map { cat -> cat.id },
            )
        }?.let {
            categoryRepository.updateCategory(
                category = it,
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
    }

}

