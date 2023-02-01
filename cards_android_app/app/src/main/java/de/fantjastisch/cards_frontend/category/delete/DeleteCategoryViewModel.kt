package de.fantjastisch.cards_frontend.category.delete


import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.category.CategoryRepository
import de.fantjastisch.cards_frontend.infrastructure.fold
import kotlinx.coroutines.launch
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
        viewModelScope.launch {
            categoryRepository.deleteCategory(
                categoryId = categoryId
            ).fold(
                onSuccess = {
                    isFinished.value = true
                }, onValidationError = { error.value = "Fehler bei der Eingabevalidierung." },
                onUnexpectedError = { error.value = "Ein unbekannter Fehler ist aufgetreten." }
            )
        }
    }

    fun onDismissClicked() {
        isFinished.value = true
    }
}
