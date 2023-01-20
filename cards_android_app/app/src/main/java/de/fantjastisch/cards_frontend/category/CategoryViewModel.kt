package de.fantjastisch.cards_frontend.category

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import de.fantjastisch.cards_frontend.category.CategoryRepository
import de.fantjastisch.cards_frontend.category.CategorySelectItem
import org.openapitools.client.models.ErrorEntryEntity
import java.util.*

abstract class CategoryViewModel(
    private val id: UUID? = null,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    // states, die vom view gelesen werden können -> automatisches Update vom View.
    val catId = mutableStateOf<UUID?>(null)
    val errors = mutableStateOf<List<ErrorEntryEntity>>(emptyList())
    val error = mutableStateOf<String?>(null)
    val isFinished = mutableStateOf(false)

    val catLabel = mutableStateOf("")
    val subcategories = mutableStateOf<List<CategorySelectItem>>(listOf())

    init {
        categoryRepository.getPage(
            onSuccess = {
                errors.value = emptyList()

                if (subcategories.value?.isEmpty() == true) {
                    subcategories.value = it.map { subcategory ->
                        CategorySelectItem(
                            id = subcategory.id,
                            label = subcategory.label,
                            isChecked = false,
                        )
                    }
                } else {
                    val subCatsOfCat = subcategories.value
                    val newCategories = it.map { category ->
                        CategorySelectItem(
                            id = category.id,
                            label = category.label,
                            isChecked = subCatsOfCat
                                ?.firstOrNull() { cat -> cat.id == category.id } != null,
                        )
                    }
                    subcategories.value = newCategories
                }

            },
            onFailure = {
                error.value = "Check network connection"
            },
        )
    }

    fun onCategorySelected(id: UUID) {
        subcategories.value = subcategories.value.map {
            if (it.id == id) {
                it.copy(isChecked = !it.isChecked)
            } else {
                it
            }
        }
    }

    abstract fun save()
}