package de.fantjastisch.cards_frontend.category

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import de.fantjastisch.cards_frontend.category.CategoryRepository
import org.openapitools.client.models.CategoryEntity
import java.util.*
import kotlin.collections.ArrayList

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
                        }*/},
            onFailure = {}
        )
    }

    fun getNames(cat:CategoryEntity) : ArrayList<String>{
        val result = ArrayList<String>()
        cat.subCategories.forEach{
            categoryRepository.getCategory(id = it,
                onSuccess = {
                    result.add(it.label)
                },
                onFailure = {category.value = null})
        }
        return result
    }

    fun onCategoryLoaded(id: UUID) {
        cat = ArrayList<CategoryEntity>()
        categoryRepository.getCategory(id = id,
        onSuccess = {
            cat.add(it)
        },
        onFailure = {cat})
    }


}