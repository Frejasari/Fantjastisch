package de.fantjastisch.cards_frontend.category

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import org.openapitools.client.models.ErrorEntryEntity
import java.util.*

abstract class CategoryViewModel(
) : ViewModel() {

    // states, die vom view gelesen werden können -> automatisches Update vom View.
    val catId = mutableStateOf<UUID?>(null)
    val errors = mutableStateOf<List<ErrorEntryEntity>>(emptyList())
    val error = mutableStateOf<String?>(null)
    val isFinished = mutableStateOf(false)

    val categoryLabel = mutableStateOf("")
    val subcategories = mutableStateOf<List<CategorySelectItem>>(listOf())

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