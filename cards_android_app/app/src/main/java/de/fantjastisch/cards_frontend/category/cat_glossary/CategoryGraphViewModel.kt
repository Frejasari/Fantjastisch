package de.fantjastisch.cards_frontend.category.cat_glossary

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import de.fantjastisch.cards_frontend.category.CategoryRepository
import org.openapitools.client.models.CategoryEntity
import java.util.*

class CategoryGraphViewModel(
    val categoryRepository: CategoryRepository = CategoryRepository()
) : ViewModel() {

    val categories = mutableStateOf<List<CategoryEntity>>(emptyList())
    var category = mutableStateOf<CategoryEntity?>(null)
    var name = mutableStateOf<String?>(null)

    init {
        onPageLoaded()
    }

    fun onPageLoaded() {
        categoryRepository.getPage(
            onSuccess = {
                categories.value = it
            },
            onFailure = {}
        )
    }
}