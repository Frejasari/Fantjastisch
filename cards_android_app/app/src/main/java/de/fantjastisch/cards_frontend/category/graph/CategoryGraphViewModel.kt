package de.fantjastisch.cards_frontend.category.cat_glossary

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.glossary.GlossaryViewModel
import de.fantjastisch.cards_frontend.infrastructure.RepoResult
import kotlinx.coroutines.launch
import org.openapitools.client.models.CardEntity
import org.openapitools.client.models.CategoryEntity

class CatGlossaryViewModel(
    private val catGlossaryModel: CatGlossaryModel = CatGlossaryModel()
) : ViewModel() {

    val categories = mutableStateOf<List<CategoryEntity>>(emptyList())
    val error = mutableStateOf<String?>(null)
    val currentDeleteDialog = mutableStateOf<DeletionProgress?>(null)


    fun onPageLoaded() {
        viewModelScope.launch {
            when (val result = catGlossaryModel.getCategories()) {
                is RepoResult.Success -> {
                    categories.value = result.result
                }
                is RepoResult.Error -> Unit
                is RepoResult.ServerError -> Unit

            }
        }
    }

    fun onTryDeleteCategory(cat: CategoryEntity) {
        currentDeleteDialog.value = DeletionProgress.ConfirmWithUser(cat)
    }

    fun onDeleteCategoryClicked() {
        val cat = currentDeleteDialog.value!!.cat
        currentDeleteDialog.value = DeletionProgress.Deleting(cat)
        error.value = null
        viewModelScope.launch {
            val result = catGlossaryModel.deleteCategory(
                id = cat.id
            )
            when (result) {
                is RepoResult.Success -> {
                    onPageLoaded()
                    currentDeleteDialog.value = null
                }
                is RepoResult.Error,
                is RepoResult.ServerError -> {
                    // Fehler anzeigen:
                    error.value = "Ein Netzwerkfehler ist aufgetreten."
                }
            }
        }
    }

    fun onDeleteCategoryAborted() {
        currentDeleteDialog.value = null
    }

    sealed class DeletionProgress {
        abstract val cat: CategoryEntity

        data class ConfirmWithUser(override val cat: CategoryEntity) : DeletionProgress()
        data class Deleting(override val cat: CategoryEntity) : DeletionProgress()
    }
}