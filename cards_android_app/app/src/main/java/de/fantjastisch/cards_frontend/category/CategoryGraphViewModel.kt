package de.fantjastisch.cards_frontend.category

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import org.openapitools.client.models.CategoryEntity
import java.util.*

class CategoryGraphViewModel(
    val categoryRepository: CategoryRepository = CategoryRepository()
) : ViewModel() {

    val categories = mutableStateOf<List<CategoryEntity>>(emptyList())
    var category = mutableStateOf<CategoryEntity?>(null)
    var cat = ArrayList<CategoryEntity>()
    var name = mutableStateOf<String?>(null)
    var subCatsWithName = ArrayList<String>()

    init {
        onPageLoaded()
    }

    fun onPageLoaded() {
        categoryRepository.getPage(
            onSuccess = {
                categories.value = it
                /* it.forEach{
                     subCatsWithName = getNames(it)
                 }*/
            },
            onFailure = {}
        )
    }

    fun getNames(cat: CategoryEntity): ArrayList<String> {
        val result = ArrayList<String>()
        cat.subCategories.forEach {
            categoryRepository.getCategory(id = it,
                onSuccess = {
                    result.add(it.label)
                },
                onFailure = { category.value = null })
        }
        return result
    }

    fun onCategoryLoaded(id: UUID) {
        cat = ArrayList<CategoryEntity>()
        categoryRepository.getCategory(id = id,
            onSuccess = {
                cat.add(it)
            },
            onFailure = { cat })
    }


}