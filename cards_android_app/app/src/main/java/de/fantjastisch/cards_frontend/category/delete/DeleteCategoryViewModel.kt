package de.fantjastisch.cards_frontend.category.delete


import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import de.fantjastisch.cards_frontend.category.CategoryRepository
import java.util.*


class DeleteCategoryViewModel(
    private val categoryRepository: CategoryRepository = CategoryRepository(),
    private val categoryId: UUID
    // : ViewModel() = extends ViewModel
) : ViewModel() {

    val error = mutableStateOf<String?>(null)
    val isFinished = mutableStateOf(false)

    fun onDeleteClicked() {
        error.value = null
        categoryRepository.deleteCategory(
            categoryId = categoryId,
            onSuccess = {
                isFinished.value = true
            },
            onFailure = {
                // Fehler anzeigen:
                error.value = "Irgendwas ist schief gelaufen"

            }
        )
    }

    fun onDismissClicked() {
        isFinished.value = true
    }
}
