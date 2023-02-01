package de.fantjastisch.cards_frontend.category.create

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.card.CardSelectItem
import de.fantjastisch.cards_frontend.category.CategorySelectItem
import de.fantjastisch.cards_frontend.infrastructure.RepoResult
import kotlinx.coroutines.launch
import org.openapitools.client.models.ErrorEntryEntity
import org.openapitools.client.models.LinkEntity
import java.util.*

class CreateCategoryViewModel(
    private val createCategoryModel: CreateCategoryModel = CreateCategoryModel()
) : ViewModel() {

    // states, die vom View gelesen werden können -> automatisches Update vom View.

    // states, die vom view gelesen werden können -> automatisches Update vom View.
    val errors = mutableStateOf<List<ErrorEntryEntity>>(emptyList())
    val error = mutableStateOf<String?>(null)
    val isFinished = mutableStateOf(false)

    val label = mutableStateOf("")
    val allCats = mutableStateOf(listOf<CategorySelectItem>())


    init {
        viewModelScope.launch {

            val result = createCategoryModel.getCategories()

            if (result == null) {
                error.value = "Ein Netzwerkfehler ist aufgetreten."
            } else {
                errors.value = emptyList()
                allCats.value = result
            }
        }
    }

    fun setLabel(value: String) {
        label.value = value
    }

    fun onCategorySelected(id: UUID) {
        allCats.value = allCats.value.map {
            if (it.id == id) {
                it.copy(isChecked = !it.isChecked)
            } else {
                it
            }
        }
    }



    fun onCreateCategoryClicked() {
        error.value = null
        errors.value = emptyList()

            viewModelScope.launch {
                val result = createCategoryModel.createCategory(
                    label = label.value,
                    subCategories = allCats.value,
                )

                when (result) {
                    is RepoResult.Success -> isFinished.value = true
                    is RepoResult.Error -> errors.value = result.errors
                    is RepoResult.ServerError -> error.value = "Ein Netzwerkfehler ist aufgetreten."
                }
            }



    }


}