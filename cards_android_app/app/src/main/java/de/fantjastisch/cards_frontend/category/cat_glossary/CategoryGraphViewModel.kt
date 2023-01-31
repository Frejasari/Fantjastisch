package de.fantjastisch.cards_frontend.category.cat_glossary

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import de.fantjastisch.cards_frontend.category.CategoryRepository
import de.fantjastisch.cards_frontend.infrastructure.fold
import kotlinx.coroutines.launch
import org.openapitools.client.models.CategoryEntity
import java.util.*

class CategoryGraphViewModel(
    val categoryRepository: CategoryRepository = CategoryRepository()
) : ViewModel() {

    val categories = mutableStateOf<List<CategoryEntity>>(emptyList())
    var category = mutableStateOf<CategoryEntity?>(null)
    var name = mutableStateOf<String?>(null)
    val error = mutableStateOf<String?>(null)
    init {
        onPageLoaded()
    }

    fun onPageLoaded() {
        viewModelScope.launch {
            categoryRepository.getPage().fold(
                onSuccess = {
                    categories.value = it
                    onPageLoaded()
                },
                onValidationError = { error.value = "Fehler bei der Eingabevalidierung." },
                onUnexpectedError = { error.value = "Ein unbekannter Fehler ist aufgetreten." }
            )
        }

    }
}