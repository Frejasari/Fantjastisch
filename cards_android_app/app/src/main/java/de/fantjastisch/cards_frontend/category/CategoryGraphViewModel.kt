package de.fantjastisch.cards_frontend.category

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import de.fantjastisch.cards_frontend.category.CategoryRepository
import org.openapitools.client.models.CategoryEntity

class CategoryGraphViewModel(
    private val categoryRepository: CategoryRepository = CategoryRepository()
) : ViewModel() {

    val categories = mutableStateOf<List<CategoryEntity>>(emptyList())

    fun onPageLoaded() {
        categoryRepository.getPage(
            onSuccess = { categories.value = it },
            onFailure = {}
        )
    }

    init {
        onPageLoaded()
    }
}